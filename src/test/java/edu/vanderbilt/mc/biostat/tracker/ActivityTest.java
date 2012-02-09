package edu.vanderbilt.mc.biostat.tracker;

import java.util.Date;
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

//  @Test
//  public void findAll() {
//    Activity Activity_1 = Activity.create("foo");
//    Activity Activity_2 = Activity.create("bar");
//
//    List Activitys = Activity.findAll();
//    Assert.assertEquals(2, Activitys.size());
//    Assert.assertEquals(Activity_1, Activitys.get(0));
//    Assert.assertEquals(Activity_2, Activitys.get(1));
//  }
}
