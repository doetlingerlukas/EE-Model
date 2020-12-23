package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionServerlessTest {

	@Test
	public void testGetSetResource() {
		String resString = "resource";
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Serverless, task);
		assertFalse(PropertyServiceFunctionServerless.isResourceSet(task));
		PropertyServiceFunctionServerless.setResource(task, resString);
		assertTrue(PropertyServiceFunctionServerless.isResourceSet(task));
		assertEquals(resString, PropertyServiceFunctionServerless.getResource(task));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkExceptionNoType() {
		Task task = new Task("task");
		PropertyServiceFunctionServerless.getResource(task);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void checkExceptionWrongType() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Local, task);
		PropertyServiceFunctionServerless.getResource(task);
	}
}
