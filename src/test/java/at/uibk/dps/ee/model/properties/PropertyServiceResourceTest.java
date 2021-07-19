package at.uibk.dps.ee.model.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.properties.PropertyServiceResource.ResourceState;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

public class PropertyServiceResourceTest {

  @Test
  public void testCreateResource() {
    String id = "resId";
    Resource result = PropertyServiceResource.createResource(id);
    assertEquals(id, result.getId());
  }

  @Test
  public void testState() {
    String id = "resId";
    Resource result = PropertyServiceResource.createResource(id);
    assertEquals(ResourceState.Idle, PropertyServiceResource.getState(result));
    PropertyServiceResource.setState(result, ResourceState.Used);
    assertEquals(ResourceState.Used, PropertyServiceResource.getState(result));
    PropertyServiceResource.setState(result, ResourceState.Idle);
    assertEquals(ResourceState.Idle, PropertyServiceResource.getState(result));
  }

  @Test
  public void testUsedBy() {
    String id = "resId";
    Resource result = PropertyServiceResource.createResource(id);
    assertTrue(PropertyServiceResource.getUsingTaskIds(result).isEmpty());
    Task task1 = new Task("task1");
    Task task2 = new Task("task2");
    PropertyServiceResource.addUsingTask(task1, result);
    PropertyServiceResource.addUsingTask(task2, result);
    assertTrue(PropertyServiceResource.getUsingTaskIds(result).contains(task1.getId()));
    assertTrue(PropertyServiceResource.getUsingTaskIds(result).contains(task2.getId()));
    PropertyServiceResource.removeUsingTask(task1, result);
    assertFalse(PropertyServiceResource.getUsingTaskIds(result).contains(task1.getId()));
    assertTrue(PropertyServiceResource.getUsingTaskIds(result).contains(task2.getId()));
  }

  @Test
  public void testUsedByException() {
    assertThrows(IllegalStateException.class, () -> {
      String id = "resId";
      Resource result = PropertyServiceResource.createResource(id);
      assertTrue(PropertyServiceResource.getUsingTaskIds(result).isEmpty());
      Task task1 = new Task("task1");
      PropertyServiceResource.removeUsingTask(task1, result);
    });
  }
}
