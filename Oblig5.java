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

class Oblig5 {

  /**
   * Main method handles the command line parameters.
   * Usage: java Oblig5 <infile> <outfile>
   * where infile is the filename of the sudokuboard to be read.
   * If infile is left out the program will prompt for a file.
   * The outfile is optional and used for writing the results
   * to the specified filename.
   */
  public static void main(String [] args) {
    Scanner scanner=null;
    String outfile;
    if (args.length == 0) {
      JFileChooser jfc = new JFileChooser();
      jfc.setCurrentDirectory(new java.io.File("."));
      if (jfc.showOpenDialog(null) == jfc.APPROVE_OPTION) {
	try {
	  scanner = new Scanner(jfc.getSelectedFile());
	} catch (FileNotFoundException e) {
	  JOptionPane.showMessageDialog(null, "Couldn't open the file: " +
					e.getMessage());
	}
      }
    } else try {
	scanner = new Scanner(new File(args[0]));
      } catch (FileNotFoundException e) {
	System.out.println("Couldn't open the file: " + e.getMessage());
      }
    //user pressed cancel or file not found
    if (scanner == null) return;
    Sudokubeholder beholder = null;
    if (args.length == 2) beholder = new SaveStraight(args[1]);
    else beholder = new Sudokubeholder();
    try {
      int size;
      Board board = new Board(size = scanner.nextInt(), scanner.nextInt(),
			      scanner.nextInt(), beholder);
      scanner.nextLine();
      StringBuffer values = new StringBuffer();
      for (int i = 0; i < size; i++) {
	values.append(scanner.nextLine());
      }
      board.addSquares(0, 0, values.toString());
    } catch (NoSuchElementException e) {
      String msg = "This file can not be read.";
      if (args.length==0) JOptionPane.showMessageDialog(null, msg);
      else System.out.println(msg);
      return;
    }
    Board.mainBoard.getSquare(0,0).fillInnRemainingOfBoard();
    if (args.length == 2) ((SaveStraight)beholder).close();
    else {
      SudokuGUI gui = new SudokuGUI(beholder, Board.mainBoard);
      while (!gui.exit) {
	//interact with GUI here
	
	try {
	  Thread.sleep(50);
	} catch (InterruptedException e) {
	  
	}
      }
      gui.dispose();
    }
    
  }
}

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

class Sudokubeholder {

  int solutions;
  static Sudokubeholder beholder;
  
  LinkedList<String[]> ll = new LinkedList<String[]>();
  
  public Sudokubeholder() {
    beholder = this;
  }
  
  public void insert(String[] rows) {
    if (solutions++ < 500) ll.add(rows);
  }

  public String[] get() {
    if (ll.size() == 0) return null;
    return ll.pop();
  }

  public int getSolutionCount() {
    return solutions;
  }

}

/**
 * This class is to be used instead of sudokubeholder, when
 * all solutions is to be written to file.
 */
class SaveStraight extends Sudokubeholder {

  PrintWriter writer;

  /**
   * Constructor.
   * @param filename filename of file to be written.
   */
  public SaveStraight(String filename) {
    try {
      writer = new PrintWriter(filename);
    } catch (IOException e) {
      System.out.println("Couldn't write file: " + filename);
      System.out.println(e.getMessage());
      System.exit(1);
    }
  }

  /**
   * adds a solution to the file, with the specified format
   * as described in the requirements.
   */
  public void insert(String[] rows) {
    StringBuffer sb = new StringBuffer();
    sb.append(++solutions).append(": ");
    for (String r: rows) {
      sb.append(r).append("// ");
    }
    writer.println(sb);
  }

  /**
   * Closes the file
   */
  public void close() {
    writer.close();
  }
}

class Board {

  static Board mainBoard;

  private int size;
  private int m, n;
  private Square[][] grid;
  private Box[][] boxes;
  private Row[] rows;
  private Column[] cols;
  private Sudokubeholder beholder;
  
  public static Board getBoard() {
    return mainBoard;
  }
  
  public Board(int size, int m, int n, Sudokubeholder beholder) {
    mainBoard = this;
    this.size = size;
    this.m = m;
    this.n = n;
    this.beholder = beholder;
    grid = new Square[size][size];
    boxes = new Box[size/m][size/n];
    cols = new Column[size];
    rows = new Row[size];
    Square.size = size;
    for (int i = 0; i < size; i++) {
      rows[i] = new Row();
      cols[i] = new Column();
      boxes[i/m][i%(size/n)] = new Box();
    }
  }

  /**
   * Recursively add squares to the board.
   * @param i grid index
   * @param j grid index
   * @param values Remaining values to be inserted.
   * @param the added square
   */
  Square addSquares(int i, int j, String values) {
    if (values.length() == 0) return null;
    Square v = null;
    Row r = rows[i];
    Column c = cols[j];
    Box b = boxes[i/m][j/n];
    Square next = addSquares(i+(j/(size-1)), (j+1)%size, 
			     values.substring(1));
    if (values.charAt(0) == '.') {
      v = new Candidate(b, c, r, next);
    } else {
      v = new Given(b, c, r, next, values.charAt(0));
    }
    grid[i][j] = v;
    return v;
  }
  
  int getSize() {
    return size;
  }

  int getBoxSizeH() {
    return m;
  }

  int getBoxSizeV() {
    return n;
  }

  Square getSquare(int i, int j) {
    return grid[i][j];
  }

  void addSolution() {
    String[] solution = new String[size];
    for (int i = 0; i < size; i++) {
      StringBuffer sb = new StringBuffer();
      for (Square c: grid[i]) {
	sb.append(c.getChar());
      }
      solution[i] = sb.toString();
    }
    beholder.insert(solution);
  }
}

abstract class Square {

  private int value;
  
  private Square next;
  private Box box;
  private Column col;
  private Row row;
  static int size;
  
  public Square(Box box, Column col, Row row, Square next) {
    value = 0;
    this.box = box;
    this.col = col;
    this.row = row;
    this.next = next;

    box.add(this);
    col.add(this);
    row.add(this);
  }

  /**
   * Set given value
   * @param value The predefined given value.
   */
  void setValue(char value) {
    this.value = (int)(value>64?value-65:value-48);
  }

  void setValue(int value) {
    this.value = value;
  }

  /**
   * Check if value can be inserted in current square
   * @param value Value to be checked
   * @return true if value is not used on any of the row,
   * box or column.
   */
  boolean checkValue(int value) {
    return (box.checkValue(value) && 
	    col.checkValue(value) && 
	    row.checkValue(value));
  }

  /**
   * Recursive brute force solver.
   * Tries every value; and recursively
   * calls the next square.
   */
  abstract void fillInnRemainingOfBoard();

  void addSolution() {
    Board.mainBoard.addSolution();
  }
  
  public String getChar() {
    if (value == 0) return "";
    return ""+(char)(value+(value>9?55:48));
  }

  boolean hasNext() {
    return next != null;
  }

  boolean equals(int value) {
    return value==this.value;
  }

  Box getBox() {
    return box;
  }

  Column getColumn() {
    return col;
  }

  Row getRow() {
    return row;
  }

  Square getNext() {
    return next;
  }

}

class Given extends Square {
  public Given(Box box, Column col, Row row, Square next, char value) {
    super(box, col, row, next);
    setValue(value);
  }

  /**
   * Used for converting between given and candidate
   * (in case it will be necessary for solving extra
   * features where users can enter (given) numbers 
   * directly into the GUI)
   * @param src the candidate to be converted
   * @param value The character to be used for the given.
   */
  public Given(Square src, char value) {
    super(src.getBox(), src.getColumn(), src.getRow(), src.getNext());
    setValue(value);
  }

  public void fillInnRemainingOfBoard() {
    if (hasNext()) {
      getNext().fillInnRemainingOfBoard();      
    } else {
      addSolution();
    }
  }
}


class Candidate extends Square {

  /**
   * Constructor for Candidate.
   * @param box The box that this square belongs to
   * @param col The column that this square belongs to
   * @param row The row that this square belongs to
   * @param next The next square in the remaining board.
   */
  public Candidate(Box box, Column col, Row row, Square next){
    super(box, col, row, next);
  }

  /**
   * Used for converting between given and candidate
   * @param src the given to be converted.
   */
  public Candidate(Given src) {
    super(src.getBox(), src.getColumn(), src.getRow(), src.getNext());
  }

  /**
   * Recursive method that finds all solutions. 
   * To be called from the top left square.
   * The method will insert the found solutions
   * inside the solution container (SudokuBeholder).
   */
  public void fillInnRemainingOfBoard() {
    for (int i = 0; i <= size; i++) {
      if(checkValue(i)) {
	setValue(i);
	if (hasNext()) {
	  getNext().fillInnRemainingOfBoard();
	} else {
	  addSolution();
	}
      }
      setValue(0);
    }
  }
}

class Area {

  Square[] cells;
  private int pos = 0;

  Area() {
    cells = new Square[Board.mainBoard.getSize()];
  }

  /**
   * Checks if a value is permitted to be added to this area.
   * @param value The value to be tested
   * @return true if value can be added.
   */
  boolean checkValue(int value) {
    for(Square c: cells) if (c.equals(value)) return false;
    return true;
  }

  void add(Square sq) {
    cells[pos++] = sq;
  }
}

class Box extends Area {
  
}

class Column extends Area {
  
}

class Row extends Area {

}


