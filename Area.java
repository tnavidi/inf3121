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
