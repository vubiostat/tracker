/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.vanderbilt.mc.biostat.tracker;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.junit.*;

public class DatabaseTest {

  private String databasePath;

  public DatabaseTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    try {
      File tempFile = File.createTempFile("tracker", ".db");
      tempFile.deleteOnExit();
      databasePath = tempFile.getAbsolutePath();
    } catch (IOException ex) {
      Assert.fail("Couldn't create temporary file: " + ex.toString());
    }
  }

  @After
  public void tearDown() {
  }

  private Database getDatabase() {
    return new Database(databasePath);
  }

  private Connection getConnection() {
    Connection conn = null;
    try {
      Class.forName("org.h2.Driver");
      conn = DriverManager.getConnection("jdbc:h2:" + databasePath);
    } catch (Exception ex) {
      Assert.fail("Couldn't open H2 connection: " + ex.toString());
    }
    return conn;
  }

  @Test
  public void createTablesOnFirstUse() {
    Database db = getDatabase();
    Assert.assertEquals(0, db.getSchemaVersion());
    db.close();

    Set expectedTables = new HashSet<String>();
    expectedTables.add("ACTIVITIES");
    expectedTables.add("PROJECTS");
    expectedTables.add("SCHEMA_INFO");

    Set actualTables = new HashSet<String>();
    Connection conn = getConnection();
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SHOW TABLES");
      while (rs.next()) {
        actualTables.add(rs.getString("TABLE_NAME"));
      }
    } catch (SQLException ex) {
      Assert.fail("Couldn't get schema: " + ex.toString());
    }

    Assert.assertEquals(expectedTables, actualTables);
  }

  @Test
  public void insertingRecords() {
    Database db = getDatabase();

    HashMap values = new HashMap<String, Object>();
    values.put("name", "foo");
    int id_1 = db.insert("projects", values);
    Assert.assertEquals(1, id_1);
    
    values.put("name", "bar");
    int id_2 = db.insert("projects", values);
    Assert.assertEquals(2, id_2);

    db.close();

    Connection conn = getConnection();
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS num FROM projects WHERE id IN (" + id_1 + ", " + id_2 + ")");
      while (rs.next()) {
        Assert.assertEquals(2, rs.getInt("num"));
      }
    } catch (SQLException ex) {
      Assert.fail("Couldn't get count: " + ex.toString());
    }
  }
}