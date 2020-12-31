package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionUtilityTest {

	@Test(expected = IllegalArgumentException.class)
	public void testWrongTask() {
		Task t = new Task("t");
		PropertyServiceFunction.setType(FunctionType.Local, t);
		PropertyServiceFunctionUtility.setUtilityType(t, UtilityType.Condition);
	}

	@Test
	public void testGetSetType() {
		Task condi = new Task("condi");
		PropertyServiceFunction.setType(FunctionType.Utility, condi);
		PropertyServiceFunctionUtility.setUtilityType(condi, UtilityType.Condition);
		assertEquals(UtilityType.Condition, PropertyServiceFunctionUtility.getUtilityType(condi));
	}
}
