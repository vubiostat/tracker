package edu.vanderbilt.mc.biostat.tracker;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class TestCase {

  protected String databasePath;

  @Before
  public void setUp() {
    SecureRandom random = new SecureRandom();
    String tmpDir = System.getProperty("java.io.tmpdir");
    String databaseName;
    File databaseFile;
    do {
      databaseName = "tracker" + Math.abs(random.nextLong());
      databaseFile = new File(databaseName + ".h2.db", tmpDir);
    } while (databaseFile.exists());
    databasePath = databaseFile.getAbsolutePath();
  }

  @After
  public void tearDown() {
    new File(databasePath + ".h2.db").delete();
    new File(databasePath + ".trace.db").delete();
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
