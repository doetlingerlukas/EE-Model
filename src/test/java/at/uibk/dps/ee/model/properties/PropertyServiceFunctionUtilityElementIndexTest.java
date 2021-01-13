package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.objects.SubCollections;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;

import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

public class PropertyServiceFunctionUtilityElementIndexTest {

	@Test
	public void testCreateTask() {
		SubCollections mock = mock(SubCollections.class);
		String dataId = "data";
		String collId = "subcollection";
		when(mock.toString()).thenReturn(collId);

		String expectedId = dataId + ConstantsEEModel.DependencyAffix + ConstantsEEModel.EIdxName
				+ ConstantsEEModel.DependencyAffix + collId;

		Task result = PropertyServiceFunctionUtilityElementIndex.createElementIndexTask(dataId, mock);
		assertEquals(UtilityType.ElementIndex, PropertyServiceFunctionUtility.getUtilityType(result));
		assertEquals(mock, PropertyServiceFunctionUtilityElementIndex.getSubCollections(result));
		assertEquals(expectedId, result.getId());
	}

	@Test
	public void testGetSetSubCollections() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.ElementIndex);
		SubCollections mock = mock(SubCollections.class);
		PropertyServiceFunctionUtilityElementIndex.setSubCollections(task, mock);
		assertEquals(mock, PropertyServiceFunctionUtilityElementIndex.getSubCollections(task));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTask() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Utility, task);
		PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);
		PropertyServiceFunctionUtilityElementIndex.checkTask(task);
	}
}
