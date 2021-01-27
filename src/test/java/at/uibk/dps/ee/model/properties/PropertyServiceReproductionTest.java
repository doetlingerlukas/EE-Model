package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.opendse.model.Task;

public class PropertyServiceReproductionTest {

	@Test
	public void testIsReproduced() {
		Task task = new Task("task");
		assertFalse(PropertyServiceReproduction.isReproduced(task));
		PropertyServiceReproduction.makeReproduced(task);
		assertTrue(PropertyServiceReproduction.isReproduced(task));
	}
	
	@Test
	public void testGetSetReproductionScope() {
		Task task = new Task("task");
		String scope = "scope";
		PropertyServiceReproduction.makeReproduced(task);
		PropertyServiceReproduction.setReproductionScope(task, scope);
		assertEquals(scope, PropertyServiceReproduction.getReproductionScope(task));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetSetReproductionScopeExc1() {
		Task task = new Task("task");
		PropertyServiceReproduction.getReproductionScope(task);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetSetReproductionScopeExc2() {
		Task task = new Task("task");
		String scope = "scope";
		PropertyServiceReproduction.setReproductionScope(task, scope);
	}

}
