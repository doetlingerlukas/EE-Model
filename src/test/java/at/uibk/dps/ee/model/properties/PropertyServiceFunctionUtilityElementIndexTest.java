package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtilityCollections.CollectionOperation;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionUtilityElementIndexTest {

	@Test
	public void testCreateTask() {
		String dataId = "data";
		String collId = "subcollection";

		CollectionOperation operation = CollectionOperation.ElementIndex;

		String expectedId = dataId + ConstantsEEModel.DependencyAffix + operation + ConstantsEEModel.DependencyAffix
				+ collId;

		Task result = PropertyServiceFunctionUtilityCollections.createCollectionOperation(dataId, collId, operation);
		assertEquals(UtilityType.CollectionOperation, PropertyServiceFunctionUtility.getUtilityType(result));
		assertEquals(collId, PropertyServiceFunctionUtilityCollections.getSubCollectionsString(result));
		assertEquals(expectedId, result.getId());
		assertEquals(operation, PropertyServiceFunctionUtilityCollections.getCollectionOperation(result));
	}

	@Test
	public void testGetSetSubCollections() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.CollectionOperation);
		String string = "bla";
		PropertyServiceFunctionUtilityCollections.setSubCollectionsString(task, string);
		assertEquals(string, PropertyServiceFunctionUtilityCollections.getSubCollectionsString(task));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTask() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);
		PropertyServiceFunctionUtilityCollections.checkTask(task);
	}

	@Test
	public void testCheckSubcollectionString() {

	}
}
