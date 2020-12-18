package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNotFunction() {
		Task comm = new Communication("comm");
		PropertyServiceFunction.checkTask(comm);
	}

	@Test
	public void testGetSetResource() {
		String resString = "resource";
		Task task = new Task("task");
		assertFalse(PropertyServiceFunction.isResourceSet(task));
		PropertyServiceFunction.setResource(task, resString);
		assertTrue(PropertyServiceFunction.isResourceSet(task));
		assertEquals(resString, PropertyServiceFunction.getResource(task));
	}
}
