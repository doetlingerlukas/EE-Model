package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.uibk.dps.ee.core.enactable.Enactable;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;

import static org.mockito.Mockito.mock;

public class PropertyServiceFunctionTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNotFunction() {
		Task comm = new Communication("comm");
		PropertyServiceFunction.checkTask(comm);
	}

	@Test
	public void testGetSetFunctionType() {
		Task task = new Task("task");
		PropertyServiceFunction.setUsageType(UsageType.User, task);
		assertEquals(UsageType.User, PropertyServiceFunction.getUsageType(task));
	}

	@Test
	public void testGetSetEnactable() {
		Task task = new Task("task");
		Enactable mockEnactable = mock(Enactable.class);
		PropertyServiceFunction.setEnactable(task, mockEnactable);
		assertEquals(mockEnactable, PropertyServiceFunction.getEnactable(task));
	}
}
