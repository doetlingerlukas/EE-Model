package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import at.uibk.dps.ee.core.enactable.Enactable.State;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionTest {

	@Test(expected = IllegalArgumentException.class)
	public void testNotFunction() {
		Task comm = new Communication("comm");
		PropertyServiceFunction.checkTask(comm);
	}

	@Test
	public void testGetSetFunctionType() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Local, task);
		assertEquals(FunctionType.Local, PropertyServiceFunction.getType(task));
	}

	@Test
	public void testGetSetEnactableState() {
		Task task = new Task("task");
		assertEquals(State.WAITING, PropertyServiceFunction.getEnactableState(task));
		PropertyServiceFunction.setEnactableState(task, State.RUNNING);
		assertEquals(State.RUNNING, PropertyServiceFunction.getEnactableState(task));
	}
}
