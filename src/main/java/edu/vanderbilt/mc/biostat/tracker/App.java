package edu.vanderbilt.mc.biostat.tracker;

import java.util.Date;

public class App {

  public static void main(String[] args) {
    new App().start();
  }
  private Window window;

  public App() {
    window = new Window(this);
  }

  public void start() {
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
    
    return Activity.create(project.id, activityName, new Date(), null);
  }
}
