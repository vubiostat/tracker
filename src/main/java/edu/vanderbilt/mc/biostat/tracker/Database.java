/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.vanderbilt.mc.biostat.tracker;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stephej1
 */
public class Database {

  private static Database _instance;

  public static Database getInstance() {
    if (_instance == null) {
      _instance = new Database();
    }
    return _instance;
  }
  private Connection conn;
  private String databasePath;

  public Database() {
    databasePath = System.getProperty("tracker.databasePath");
    if (databasePath == null) {
      File settingsDirectory = getSettingsDirectory();
      databasePath = new File(settingsDirectory, "data").getAbsolutePath();
    }
    setupConnection();
  }

  public Database(String databasePath) {
    this.databasePath = databasePath;
    setupConnection();
    migrate();
  }
  
  public void close() {
    try {
      conn.close();
    } catch (SQLException ex) {
    }
  }
  
  public int getSchemaVersion() {
    int version = -1;
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT version FROM schema_info");
      while (rs.next()) {
        version = rs.getInt("version");
      }
    } catch (SQLException ex) { }
    
    return(version);
  }

  private File getSettingsDirectory() {
    String userHome = System.getProperty("user.home");
    if (userHome == null) {
      throw new IllegalStateException("Couldn't determine user's home directory");
    }
    File home = new File(userHome);
    File settingsDirectory = new File(home, ".tracker");
    if (!settingsDirectory.exists()) {
      if (!settingsDirectory.mkdir()) {
        throw new IllegalStateException(settingsDirectory.toString());
      }
    }
    return settingsDirectory;
  }

  private void setupConnection() {
    try {
      Class.forName("org.h2.Driver");
    } catch (ClassNotFoundException ex) {
      throw new DatabaseConnectionException("Couldn't find the H2 driver: " + ex.toString());
    }
    try {
      conn = DriverManager.getConnection("jdbc:h2:" + databasePath);
    } catch (SQLException ex) {
      throw new DatabaseConnectionException("Couldn't open the database: " + ex.toString());
    }
  }
  
  private void migrate() {
    if (getSchemaVersion() == -1) {
      try {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE schema_info (version INT); "
                + "CREATE TABLE activities (id INT PRIMARY KEY, name VARCHAR(255), project_id INT, started_at TIMESTAMP, ended_at TIMESTAMP);"
                + "CREATE TABLE projects (id INT PRIMARY KEY, name VARCHAR(255));"
                + "INSERT INTO schema_info (version) VALUES (0);");
      } catch (SQLException ex) {
        throw new DatabaseMigrationException("Couldn't migrate database: " + ex.toString());
      }
    }
  }
}
