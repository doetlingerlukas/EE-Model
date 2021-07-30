package at.uibk.dps.ee.model.properties;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import net.sf.opendse.model.Task;

class PropertyServiceFunctionUtilityWhileTest {

  @Test
  void testCheckTask() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = PropertyServiceFunctionUtilityCondition.createConditionEvaluation("bla",
          new ArrayList<>());
      assertFalse(PropertyServiceFunctionUtilityWhile.isWhileEndTask(task));
      PropertyServiceFunctionUtilityWhile.checkTask(task);
    });
  }

  @Test
  void test() {
    String startId = "start";
    Task whileStart = new Task(startId);
    Task whileCounter = new Task("whileCounter");
    Task result = PropertyServiceFunctionUtilityWhile.createWhileEndTask(whileStart, whileCounter);
    assertEquals(startId, PropertyServiceFunctionUtilityWhile.getWhileStart(result));
    assertTrue(PropertyServiceFunctionUtilityWhile.isWhileEndTask(result));
  }

}
