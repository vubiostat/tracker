/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.vanderbilt.mc.biostat.tracker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class DatabaseTest extends TestCase {

  public DatabaseTest() {
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

  @Test
  public void findById() {
    Database db = getDatabase();

    HashMap values = new HashMap<String, Object>();
    values.put("NAME", "foo");
    int id = db.insert("projects", values);
    values.put("ID", id);

    HashMap actualValues = db.findById("projects", id);
    Assert.assertEquals(values, actualValues);
  }

  @Test
  public void findAll() {
    Database db = getDatabase();

    HashMap values = new HashMap<String, Object>();
    values.put("NAME", "foo_1");
    db.insert("projects", values);

    values.put("NAME", "foo_2");
    db.insert("projects", values);

    values.put("NAME", "bar");
    db.insert("projects", values);

    List records = db.findAll("projects");
    Assert.assertEquals(3, records.size());
  }

  @Test
  public void findAllWithConditions() {
    Database db = getDatabase();

    HashMap values = new HashMap<String, Object>();
    values.put("NAME", "foo_1");
    db.insert("projects", values);

    values.put("NAME", "foo_2");
    db.insert("projects", values);

    values.put("NAME", "bar");
    db.insert("projects", values);

    List records = db.findAll("projects", "name LIKE ?", "foo%");
    Assert.assertEquals(2, records.size());
  }
}