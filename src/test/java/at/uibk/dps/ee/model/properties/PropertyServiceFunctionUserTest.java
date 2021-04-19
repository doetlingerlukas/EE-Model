package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;
import org.junit.Test;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionUserTest {

  @Test(expected = IllegalArgumentException.class)
  public void testCheckTask() {
    Task task = new Task("task");
    PropertyServiceFunction.setUsageType(UsageType.DataFlow, task);
    PropertyServiceFunctionUser.checkTask(task);
  }

  @Test
  public void testCreateTask() {
    String id = "user task";
    String functionTypeString = "addition";
    Task result = PropertyServiceFunctionUser.createUserTask(id, functionTypeString);
    assertEquals(UsageType.User, PropertyServiceFunction.getUsageType(result));
    assertEquals(id, result.getId());
    assertEquals(functionTypeString, PropertyServiceFunctionUser.getTypeId(result));
  }
}
