package at.uibk.dps.ee.model.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonPrimitive;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceData;
import at.uibk.dps.ee.model.properties.PropertyServiceData.DataType;
import at.uibk.dps.ee.model.properties.PropertyServiceDependency;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;

public class UtilsEnactmentGraphTest {

  EnactmentGraph input;

  Task input1;
  Task input2;

  Task input3;

  @BeforeEach
  void setup() {
    input = new EnactmentGraph();

    input1 = new Communication("input1");
    input2 =
        PropertyServiceData.createConstantNode("input2", DataType.Number, new JsonPrimitive(3));
    input3 =
        PropertyServiceData.createConstantNode("input3", DataType.Number, new JsonPrimitive(3));



    Task t0 = new Task("t0");
    Task t1 = new Task("t1");

    Communication d4 = new Communication("d4");
    Communication d5 = new Communication("d5");

    PropertyServiceDependency.addDataDependency(input1, t0, "bla", input);
    PropertyServiceDependency.addDataDependency(input2, t0, "bla", input);
    PropertyServiceDependency.addDataDependency(t0, d4, "bla", input);
    PropertyServiceDependency.addDataDependency(input3, t1, "bla", input);
    PropertyServiceDependency.addDataDependency(d4, t1, "bla", input);
    PropertyServiceDependency.addDataDependency(t1, d5, "bla", input);
  }

  @Test
  void testGetRootDataNodesExc() {
    assertThrows(IllegalStateException.class, () -> {
      UtilsEnactmentGraph.getNonConstRootNodes(input);
    });
  }

  @Test
  void testGetRootDataNodes() {
    PropertyServiceData.makeRoot(input1);
    Set<Task> result = UtilsEnactmentGraph.getNonConstRootNodes(input);
    assertEquals(1, result.size());
    assertTrue(result.contains(input1));
  }

  @Test
  void testGetConstantNodes() {
    Set<Task> result = UtilsEnactmentGraph.getConstantDataNodes(input);
    assertEquals(2, result.size());
    assertTrue(result.contains(input2));
    assertTrue(result.contains(input3));
  }



}
