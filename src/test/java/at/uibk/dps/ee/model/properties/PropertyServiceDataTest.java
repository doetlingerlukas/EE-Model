package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonObject;

import at.uibk.dps.ee.model.properties.PropertyServiceData.DataType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;

public class PropertyServiceDataTest {

	@Test(expected = IllegalArgumentException.class)
	public void testGetContentException() {
		Task task = new Communication("comm");
		PropertyServiceData.getContent(task);
	}

	@Test
	public void testGetSetAvailabilityContent() {
		Task input = new Communication("comm");
		assertFalse(PropertyServiceData.isDataAvailable(input));
		JsonObject json = new JsonObject();
		PropertyServiceData.setContent(input, json);
		assertTrue(PropertyServiceData.isDataAvailable(input));
		assertEquals(json, PropertyServiceData.getContent(input));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTaskFail() {
		Task task = new Task("task");
		PropertyServiceData.checkTask(task);
	}

	@Test
	public void testCheckTaskPass() {
		Task task = new Communication("comm");
		try {
			PropertyServiceData.checkTask(task);
		} catch (IllegalArgumentException exc) {
			fail();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDataTypeUnset() {
		Task task = new Communication("comm");
		PropertyServiceData.getDataType(task);
	}

	@Test
	public void testGetSetDataType() {
		Task task = new Communication("comm");
		PropertyServiceData.setDataType(task, DataType.Number);
		assertEquals(DataType.Number, PropertyServiceData.getDataType(task));
	}

	@Test
	public void testJsonKey() {
		Task task = new Communication("comm");
		PropertyServiceData.makeRoot(task);
		PropertyServiceData.setJsonKey(task, "key");
		assertEquals("key", PropertyServiceData.getJsonKey(task));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testJsonKeyNonRootLeaf() {
		Task task = new Communication("comm");
		PropertyServiceData.getJsonKey(task);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testJsonKeySetNonRootLeaf() {
		Task task = new Communication("comm");
		PropertyServiceData.setJsonKey(task, "key");
	}

	@Test
	public void testGetSetLeaf() {
		Task task = new Communication("comm");
		assertFalse(PropertyServiceData.isLeaf(task));
		PropertyServiceData.makeLeaf(task);
		assertTrue(PropertyServiceData.isLeaf(task));
	}

	@Test
	public void testGetSetRoot() {
		Task task = new Communication("comm");
		assertFalse(PropertyServiceData.isRoot(task));
		PropertyServiceData.makeRoot(task);
		assertTrue(PropertyServiceData.isRoot(task));
	}
}
