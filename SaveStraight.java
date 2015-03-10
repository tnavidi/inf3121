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
