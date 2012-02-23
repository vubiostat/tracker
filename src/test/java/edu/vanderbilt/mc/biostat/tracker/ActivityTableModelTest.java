package edu.vanderbilt.mc.biostat.tracker;

import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityTableModelTest extends TestCase {

  public ActivityTableModelTest() {
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    Activity.setDatabase(getDatabase());
  }

  @Test
  public void addingActivities() {
    ActivityTableModel atm = new ActivityTableModel(Utils.today(), Utils.tomorrow());

    Project project = Project.create("foo");
    Date now = new Date();
    Date startedAt = new Date(now.getTime() - 60000);
    Date endedAt = new Date(now.getTime());
    Activity activity = Activity.create(project.id, "foo", startedAt, endedAt);

    atm.updateActivities();
    Assert.assertEquals(1, atm.getRowCount());
  }

  @Test
  public void findExistingActivities() {
    Project project = Project.create("foo");
    Date now = new Date();
    Date startedAt = new Date(now.getTime() - 60000);
    Date endedAt = new Date(now.getTime());
    Activity activity = Activity.create(project.id, "foo", startedAt, endedAt);

    ActivityTableModel atm = new ActivityTableModel(Utils.today(), Utils.tomorrow());

    Assert.assertEquals(1, atm.getRowCount());
  }

  @Test
  public void columnMethods() {
    ActivityTableModel atm = new ActivityTableModel(Utils.today(), Utils.tomorrow());
    Assert.assertEquals(7, atm.getColumnCount());
    Assert.assertEquals("Started", atm.getColumnName(0));
    Assert.assertEquals("", atm.getColumnName(1));
    Assert.assertEquals("Ended", atm.getColumnName(2));
    Assert.assertEquals("Activity", atm.getColumnName(3));
    Assert.assertEquals("Project", atm.getColumnName(4));
    Assert.assertEquals("Tags", atm.getColumnName(5));
    Assert.assertEquals("Time", atm.getColumnName(6));
  }

  @Test
  public void gettingValues() {
    Project project = Project.create("bar");
    Date now = new Date();
    Date startedAt = new Date(now.getTime() - 60000);
    Date endedAt = new Date(now.getTime());
    Activity activity = Activity.create(project.id, "foo", startedAt, endedAt);
    Tag tag_1 = Tag.create("one");
    activity.addTag(tag_1);
    Tag tag_2 = Tag.create("two");
    activity.addTag(tag_2);

    ActivityTableModel atm = new ActivityTableModel(Utils.today(), Utils.tomorrow());
    Assert.assertEquals(Utils.shortTime(startedAt), atm.getValueAt(0, 0));
    Assert.assertEquals("-", atm.getValueAt(0, 1));
    Assert.assertEquals(Utils.shortTime(endedAt), atm.getValueAt(0, 2));
    Assert.assertEquals("foo", atm.getValueAt(0, 3));
    Assert.assertEquals("bar", atm.getValueAt(0, 4));
    Assert.assertEquals("one, two", atm.getValueAt(0, 5));
    Assert.assertEquals("1min", atm.getValueAt(0, 6));
  }
}
