package edu.vanderbilt.mc.biostat.tracker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

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
  public void startActivityWithTags() {
    Activity activity = app.startActivity("foo@bar", "one, two, three");
    Assert.assertNotNull(activity);
    Assert.assertEquals("foo", activity.name);

    Project project = Project.findById(activity.projectId);
    Assert.assertNotNull(project);
    Assert.assertEquals("bar", project.name);

    List<Tag> tags = activity.getTags();
    Assert.assertEquals(3, tags.size());

    Set<String> expectedTagNames = new HashSet<String>(3);
    expectedTagNames.add("one");
    expectedTagNames.add("two");
    expectedTagNames.add("three");
    Set<String> actualTagNames = new HashSet<String>(3);
    actualTagNames.add(tags.get(0).name);
    actualTagNames.add(tags.get(1).name);
    actualTagNames.add(tags.get(2).name);
    Assert.assertEquals(expectedTagNames, actualTagNames);

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
