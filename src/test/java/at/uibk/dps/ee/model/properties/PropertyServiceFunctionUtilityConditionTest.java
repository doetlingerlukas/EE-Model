package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import at.uibk.dps.ee.model.objects.Condition;
import at.uibk.dps.ee.model.objects.Condition.Operator;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.Property;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtilityCondition.Summary;
import net.sf.opendse.model.Task;


public class PropertyServiceFunctionUtilityConditionTest {

  @Test
  public void testCreateCondition() {
    Condition cond1 = new Condition("bla", "blabla", Operator.AND, true);
    Condition cond2 = new Condition("bla", "blabla", Operator.OR, false);
    Set<Condition> conditions = new HashSet<>();
    conditions.add(cond1);
    conditions.add(cond2);
    Summary summary = Summary.AND;
    String id = "id";
    Task result =
        PropertyServiceFunctionUtilityCondition.createConditionEvaluation(id, conditions, summary);
    assertEquals(id, result.getId());
    assertEquals(conditions, PropertyServiceFunctionUtilityCondition.getConditions(result));
    assertEquals(summary, PropertyServiceFunctionUtilityCondition.getSummary(result));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExc() {
    Task task = new Task("t");
    PropertyServiceFunction.setUsageType(UsageType.Utility, task);
    task.setAttribute(Property.UtilityType.name(), "unknown");
    PropertyServiceFunctionUtilityCondition.getConditions(task);
  }

  @Test
  public void testGetSetSummary() {
    Task task = new Task("task");
    PropertyServiceFunction.setUsageType(UsageType.Utility, task);
    PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);
    PropertyServiceFunctionUtilityCondition.setSummary(task, Summary.AND);
    assertEquals(Summary.AND, PropertyServiceFunctionUtilityCondition.getSummary(task));
  }

  @Test
  public void testGetSetConditions() {
    Task task = new Task("task");
    PropertyServiceFunction.setUsageType(UsageType.Utility, task);
    PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);

    Condition cond1 = new Condition("d1", "d2", Operator.EQUAL, false);
    Condition cond2 = new Condition("d3", "d4", Operator.LESS, true);
    Set<Condition> conds = new HashSet<>();
    conds.add(cond1);
    conds.add(cond2);
    PropertyServiceFunctionUtilityCondition.setConditions(task, conds);
    Set<Condition> result = PropertyServiceFunctionUtilityCondition.getConditions(task);
    assertTrue(result.contains(cond1));
    assertTrue(result.contains(cond2));
  }
}
