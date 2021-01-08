package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlow.DataFlowType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionDataFlowTest {

	@Test
	public void testCreateSyntaxFunction() {
		String taskId = "id";
		DataFlowType syntaxType = DataFlowType.EarliestInput;
		Task result = PropertyServiceFunctionDataFlow.createDataFlowFunction(taskId, syntaxType);
		assertEquals(taskId, result.getId());
		assertEquals(FunctionType.DataFlow, PropertyServiceFunction.getType(result));
		assertEquals(DataFlowType.EarliestInput, PropertyServiceFunctionDataFlow.getDataFlowType(result));
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
		PropertyServiceFunctionDataFlow.setDataFlowType(task, DataFlowType.EarliestInput);
		assertEquals(DataFlowType.EarliestInput, PropertyServiceFunctionDataFlow.getDataFlowType(task));
	}
}
