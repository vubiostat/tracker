/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.vanderbilt.mc.biostat.tracker;

/**
 *
 * @author stephej1
 */
public class DatabaseConnectionException extends RuntimeException {

  /**
   * Creates a new instance of
   * <code>DatabaseConnectionException</code> without detail message.
   */
  public DatabaseConnectionException() {
  }

  /**
   * Constructs an instance of
   * <code>DatabaseConnectionException</code> with the specified detail message.
   *
   * @param msg the detail message.
   */
  public DatabaseConnectionException(String msg) {
    super(msg);
  }
}
