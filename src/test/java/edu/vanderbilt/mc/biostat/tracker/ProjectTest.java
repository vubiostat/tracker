package edu.vanderbilt.mc.biostat.tracker;

import java.util.List;
import org.junit.*;

public class ProjectTest extends TestCase {

  public ProjectTest() {
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    Project.setDatabase(getDatabase());
  }

  @Test
  public void createProject() {
    Project project = Project.create("foo");
    Assert.assertEquals(1, project.id);
    Assert.assertEquals("foo", project.name);
  }

  @Test
  public void findProjectById() {
    Project project_1 = Project.create("foo");
    Project project_2 = Project.findById(project_1.id);
    Assert.assertEquals(project_1, project_2);
  }

  @Test
  public void findAll() {
    Project project_1 = Project.create("foo");
    Project project_2 = Project.create("bar");

    List projects = Project.findAll();
    Assert.assertEquals(2, projects.size());
    Assert.assertEquals(project_1, projects.get(0));
    Assert.assertEquals(project_2, projects.get(1));
  }

  @Test
  public void findAllWithConditions() {
    Project project_1 = Project.create("foo_1");
    Project project_2 = Project.create("foo_2");
    Project project_3 = Project.create("bar");

    List projects = Project.findAll("name LIKE ?", "foo%");
    Assert.assertEquals(2, projects.size());
    Assert.assertEquals(project_1, projects.get(0));
    Assert.assertEquals(project_2, projects.get(1));
  }
}
