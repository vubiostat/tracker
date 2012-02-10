package edu.vanderbilt.mc.biostat.tracker;

import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ActivityTableModel extends AbstractTableModel {

  private Date start, end;

  public ActivityTableModel() {
    super();
    this.start = Utils.today();
    this.end = Utils.tomorrow();
  }
  
  public ActivityTableModel(Date start, Date end) {
    super();
    this.start = start;
    this.end = end;
  }

  public int getRowCount() {
    return Activity.count("started_at >= ? AND ended_at < ?", start, end);
  }

  public int getColumnCount() {
    return 6;
  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
      case 0:
        return "Activity";
      case 1:
        return "Project";
      case 2:
        return "Tags";
      case 3:
        return "Started";
      case 4:
        return "Ended";
      case 5:
        return "Time";
    }
    return null;
  }

  public Object getValueAt(int row, int column) {
    Activity activity = getActivities().get(row);
    switch (column) {
      case 0:
        return activity.name;
      case 1:
        return activity.getProject().name;
      case 2:
        return activity.getTagNames();
      case 3:
        return activity.startedAt;
      case 4:
        return activity.endedAt;
      case 5:
        return activity.getDuration();
    }
    return null;
  }

  private List<Activity> getActivities() {
    return Activity.findAll("started_at >= ? AND ended_at < ?", start, end);
  }
}
