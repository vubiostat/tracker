package edu.vanderbilt.mc.biostat.tracker;

import java.util.HashMap;

public abstract class Model {

  private static Database db = null;

  public static void setDatabase(Database d) {
    db = d;
  }

  public static Database getDatabase() {
    if (db == null) {
      db = Database.getInstance();
    }
    return db;
  }
  
  abstract public HashMap<String, Object> getAttributes();
  abstract boolean update();
}