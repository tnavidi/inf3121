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
