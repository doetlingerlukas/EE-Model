package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import at.uibk.dps.ee.model.objects.Condition;
import at.uibk.dps.ee.model.objects.Condition.Operator;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.Property;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtilityCondition.Summary;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionUtilityConditionTest {

	@Test(expected = IllegalArgumentException.class)
	public void testExc() {
		Task task = new Task("t");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		task.setAttribute(Property.UtilityType.name(), "unknown");
		PropertyServiceFunctionUtilityCondition.getConditions(task);
	}

	@Test
	public void testGetSetSummary() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);
		PropertyServiceFunctionUtilityCondition.setSummary(task, Summary.AND);
		assertEquals(Summary.AND, PropertyServiceFunctionUtilityCondition.getSummary(task));
	}
	
	@Test
	public void testGetSetConditions() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
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
