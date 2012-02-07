/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.vanderbilt.mc.biostat.tracker;

/**
 *
 * @author stephej1
 */
public class DatabaseMigrationException extends RuntimeException {

  /**
   * Creates a new instance of
   * <code>DatabaseMigrationException</code> without detail message.
   */
  public DatabaseMigrationException() {
  }

  /**
   * Constructs an instance of
   * <code>DatabaseMigrationException</code> with the specified detail message.
   *
   * @param msg the detail message.
   */
  public DatabaseMigrationException(String msg) {
    super(msg);
  }
}
