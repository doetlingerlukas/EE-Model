package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import at.uibk.dps.ee.model.properties.PropertyServiceData.DataType;
import at.uibk.dps.ee.model.properties.PropertyServiceData.NodeType;
import at.uibk.dps.ee.model.properties.PropertyServiceData.Property;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;

public class PropertyServiceDataTest {

  @Test
  public void testCreateSeqNode() {
    String id = "id";
    Task result = PropertyServiceData.createSequentialityNode(id);
    assertEquals(id, result.getId());
    assertEquals(NodeType.Sequentiality, PropertyServiceData.getNodeType(result));
  }

  @Test
  public void testCreateConstantNode() {
    String id = "id";
    DataType type = DataType.String;
    JsonElement content = new JsonObject();
    Task result = PropertyServiceData.createConstantNode(id, type, content);
    assertEquals(id, result.getId());
    assertEquals(DataType.String, PropertyServiceData.getDataType(result));
    assertEquals(NodeType.Constant, PropertyServiceData.getNodeType(result));
    assertEquals(content, PropertyServiceData.getContent(result));
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkResetContentConstant() {
    Task task = new Communication("comm");
    PropertyServiceData.setContent(task, new JsonObject());
    PropertyServiceData.setNodeType(task, NodeType.Constant);
    PropertyServiceData.resetContent(task);
  }
  
  @Test
  public void checkResetContent() {
    Task task = new Communication("comm");
    PropertyServiceData.setContent(task, new JsonObject());
    assertTrue(PropertyServiceData.isDataAvailable(task));
    PropertyServiceData.resetContent(task);
    assertFalse(PropertyServiceData.isDataAvailable(task));
    assertNull(task.getAttribute(Property.Content.name()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetContentConstant() {
    Task task = new Communication("comm");
    PropertyServiceData.setNodeType(task, NodeType.Constant);
    PropertyServiceData.setContent(task, new JsonObject());
  }

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
  
  @Test
  public void testGetSetAvailabilityContentString() {
    Task input = new Communication("comm");
    assertFalse(PropertyServiceData.isDataAvailable(input));
    JsonElement json = new JsonPrimitive("123");
    PropertyServiceData.setContent(input, json);
    assertTrue(PropertyServiceData.isDataAvailable(input));
    assertEquals(json, PropertyServiceData.getContent(input));
  }
  
  @Test
  public void testGetSetAvailabilityContentNumber() {
    Task input = new Communication("comm");
    assertFalse(PropertyServiceData.isDataAvailable(input));
    JsonElement json = new JsonPrimitive(123);
    PropertyServiceData.setContent(input, json);
    assertTrue(PropertyServiceData.isDataAvailable(input));
    assertEquals(json, PropertyServiceData.getContent(input));
  }
  
  @Test
  public void testGetSetAvailabilityContentArray() {
    Task input = new Communication("comm");
    assertFalse(PropertyServiceData.isDataAvailable(input));
    JsonArray json = new JsonArray();
    json.add(123);
    json.add("123");
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
