package at.uibk.dps.ee.model.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionUserTest {

  @Test
  void testWhileReplica() {
    String reference = "reference";
    Task task = PropertyServiceFunctionUser.createUserTask("task", "function");
    assertFalse(PropertyServiceFunctionUser.isWhileReplica(task));
    assertThrows(IllegalArgumentException.class, ()->{
      PropertyServiceFunctionUser.getWhileRef(task);
    });
    PropertyServiceFunctionUser.setWhileRef(task, reference);
    assertTrue(PropertyServiceFunctionUser.isWhileReplica(task));
    assertEquals(reference, PropertyServiceFunctionUser.getWhileRef(task));
  }

  @Test
  public void testCheckTask() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Task("task");
      PropertyServiceFunction.setUsageType(UsageType.DataFlow, task);
      PropertyServiceFunctionUser.checkTask(task);
    });
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
