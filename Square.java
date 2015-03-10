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
