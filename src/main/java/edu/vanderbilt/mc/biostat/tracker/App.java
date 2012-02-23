package edu.vanderbilt.mc.biostat.tracker;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

public class App {

  public static void main(String[] args) {
    // set look and feel
    String os = System.getProperty("os.name");
    String lafName;
    if (os.equals("Linux")) {
      lafName = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
    } else {
      lafName = UIManager.getSystemLookAndFeelClassName();
    }
    try {
      UIManager.setLookAndFeel(lafName);
    } catch (Exception ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
    }

    new App().start();
  }
  private Window window;

  public App() {
    window = new Window(this);
  }

  public void start() {
    stopAllActivities();
    window.setVisible(true);
  }

  public Activity startActivity(String fullName, String tags) {
    String[] nameParts = fullName.split("@", 2);
    String activityName = nameParts[0];
    String projectName = nameParts.length == 1 ? "unsorted" : nameParts[1];

    Project project = Project.findByName(projectName);
    if (project == null) {
      project = Project.create(projectName);
    }

    Activity activity = Activity.create(project.id, activityName, new Date(), null);

    if (tags != null) {
      String[] tagNames = tags.split(", *");
      for (String tagName : tagNames) {
        Tag tag = Tag.findByName(tagName);
        if (tag == null) {
          tag = Tag.create(tagName);
        }
        activity.addTag(tag);
      }
    }

    return activity;
  }

  public boolean stopActivity(Activity activity) {
    activity.endedAt = new Date();
    return activity.update();
  }
  
  public void stopAllActivities() {
    HashMap values = new HashMap<String, Object>(1);
    values.put("ended_at", new Date());
    Activity.updateAll(values, "ended_at IS NULL");
  }
}
