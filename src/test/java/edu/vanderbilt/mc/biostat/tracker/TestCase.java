package edu.vanderbilt.mc.biostat.tracker;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class TestCase {

  protected String databasePath;

  @Before
  public void setUp() {
    try {
      File tempFile = File.createTempFile("tracker", "db");
      tempFile.deleteOnExit();
      databasePath = tempFile.getAbsolutePath();
    } catch (IOException ex) {
      Assert.fail("Couldn't create temporary file: " + ex.toString());
    }
  }

  @After
  public void tearDown() {
  }

  protected Database getDatabase() {
    return new Database(databasePath);
  }

  protected Connection getConnection() {
    Connection conn = null;
    try {
      Class.forName("org.h2.Driver");
      conn = DriverManager.getConnection("jdbc:h2:" + databasePath);
    } catch (Exception ex) {
      Assert.fail("Couldn't open H2 connection: " + ex.toString());
    }
    return conn;
  }
}
