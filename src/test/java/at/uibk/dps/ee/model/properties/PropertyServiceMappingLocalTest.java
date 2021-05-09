package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;
import org.junit.Test;
import at.uibk.dps.ee.model.properties.PropertyServiceMapping.EnactmentMode;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

public class PropertyServiceMappingLocalTest {

  @Test
  public void test() {
    Task task = new Task("t");
    Resource res = new Resource("res");
    String imageName = "myImage";
    Mapping<Task, Resource> result =
        PropertyServiceMappingLocal.createMappingLocal(task, res, imageName);

    assertEquals(EnactmentMode.Local, PropertyServiceMapping.getEnactmentMode(result));
    assertEquals(imageName, PropertyServiceMappingLocal.getImageName(result));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testException() {
    Task task = new Task("t");
    Resource res = new Resource("res");
    Mapping<Task, Resource> result =
        PropertyServiceMapping.createMapping(task, res, EnactmentMode.Serverless, "aws");
    PropertyServiceMappingLocal.getImageName(result);
  }
}
