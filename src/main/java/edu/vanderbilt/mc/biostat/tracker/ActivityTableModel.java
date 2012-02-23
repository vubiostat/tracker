package edu.vanderbilt.mc.biostat.tracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

public class ActivityTableModel extends AbstractTableModel {

  private Date start, end;
  private List<Activity> activities;

  public ActivityTableModel() {
    this(Utils.today(), Utils.tomorrow());
  }

  public ActivityTableModel(Date start, Date end) {
    super();
    this.start = start;
    this.end = end;
    updateActivities();
  }

  public int getRowCount() {
    return activities.size();
  }

  public int getColumnCount() {
    return 7;
  }

  @Override
  public String getColumnName(int column) {
    switch (column) {
      case 0:
        return "Started";
      case 1:
        return "";
      case 2:
        return "Ended";
      case 3:
        return "Activity";
      case 4:
        return "Project";
      case 5:
        return "Tags";
      case 6:
        return "Time";
    }
    return null;
  }

  public Object getValueAt(int row, int column) {
    Activity activity = activities.get(row);
    switch (column) {
      case 0:
        return Utils.shortTime(activity.startedAt);
      case 1:
        return "-";
      case 2:
        if (activity.endedAt != null) {
          return Utils.shortTime(activity.endedAt);
        }
        return "";

      case 3:
        return activity.name;
      case 4:
        return activity.getProject().name;
      case 5:
        return activity.getTagNames();
      case 6:
        return activity.getDurationString();
    }
    return null;
  }

  public void updateActivities() {
    activities = Activity.findAll("started_at >= ? AND (ended_at IS NULL OR ended_at < ?)", start, end);
    fireTableDataChanged();
  }
}
