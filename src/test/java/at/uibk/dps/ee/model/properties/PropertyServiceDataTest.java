package at.uibk.dps.ee.model.properties;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
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
  void testWhileEndAnnotation() {
    Task data = new Communication("data");
    assertFalse(PropertyServiceData.isWhileOutput(data));
    String whileEndRef = "whileEndRef";
    PropertyServiceData.annotateOriginalWhileEnd(data, whileEndRef);
    assertEquals(whileEndRef, PropertyServiceData.getOriginalWhileEndReference(data));
  }

  @Test
  void testWhileCounter() {
    Task counter = PropertyServiceData.createWhileCounter("whileCounter");
    assertEquals(NodeType.WhileCounter, PropertyServiceData.getNodeType(counter));
    assertEquals(0, PropertyServiceData.getContent(counter).getAsInt());
    PropertyServiceData.incrementWhileCounter(counter);
    assertEquals(1, PropertyServiceData.getContent(counter).getAsInt());
    Task data = new Communication("data");
    assertThrows(IllegalArgumentException.class, () -> {
      PropertyServiceData.incrementWhileCounter(data);
    });
  }

  @Test
  void testCreateWhileStart() {
    String id = "while";
    Task result = PropertyServiceData.createWhileStart(id);
    assertTrue(result instanceof Communication);
    assertEquals(id, result.getId());
    assertFalse(PropertyServiceData.isConstantNode(result));
    assertTrue(PropertyServiceData.getContent(result).getAsBoolean());
  }

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

  @Test
  public void checkResetContentConstant() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Communication("comm");
      PropertyServiceData.setContent(task, new JsonObject());
      PropertyServiceData.setNodeType(task, NodeType.Constant);
      PropertyServiceData.resetContent(task);
    });
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

  @Test
  public void testSetContentConstant() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Communication("comm");
      PropertyServiceData.setNodeType(task, NodeType.Constant);
      PropertyServiceData.setContent(task, new JsonObject());
    });
  }

  @Test
  public void testGetContentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Communication("comm");
      PropertyServiceData.getContent(task);
    });
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

  @Test
  public void testCheckTaskFail() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Task("task");
      PropertyServiceData.checkTask(task);
    });
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

  @Test
  public void testDataTypeUnset() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Communication("comm");
      PropertyServiceData.getDataType(task);
    });
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

  @Test
  public void testJsonKeyNonRootLeaf() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Communication("comm");
      PropertyServiceData.getJsonKey(task);
    });
  }

  @Test
  public void testJsonKeySetNonRootLeaf() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Communication("comm");
      PropertyServiceData.setJsonKey(task, "key");
    });
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
