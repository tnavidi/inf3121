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
