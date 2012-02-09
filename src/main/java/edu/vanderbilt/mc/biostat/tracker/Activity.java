package edu.vanderbilt.mc.biostat.tracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Activity extends Model {

  public static Activity create(int projectId, String name, Date startedAt, Date endedAt) {
    HashMap values = new HashMap<String, Object>(4);
    values.put("PROJECT_ID", projectId);
    values.put("NAME", name);
    values.put("STARTED_AT", startedAt);
    values.put("ENDED_AT", endedAt);
    int id = getDatabase().insert("activities", values);

    return new Activity(id, projectId, name, startedAt, endedAt);
  }

  public static Activity findById(int id) {
    HashMap values = getDatabase().findById("activities", id);
    if (values == null) {
      return null;
    }
    return new Activity(values);
  }

  public static List findAll() {
    List<HashMap> records = getDatabase().findAll("activities");
    List Activitys = new ArrayList<Activity>(records.size());
    for (HashMap attributes : records) {
      Activitys.add(new Activity(attributes));
    }
    return Activitys;
  }
  public int id;
  public int projectId;
  public String name;
  public Date startedAt;
  public Date endedAt;

  private Activity(int id, int projectId, String name, Date startedAt, Date endedAt) {
    this.id = id;
    this.projectId = projectId;
    this.name = name;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
  }

  private Activity(HashMap<String, Object> attributes) {
    this.id = (Integer) attributes.get("ID");
    this.projectId = (Integer) attributes.get("PROJECT_ID");
    this.name = (String) attributes.get("NAME");
    this.startedAt = (Date) attributes.get("STARTED_AT");
    this.endedAt = (Date) attributes.get("ENDED_AT");
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Activity) {
      Activity a = (Activity) other;
      return this.id == a.id
              && this.projectId == a.projectId
              && (this.name == null ? a.name == null : this.name.equals(a.name))
              && (this.startedAt == null ? a.startedAt == null : this.startedAt.equals(a.startedAt))
              && (this.endedAt == null ? a.endedAt == null : this.endedAt.equals(a.endedAt));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 53 * hash + this.id;
    hash = 53 * hash + this.projectId;
    hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
    hash = 53 * hash + (this.startedAt != null ? this.startedAt.hashCode() : 0);
    hash = 53 * hash + (this.endedAt != null ? this.endedAt.hashCode() : 0);
    return hash;
  }
}