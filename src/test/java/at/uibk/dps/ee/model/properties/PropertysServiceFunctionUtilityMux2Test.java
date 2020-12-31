package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;

public class PropertysServiceFunctionUtilityMux2Test {

	@Test(expected = IllegalArgumentException.class)
	public void testException() {
		Task task = new Task("t");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);
		PropertyServiceFunctionUtilityMux2.getControlId(task);
	}
	
	@Test
	public void testGetSet() {
		Task task = new Task("t");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.MultiPlex2);
		
		String firstId = "first";
		String secondId = "second";
		String controlId = "control";
		
		PropertyServiceFunctionUtilityMux2.setFirstInputId(task, firstId);
		PropertyServiceFunctionUtilityMux2.setSecondInputId(task, secondId);
		PropertyServiceFunctionUtilityMux2.setControl(task, controlId);
		
		assertEquals(firstId, PropertyServiceFunctionUtilityMux2.getFirstInputId(task));
		assertEquals(secondId, PropertyServiceFunctionUtilityMux2.getSecondInput(task));
		assertEquals(controlId, PropertyServiceFunctionUtilityMux2.getControlId(task));
	}
}
