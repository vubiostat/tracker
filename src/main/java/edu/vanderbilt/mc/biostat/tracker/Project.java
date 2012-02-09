package edu.vanderbilt.mc.biostat.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project extends Model {

  public static Project create(String name) {
    HashMap values = new HashMap<String, Object>(2);
    values.put("NAME", name);
    int id = getDatabase().insert("projects", values);

    return new Project(id, name);
  }

  public static Project findById(int id) {
    HashMap values = getDatabase().findById("projects", id);
    if (values == null) {
      return null;
    }
    return new Project(values);
  }

  public static Project findByName(String name) {
    List<Project> records = findAll("name = ?", name);
    if (records.size() > 0) {
      return records.get(0);
    }
    return null;
  }

  public static List findAll() {
    return findAll(null);
  }

  public static List findAll(String conditions, Object... arguments) {
    List<HashMap> records = getDatabase().findAll("projects", conditions, arguments);
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

  public HashMap<String, Object> getAttributes() {
    HashMap attributes = new HashMap<String, Object>(2);
    attributes.put("ID", id);
    attributes.put("NAME", name);
    return attributes;
  }

  public boolean update() {
    return getDatabase().update("projects", getAttributes());
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