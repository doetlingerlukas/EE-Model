package at.uibk.dps.ee.model.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.objects.Condition;
import at.uibk.dps.ee.model.objects.Condition.CombinedWith;
import at.uibk.dps.ee.model.objects.Condition.Operator;
import at.uibk.dps.ee.model.properties.PropertyServiceData.DataType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.Property;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;


public class PropertyServiceFunctionUtilityConditionTest {

  @Test
  public void testCreateCondition() {
    Condition cond1 =
        new Condition("bla", "blabla", Operator.LESS, true, DataType.Number, CombinedWith.And);
    Condition cond2 = new Condition("bla", "blabla", Operator.LESS_EQUAL, false, DataType.Number,
        CombinedWith.Or);
    List<Condition> conditions = new ArrayList<>();
    conditions.add(cond1);
    conditions.add(cond2);
    String id = "id";
    Task result = PropertyServiceFunctionUtilityCondition.createConditionEvaluation(id, conditions);
    assertEquals(id, result.getId());
    assertEquals(conditions, PropertyServiceFunctionUtilityCondition.getConditions(result));
  }

  @Test
  public void testExc() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Task("t");
      PropertyServiceFunction.setUsageType(UsageType.Utility, task);
      task.setAttribute(Property.UtilityType.name(), "unknown");
      PropertyServiceFunctionUtilityCondition.getConditions(task);
    });
  }

  @Test
  public void testGetSetConditions() {
    Task task = new Task("task");
    PropertyServiceFunction.setUsageType(UsageType.Utility, task);
    PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);

    Condition cond1 =
        new Condition("d1", "d2", Operator.EQUAL, false, DataType.Object, CombinedWith.And);
    Condition cond2 =
        new Condition("d3", "d4", Operator.LESS, true, DataType.Number, CombinedWith.Or);
    List<Condition> conds = new ArrayList<>();
    conds.add(cond1);
    conds.add(cond2);
    PropertyServiceFunctionUtilityCondition.setConditions(task, conds);
    List<Condition> result = PropertyServiceFunctionUtilityCondition.getConditions(task);
    assertTrue(result.contains(cond1));
    assertTrue(result.contains(cond2));
  }
}
