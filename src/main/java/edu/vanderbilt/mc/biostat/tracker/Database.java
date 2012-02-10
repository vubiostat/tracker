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

  public List findAll(String tableName) {
    return findAll(tableName, null);
  }

  public List findAll(String tableName, String conditions, Object... arguments) {
    List records = new ArrayList<HashMap>();
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

  public boolean update(String tableName, HashMap<String, Object> values) {
    String query = "UPDATE " + tableName + " SET ";
    values = (HashMap) values.clone();
    int id = (Integer) values.remove("ID");
    Set keys = values.keySet();

    for (Iterator i = keys.iterator(); i.hasNext();) {
      String key = (String) i.next();
      query += i.hasNext() ? key + " = ?, " : key + " = ? ";
    }
    query += "WHERE ID = ?";

    List parameters = new ArrayList<Object>(values.values());
    parameters.add(id);

    try {
      PreparedStatement stmt = conn.prepareStatement(query);
      for (int i = 0; i < parameters.size(); i++) {
        stmt.setObject(i + 1, parameters.get(i));
      }
      return stmt.executeUpdate() == 1;
    } catch (SQLException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
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
    if (getSchemaVersion() == -1) {
      try {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE schema_info (version INT); "
                + "CREATE TABLE activities (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), project_id INT, started_at TIMESTAMP, ended_at TIMESTAMP);"
                + "CREATE TABLE projects (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255));"
                + "INSERT INTO schema_info (version) VALUES (0);");
      } catch (SQLException ex) {
        throw new DatabaseMigrationException("Couldn't migrate database: " + ex.toString());
      }
    }
  }
}
