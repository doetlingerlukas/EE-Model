package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNotFunction() {
    Task comm = new Communication("comm");
    PropertyServiceFunction.checkTask(comm);
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
  
  @Test(expected = IllegalStateException.class)
  public void testSetInputAlredySet() {
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
  }
}
