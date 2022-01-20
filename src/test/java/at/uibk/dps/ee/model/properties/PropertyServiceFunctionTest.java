package at.uibk.dps.ee.model.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionTest {

  @Test
  void testGetSetNeglectableWorkload() {
    Task task = new Task("task");
    assertTrue(PropertyServiceFunction.hasNegligibleWorkload(task));
    PropertyServiceFunction.annotateNonNegligibleWorkload(task);
    assertFalse(PropertyServiceFunction.hasNegligibleWorkload(task));
  }

  @Test
  void testOutput() {
    Task function = new Task("function");
    assertFalse(PropertyServiceFunction.isOutputSet(function));
    assertThrows(IllegalStateException.class, () -> {
      PropertyServiceFunction.getOutput(function);
    });
    JsonObject output = new JsonObject();
    PropertyServiceFunction.setOutput(function, output);
    assertThrows(IllegalStateException.class, () -> {
      PropertyServiceFunction.setOutput(function, output);
    });
    assertTrue(PropertyServiceFunction.isOutputSet(function));
    assertEquals(output, PropertyServiceFunction.getOutput(function));
    PropertyServiceFunction.resetOutput(function);
    assertFalse(PropertyServiceFunction.isOutputSet(function));
  }

  @Test
  public void testNotFunction() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task comm = new Communication("comm");
      PropertyServiceFunction.checkTask(comm);
    });
  }

  @Test
  public void testGetSetFunctionType() {
    Task task = new Task("task");
    PropertyServiceFunction.setUsageType(UsageType.User, task);
    assertEquals(UsageType.User, PropertyServiceFunction.getUsageType(task));
  }

  @Test
  public void testSetInput() {
    Task task = new Task("task");
    assertFalse(PropertyServiceFunction.isInputSet(task));
    assertThrows(IllegalStateException.class, () -> {
      PropertyServiceFunction.getInput(task);
    });
    JsonElement element = new JsonPrimitive(42);
    String key = "key";
    JsonObject input = new JsonObject();
    input.add(key, element);
    PropertyServiceFunction.setInput(task, input);
    assertTrue(PropertyServiceFunction.isInputSet(task));
    assertEquals(input, PropertyServiceFunction.getInput(task));
    PropertyServiceFunction.resetInput(task);
    assertFalse(PropertyServiceFunction.isInputSet(task));
  }

  @Test
  public void testSetInputAlredySet() {
    assertThrows(IllegalStateException.class, () -> {
      Task task = new Task("task");
      assertFalse(PropertyServiceFunction.isInputSet(task));

      JsonElement element = new JsonPrimitive(42);
      String key = "key";
      JsonObject input = new JsonObject();
      input.add(key, element);
      JsonObject input2 = new JsonObject();
      input2.add(key, element);

      PropertyServiceFunction.setInput(task, input);
      PropertyServiceFunction.setInput(task, input2);
    });
  }
}
