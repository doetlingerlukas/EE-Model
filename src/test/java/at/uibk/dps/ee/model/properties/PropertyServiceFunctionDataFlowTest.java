package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlow.SyntaxType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionDataFlowTest {

	@Test
	public void testCreateSyntaxFunction() {
		String taskId = "id";
		SyntaxType syntaxType = SyntaxType.EarliestInput;
		Task result = PropertyServiceFunctionDataFlow.createSyntaxFunction(taskId, syntaxType);
		assertEquals(taskId, result.getId());
		assertEquals(FunctionType.DataFlow, PropertyServiceFunction.getType(result));
		assertEquals(SyntaxType.EarliestInput, PropertyServiceFunctionDataFlow.getSyntaxType(result));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTask() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Local, task);
		PropertyServiceFunctionDataFlow.checkTask(task);
	}

	@Test
	public void checkSetGetSyntaxType() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.DataFlow, task);
		PropertyServiceFunctionDataFlow.setSyntaxType(task, SyntaxType.EarliestInput);
		assertEquals(SyntaxType.EarliestInput, PropertyServiceFunctionDataFlow.getSyntaxType(task));
	}
}
