package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionUtilityElementIndexTest {

	@Test
	public void testCreateTask() {
		String dataId = "data";
		String collId = "subcollection";

		String expectedId = dataId + ConstantsEEModel.DependencyAffix + ConstantsEEModel.EIdxName
				+ ConstantsEEModel.DependencyAffix + collId;

		Task result = PropertyServiceFunctionUtilityElementIndex.createElementIndexTask(dataId, collId);
		assertEquals(UtilityType.ElementIndex, PropertyServiceFunctionUtility.getUtilityType(result));
		assertEquals(collId, PropertyServiceFunctionUtilityElementIndex.getSubCollectionsString(result));
		assertEquals(expectedId, result.getId());
	}

	@Test
	public void testGetSetSubCollections() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.ElementIndex);
		String string = "bla";
		PropertyServiceFunctionUtilityElementIndex.setSubCollectionsString(task, string);
		assertEquals(string, PropertyServiceFunctionUtilityElementIndex.getSubCollectionsString(task));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTask() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);
		PropertyServiceFunctionUtilityElementIndex.checkTask(task);
	}

	@Test
	public void testCheckSubcollectionString() {

	}
}
