/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.vanderbilt.mc.biostat.tracker;

import java.util.HashMap;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author stephej1
 */
public class TagTest extends TestCase {
  
  public TagTest() {
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    Tag.setDatabase(getDatabase());
  }

  @Test
  public void createTag() {
    Tag tag = Tag.create("foo");
    Assert.assertEquals(1, tag.id);
    Assert.assertEquals("foo", tag.name);
  }

  @Test
  public void findTagById() {
    Tag tag_1 = Tag.create("foo");
    Tag tag_2 = Tag.findById(tag_1.id);
    Assert.assertEquals(tag_1, tag_2);
  }

  @Test
  public void findAll() {
    Tag tag_1 = Tag.create("foo");
    Tag tag_2 = Tag.create("bar");

    List tags = Tag.findAll();
    Assert.assertEquals(2, tags.size());
    Assert.assertEquals(tag_1, tags.get(0));
    Assert.assertEquals(tag_2, tags.get(1));
  }

  @Test
  public void findAllWithConditions() {
    Tag tag_1 = Tag.create("foo_1");
    Tag tag_2 = Tag.create("foo_2");
    Tag tag_3 = Tag.create("bar");

    List tags = Tag.findAll("name LIKE ?", "foo%");
    Assert.assertEquals(2, tags.size());
    Assert.assertEquals(tag_1, tags.get(0));
    Assert.assertEquals(tag_2, tags.get(1));
  }

  @Test
  public void findByName() {
    Tag tag_1 = Tag.create("foo_1");
    Tag tag_2 = Tag.create("foo_2");
    Tag tag_3 = Tag.create("bar");

    Assert.assertEquals(tag_1, Tag.findByName("foo_1"));
    Assert.assertEquals(null, Tag.findByName("baz"));
  }

  @Test
  public void getAttributes() {
    Tag tag = Tag.create("foo");

    HashMap expected = new HashMap<String, Object>();
    expected.put("ID", tag.id);
    expected.put("NAME", tag.name);
    Assert.assertEquals(expected, tag.getAttributes());
  }

  @Test
  public void update() {
    Tag tag = Tag.create("foo");
    tag.name = "bar";
    
    Assert.assertTrue(tag.update());
    Assert.assertEquals(tag, Tag.findById(tag.id));
  }
}
