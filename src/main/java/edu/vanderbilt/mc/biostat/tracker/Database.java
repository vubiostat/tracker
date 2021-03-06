package edu.vanderbilt.mc.biostat.tracker;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    } catch (SQLException ex) {
    }

    return (version);
  }

  public int insert(String tableName, HashMap<String, Object> values) {
    int result = 0;
    String query = "INSERT INTO " + tableName + " (";
    String predicate = "VALUES (";
    Set keys = values.keySet();
    for (Iterator i = keys.iterator(); i.hasNext();) {
      String key = (String) i.next();
      if (i.hasNext()) {
        query += key + ", ";
        predicate += "?, ";
      } else {
        query += key + ") ";
        predicate += "?)";
      }
    }
    query += predicate;

    try {
      PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      int index = 1;
      for (Object value : values.values()) {
        stmt.setObject(index++, value);
      }
      stmt.executeUpdate();

      ResultSet rs = stmt.getGeneratedKeys();
      while (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (SQLException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
      result = -1;
    }
    return result;
  }

  public HashMap findById(String tableName, int id) {
    List records = findAll(tableName, "id = ?", id);
    return records.size() > 0 ? (HashMap) records.get(0) : (HashMap) null;
  }

  public List<HashMap> findAll(String tableName) {
    return findAll(tableName, null);
  }

  public List<HashMap> findAll(String tableName, String conditions, Object... arguments) {
    List<HashMap> records = new ArrayList<HashMap>();
    try {
      String query = "SELECT * FROM " + tableName;
      if (conditions != null) {
        query += " WHERE " + conditions;
      }

      PreparedStatement stmt = conn.prepareStatement(query);
      for (int i = 0; i < arguments.length; i++) {
        stmt.setObject(i + 1, arguments[i]);
      }

      ResultSet rs = stmt.executeQuery();
      ResultSetMetaData md = rs.getMetaData();
      while (rs.next()) {
        HashMap values = new HashMap<String, Object>();
        for (int i = 1; i <= md.getColumnCount(); i++) {
          values.put(md.getColumnName(i), rs.getObject(i));
        }
        records.add(values);
      }
    } catch (SQLException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
    }
    return records;
  }

  public boolean updateById(String tableName, HashMap<String, Object> values) {
    values = (HashMap) values.clone();
    int id = (Integer) values.remove("ID");
    return update(tableName, values, "ID = ?", id) == 1;
  }

  public int update(String tableName, HashMap<String, Object> values, String conditions, Object... arguments) {
    String query = "UPDATE " + tableName + " SET ";
    List parameters = new ArrayList<Object>(values.size() + arguments.length);
    Set keys = values.keySet();

    for (Iterator i = keys.iterator(); i.hasNext();) {
      String key = (String) i.next();
      query += i.hasNext() ? key + " = ?, " : key + " = ? ";
      parameters.add(values.get(key));
    }
    if (conditions != null) {
      query += "WHERE " + conditions;
      parameters.addAll(Arrays.asList(arguments));
    }

    try {
      PreparedStatement stmt = conn.prepareStatement(query);
      for (int i = 0; i < parameters.size(); i++) {
        stmt.setObject(i + 1, parameters.get(i));
      }
      return stmt.executeUpdate();
    } catch (SQLException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
      return 0;
    }
  }

  public int count(String tableName) {
    return count(tableName, null);
  }

  public int count(String tableName, String conditions, Object... arguments) {
    try {
      String query = "SELECT COUNT(*) FROM " + tableName;
      if (conditions != null) {
        query += " WHERE " + conditions;
      }

      PreparedStatement stmt = conn.prepareStatement(query);
      for (int i = 0; i < arguments.length; i++) {
        stmt.setObject(i + 1, arguments[i]);
      }

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
  }

  public boolean deleteById(String tableName, int id) {
    return delete(tableName, "ID = ?", id) == 1;
  }

  public int delete(String tableName, String conditions, Object... arguments) {
    try {
      String query = "DELETE FROM " + tableName;
      if (conditions != null) {
        query += " WHERE " + conditions;
      }

      PreparedStatement stmt = conn.prepareStatement(query);
      for (int i = 0; i < arguments.length; i++) {
        stmt.setObject(i + 1, arguments[i]);
      }

      return stmt.executeUpdate();
    } catch (SQLException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
    }
    return -1;
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

    migrate();
  }

  private void migrate() {
    mainloop:
    for (int version = getSchemaVersion();; version++) {
      String query = null;

      switch (version) {
        case -1:
          query = "CREATE TABLE schema_info (version INT); "
                  + "CREATE TABLE activities (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), project_id INT, started_at TIMESTAMP, ended_at TIMESTAMP); "
                  + "CREATE TABLE projects (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255)); "
                  + "INSERT INTO schema_info (version) VALUES (0);";
          break;
        case 0:
          query = "CREATE TABLE tags (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255)); "
                  + "CREATE TABLE activities_tags (activity_id INT, tag_id INT); "
                  + "UPDATE schema_info SET version = 1;";
          break;
        default:
          break mainloop;
      }
      try {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
      } catch (SQLException ex) {
        throw new DatabaseMigrationException("Couldn't migrate database from version " + version + ": " + ex.toString());
      }
    }
  }
}
