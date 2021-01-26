package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlow.DataFlowType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlowCollections.OperationType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionDataFlowCollectionsTest {

	@Test
	public void test() {
		String scope = "here";
		Task task = PropertyServiceFunctionDataFlowCollections.createCollectionDataFlowTask("t",
				OperationType.Distribution, scope);
		assertEquals(scope, PropertyServiceFunctionDataFlowCollections.getScope(task));
		assertEquals(OperationType.Distribution, PropertyServiceFunctionDataFlowCollections.getOperationType(task));
		PropertyServiceFunctionDataFlowCollections.setIterationNumber(task, 3);
		assertEquals(3, PropertyServiceFunctionDataFlowCollections.getIterationNumber(task));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExc3() {
		String scope = "here";
		Task task = PropertyServiceFunctionDataFlowCollections.createCollectionDataFlowTask("t",
				OperationType.Aggregation, scope);
		PropertyServiceFunctionDataFlowCollections.getIterationNumber(task);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExc2() {
		String scope = "here";
		Task task = PropertyServiceFunctionDataFlowCollections.createCollectionDataFlowTask("t",
				OperationType.Aggregation, scope);
		PropertyServiceFunctionDataFlowCollections.setIterationNumber(task, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExc() {
		Task task = PropertyServiceFunctionDataFlow.createDataFlowFunction("task", DataFlowType.EarliestInput);
		PropertyServiceFunctionDataFlowCollections.getScope(task);
	}
}
