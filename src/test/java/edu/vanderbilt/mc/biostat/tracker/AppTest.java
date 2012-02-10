package edu.vanderbilt.mc.biostat.tracker;

import java.util.List;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class AppTest extends TestCase {

  private App app;

  public AppTest() {
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    Model.setDatabase(getDatabase());
    app = new App();
  }

  @Test
  public void startActivityWithNoTags() {
    Activity activity = app.startActivity("foo@bar", null);
    Assert.assertNotNull(activity);
    Assert.assertEquals("foo", activity.name);

    Project project = Project.findById(activity.projectId);
    Assert.assertNotNull(project);
    Assert.assertEquals("bar", project.name);

    Activity foundActivity = Activity.findById(activity.id);
    Assert.assertEquals(activity, foundActivity);
  }
  
  @Test
  public void stopActivity() {
    Activity activity = app.startActivity("foo@bar", null);
    app.stopActivity(activity);
    Assert.assertNotNull(activity.endedAt);
  }
}
