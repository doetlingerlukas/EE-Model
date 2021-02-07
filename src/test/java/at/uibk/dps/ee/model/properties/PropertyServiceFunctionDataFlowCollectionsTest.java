package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlow.DataFlowType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlowCollections.OperationType;
import net.sf.opendse.model.Communication;
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

	@Test
	public void testIsAggregationNode() {
		Task aggregationTask = PropertyServiceFunctionDataFlowCollections.createCollectionDataFlowTask("aggr",
				OperationType.Aggregation, "scope");
		assertTrue(PropertyServiceFunctionDataFlowCollections.isAggregationNode(aggregationTask));

		Communication noAggTask1 = new Communication("comm");
		Task noAggTask2 = new Task("task");
		PropertyServiceFunction.setUsageType(UsageType.User, noAggTask2);
		Task noAggTask3 = PropertyServiceFunctionDataFlow.createDataFlowFunction("bla", DataFlowType.EarliestInput);
		Task noAggTask4 = PropertyServiceFunctionDataFlowCollections.createCollectionDataFlowTask("bla",
				OperationType.Distribution, "scope");
		
		assertFalse(PropertyServiceFunctionDataFlowCollections.isAggregationNode(noAggTask1));
		assertFalse(PropertyServiceFunctionDataFlowCollections.isAggregationNode(noAggTask2));
		assertFalse(PropertyServiceFunctionDataFlowCollections.isAggregationNode(noAggTask3));
		assertFalse(PropertyServiceFunctionDataFlowCollections.isAggregationNode(noAggTask4));
	}
	
	@Test
	public void testIsDistributionNode() {
		Task aggregationTask = PropertyServiceFunctionDataFlowCollections.createCollectionDataFlowTask("aggr",
				OperationType.Distribution, "scope");
		assertTrue(PropertyServiceFunctionDataFlowCollections.isDistributionNode(aggregationTask));

		Communication noAggTask1 = new Communication("comm");
		Task noAggTask2 = new Task("task");
		PropertyServiceFunction.setUsageType(UsageType.User, noAggTask2);
		Task noAggTask3 = PropertyServiceFunctionDataFlow.createDataFlowFunction("bla", DataFlowType.EarliestInput);
		Task noAggTask4 = PropertyServiceFunctionDataFlowCollections.createCollectionDataFlowTask("bla",
				OperationType.Aggregation, "scope");
		
		assertFalse(PropertyServiceFunctionDataFlowCollections.isDistributionNode(noAggTask1));
		assertFalse(PropertyServiceFunctionDataFlowCollections.isDistributionNode(noAggTask2));
		assertFalse(PropertyServiceFunctionDataFlowCollections.isDistributionNode(noAggTask3));
		assertFalse(PropertyServiceFunctionDataFlowCollections.isDistributionNode(noAggTask4));
	}
}
