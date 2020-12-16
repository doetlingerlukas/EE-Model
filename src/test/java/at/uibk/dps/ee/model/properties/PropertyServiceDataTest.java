package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.JsonObject;

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
}
