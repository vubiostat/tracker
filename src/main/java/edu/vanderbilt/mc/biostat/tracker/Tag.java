package edu.vanderbilt.mc.biostat.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tag extends Model {

  public static Tag create(String name) {
    HashMap values = new HashMap<String, Object>(2);
    values.put("NAME", name);
    int id = getDatabase().insert("tags", values);

    return new Tag(id, name);
  }

  public static Tag findById(int id) {
    HashMap values = getDatabase().findById("tags", id);
    if (values == null) {
      return null;
    }
    return new Tag(values);
  }

  public static Tag findByName(String name) {
    List<Tag> records = findAll("name = ?", name);
    if (records.size() > 0) {
      return records.get(0);
    }
    return null;
  }

  public static List findAll() {
    return findAll(null);
  }

  public static List findAll(String conditions, Object... arguments) {
    List<HashMap> records = getDatabase().findAll("tags", conditions, arguments);
    List tags = new ArrayList<Tag>(records.size());
    for (HashMap attributes : records) {
      tags.add(new Tag(attributes));
    }
    return tags;
  }
  public int id;
  public String name;

  private Tag(int id, String name) {
    this.id = id;
    this.name = name;
  }

  private Tag(HashMap<String, Object> attributes) {
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
    return getDatabase().updateById("tags", getAttributes());
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Tag) {
      Tag p = (Tag) other;
      return this.id == p.id && (this.name == null ? p.name == null : this.name.equals(p.name));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 23 * hash + this.id;
    hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
    return hash;
  }
}