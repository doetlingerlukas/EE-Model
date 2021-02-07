package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlow.DataFlowType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionDataFlowTest {

	@Test
	public void testCreateSyntaxFunction() {
		String taskId = "id";
		DataFlowType syntaxType = DataFlowType.EarliestInput;
		Task result = PropertyServiceFunctionDataFlow.createDataFlowFunction(taskId, syntaxType);
		assertEquals(taskId, result.getId());
		assertEquals(UsageType.DataFlow, PropertyServiceFunction.getUsageType(result));
		assertEquals(DataFlowType.EarliestInput, PropertyServiceFunctionDataFlow.getDataFlowType(result));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTask() {
		Task task = new Task("task");
		PropertyServiceFunction.setUsageType(UsageType.User, task);
		PropertyServiceFunctionDataFlow.checkTask(task);
	}

	@Test
	public void checkSetGetSyntaxType() {
		Task task = new Task("task");
		PropertyServiceFunction.setUsageType(UsageType.DataFlow, task);
		PropertyServiceFunctionDataFlow.setDataFlowType(task, DataFlowType.EarliestInput);
		assertEquals(DataFlowType.EarliestInput, PropertyServiceFunctionDataFlow.getDataFlowType(task));
	}
}
