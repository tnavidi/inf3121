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
