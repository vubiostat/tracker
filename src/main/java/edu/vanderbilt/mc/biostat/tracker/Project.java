package edu.vanderbilt.mc.biostat.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author stephej1
 */
public class Project {

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

  public static Project create(String name) {
    HashMap values = new HashMap<String, Object>(2);
    values.put("name", name);
    int id = getDatabase().insert("projects", values);
    
    return new Project(id, name);
  }
  
  public static Project findById(int id) {
    HashMap values = getDatabase().findById("projects", id);
    if (values == null)
      return null;
    return new Project(values);
  }
  
  public static List findAll() {
    List<HashMap> records = getDatabase().findAll("projects");
    List projects = new ArrayList<Project>(records.size());
    for (HashMap attributes : records) {
      projects.add(new Project(attributes));
    }
    return projects;
  }
  
  public int id;
  public String name;

  private Project(int id, String name) {
    this.id = id;
    this.name = name;
  }
  
  private Project(HashMap<String, Object> attributes) {
    this.id = (Integer) attributes.get("ID");
    this.name = (String) attributes.get("NAME");
  }
  
  @Override
  public boolean equals(Object other) {
    if (other instanceof Project) {
      Project p = (Project) other;
      return this.id == p.id && (this.name == null ? p.name == null : this.name.equals(p.name));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 71 * hash + this.id;
    hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }
}