/**
 *       INF1010
 *
 *     Vaaren  2013
 *
 * Obligatorisk oppgave 5
 *
 *        Sudoku
 *
 *          av
 *
 *      Tom Navidi
 */

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * GUI
 */
class SudokuGUI extends JFrame {

  Sudokubeholder beholder;
  Board board;
  int sSize;
  int hSize;
  int vSize;
  char[][][] solution;

  boolean exit = false;
  boolean[] displaySolution = {false};
  
  /**
   * The user interface
   * @param beholder Sudokubeholder containing the solutions.
   * @param board The board
   */
  public SudokuGUI(Sudokubeholder beholder, Board board) {
    super("SUDOKU av Tom Navidi");
    this.beholder = beholder;
    this.board = board;
    sSize = board.getSize();
    hSize = board.getBoxSizeH();
    vSize = board.getBoxSizeV();
    
    addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {
	  shutdown(e);
	}
      });
    
    setSize(1024, 1024);
    setVisible(true);
    Container main = getContentPane();
    JMenuBar menubar = new JMenuBar();
    menubar.add(new JMenu("File"){
	JMenu addItems() {
	  add(new JMenuItem("Exit") {
	      JMenuItem addListener() {
		addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		      shutdown(e);
		    }
		  });
		return this;
	      }
	    }.addListener());
	  return this;
	}
      }.addItems());
    setJMenuBar(menubar);
    Container buttonContainer = new Container();
    buttonContainer.setLayout(new FlowLayout());
    JButton nextButton = new JButton("Next Solution");
    nextButton.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	  nextButtonPressed(e);
	}
      });
    JButton orgButton = new JButton("Show original board");
    orgButton.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	  orgButtonPressed(e);
	}
      });
      buttonContainer.add(nextButton);
      buttonContainer.add(orgButton);
    Container sudokuContainer = new Container();
    sudokuContainer.setLayout(new GridLayout(sSize, sSize));

    solution = new char[sSize][sSize][1];
    JSquare p = null;
    JSquare u = null;
    for (int i = 0; i < sSize; i++) {
      for (int j = 0; j < sSize; j++) {
	JSquare s = new JSquare(board.getSquare(j,i), displaySolution, 
				solution[i][j]);
	if (j > 0) {
	  p.right = s;
	  s.left = p;
	  if (i > 0) {
	    s.up = p.up.right;
	    s.up.down = s;
	  }
	} else {
	  if (i == 0) {
	    s.requestFocusInWindow();
	  } else {
	    s.up = u;
	    u.down = s;
	  }
	  u = s;
	}
	p = s;
	s.setBorder(new LineBorder(Color.BLACK));
	sudokuContainer.add(s);
	
      }
      
    }
    main.add(buttonContainer, BorderLayout.NORTH);
    main.add(sudokuContainer, BorderLayout.CENTER);
    revalidate();
    repaint();
  }

  /**
   * Checks that everything is clear for closing, then exits.
   */
  void shutdown(AWTEvent e) {
    exit = true;
  }

  /**
   * Shows the next solution, and changes view to "solution mode"
   */
  void nextButtonPressed(ActionEvent e) {
    displaySolution[0] = true;
    String[] s = beholder.get();
    if (s == null) {
      JOptionPane.showMessageDialog(this, "No more solutions");
    } else {
      for (int i = 0; i < sSize; i++) {
	char[] c = s[i].toCharArray();
	for (int j = 0; j < sSize; j++) {
	  solution[j][i][0] = c[j];
	}
      }
    }
    revalidate();
    repaint();
  }
  
  /**
   * Shows the original sudokuboard that is being used.
   */
  void orgButtonPressed(ActionEvent e) {
    displaySolution[0] = false;
    revalidate();
    repaint();
  }

  /**
   * JSquare
   */
  class JSquare extends JComponent implements FocusListener{
    Square square;
    boolean focused;
    JSquare up,down,left,right;
    char[] solution;
    boolean[] displaySolution;
    
    /**
     * Constructor
     * @param square The square from the sudoku board (was planning
     * to make editable board)
     * @param displaySolution keeps track if solution or original board
     * is to be displayed.
     * @param solution The Solved number to be displayed as part of 
     * the solution
     */
    JSquare(Square square, boolean[] displaySolution, char[] solution) {
      this.square = square;
      this.solution = solution;
      this.displaySolution = displaySolution;
      setFocusable(true);
    }

    /**
     * paints the number inside the square.
     */
    public void paint(Graphics gr) {
      super.paint(gr);
      Graphics2D g = (Graphics2D) gr;
      if (focused) {
	g.setColor(Color.YELLOW);
	g.fillRect(1,1,getWidth()-1, getHeight()-1);
      }
      g.setColor(Color.BLACK);
      g.drawRect(0,0,getWidth()-1, getHeight()-1);
      g.setFont(new Font("New Courier", Font.PLAIN, 40));
      String s = null;
      if (displaySolution[0]) s = ""+solution[0]; 
      else s = square.getChar();
      g.drawString(s,getWidth()/2,getHeight()/2);
    }

    /**
     * This doesn't work at all
     */
    public void focusGained(FocusEvent e) {
      focused = true;
    }

    /**
     * This doesn't work at all either.
     */
    public void focusLost(FocusEvent e) {
      focused = false;
    }
  }
}

