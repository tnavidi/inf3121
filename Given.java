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
