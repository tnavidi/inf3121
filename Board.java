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
