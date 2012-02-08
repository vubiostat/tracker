package edu.vanderbilt.mc.biostat.tracker;

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
}