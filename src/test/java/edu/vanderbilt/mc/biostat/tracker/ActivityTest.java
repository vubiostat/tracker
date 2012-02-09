package edu.vanderbilt.mc.biostat.tracker;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityTest extends TestCase {

  private Project project;

  public ActivityTest() {
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    Activity.setDatabase(getDatabase());
    project = Project.create("foo");
  }

  @Test
  public void createActivity() {
    Date now = new Date();
    Date startedAt = new Date(now.getTime() - 60);
    Date endedAt = new Date(now.getTime() + 60);
    Activity activity = Activity.create(project.id, "foo", startedAt, endedAt);

    Assert.assertEquals(1, activity.id);
    Assert.assertEquals(project.id, activity.projectId);
    Assert.assertEquals("foo", activity.name);
    Assert.assertEquals(startedAt, activity.startedAt);
    Assert.assertEquals(endedAt, activity.endedAt);
  }

  @Test
  public void findActivityById() {
    Date now = new Date();
    Date startedAt = new Date(now.getTime() - 60);
    Date endedAt = new Date(now.getTime() + 60);
    Activity activity_1 = Activity.create(project.id, "foo", startedAt, endedAt);

    Activity activity_2 = Activity.findById(activity_1.id);
    Assert.assertEquals(activity_1, activity_2);
  }

  @Test
  public void findAll() {
    Date now = new Date();

    Date startedAt_1 = new Date(now.getTime() - 60000);
    Date endedAt_1 = new Date(now.getTime() + 60000);
    Activity activity_1 = Activity.create(project.id, "foo", startedAt_1, endedAt_1);

    Date startedAt_2 = new Date(now.getTime() - 600000);
    Date endedAt_2 = new Date(now.getTime() - 540000);
    Activity activity_2 = Activity.create(project.id, "bar", startedAt_2, endedAt_2);

    List activities = Activity.findAll();
    Assert.assertEquals(2, activities.size());
    Assert.assertEquals(activity_1, activities.get(0));
    Assert.assertEquals(activity_2, activities.get(1));
  }

  @Test
  public void findAllWithConditions() {
    Date now = new Date();

    Date startedAt_1 = new Date(now.getTime() - 60000);
    Date endedAt_1 = new Date(now.getTime() + 60000);
    Activity activity_1 = Activity.create(project.id, "foo", startedAt_1, endedAt_1);

    Date startedAt_2 = new Date(now.getTime() - 600);
    Date endedAt_2 = new Date(now.getTime() - 540);
    Activity activity_2 = Activity.create(project.id, "bar", startedAt_2, endedAt_2);

    List activities = Activity.findAll("ended_at > ?", now);
    Assert.assertEquals(1, activities.size());
    Assert.assertEquals(activity_1, activities.get(0));
  }

  @Test
  public void getAttributes() {
    Date now = new Date();
    Date startedAt = new Date(now.getTime() - 60000);
    Date endedAt = new Date(now.getTime() + 60000);
    Activity activity = Activity.create(project.id, "foo", startedAt, endedAt);

    HashMap expected = new HashMap<String, Object>();
    expected.put("ID", activity.id);
    expected.put("PROJECT_ID", activity.projectId);
    expected.put("NAME", activity.name);
    expected.put("STARTED_AT", activity.startedAt);
    expected.put("ENDED_AT", activity.endedAt);
    Assert.assertEquals(expected, activity.getAttributes());
  }

  @Test
  public void update() {
    Date now = new Date();
    Date startedAt = new Date(now.getTime() - 60000);
    Date endedAt = new Date(now.getTime() + 60000);
    Activity activity = Activity.create(project.id, "foo", startedAt, endedAt);
    
    activity.name = "bar";
    Assert.assertTrue(activity.update());
    Assert.assertEquals(activity, Activity.findById(activity.id));
  }
}