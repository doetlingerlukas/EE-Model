package at.uibk.dps.ee.model.properties;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceData.NodeType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionUtilityTest {

  @Test
  public void testWrongTask() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task t = new Task("t");
      PropertyServiceFunction.setUsageType(UsageType.User, t);
      PropertyServiceFunctionUtility.setUtilityType(t, UtilityType.Condition);
    });
  }

  @Test
  public void testGetSetType() {
    Task condi = new Task("condi");
    PropertyServiceFunction.setUsageType(UsageType.Utility, condi);
    PropertyServiceFunctionUtility.setUtilityType(condi, UtilityType.Condition);
    assertEquals(UtilityType.Condition, PropertyServiceFunctionUtility.getUtilityType(condi));
  }

  @Test
  void testEnforceSequentiality() {
    Task task = new Task("task");
    Communication comm1 = new Communication("comm1");
    Communication comm2 = new Communication("comm2");
    EnactmentGraph graph = new EnactmentGraph();
    assertTrue(
        (PropertyServiceFunctionUtility.enforceSequentiality(comm1, comm2, graph) instanceof Task));
    assertTrue((PropertyServiceFunctionUtility.enforceSequentiality(task, comm2,
        graph) instanceof Dependency));
  }

  @Test
  void testSequelizerEdge() {
    Task task = new Task("task");
    Communication comm = new Communication("comm");
    EnactmentGraph graph = new EnactmentGraph();
    PropertyServiceFunctionUtility.addSequelizerEdge(task, comm, graph);
    assertEquals(1, graph.getEdges().size());
    assertEquals(NodeType.Sequentiality, PropertyServiceData.getNodeType(comm));
  }

  @Test
  void testSequelizerNode() {
    Task dataFirst = new Communication("first");
    Task dataSecond = new Communication("dataSecond");
    Task task = new Task("task");
    EnactmentGraph graph = new EnactmentGraph();

    assertThrows(IllegalArgumentException.class, () -> {
      PropertyServiceFunctionUtility.addSequelizerNode(dataFirst, task, graph);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      PropertyServiceFunctionUtility.addSequelizerNode(task, dataSecond, graph);
    });

    PropertyServiceFunctionUtility.addSequelizerNode(dataFirst, dataSecond, graph);
    assertEquals(3, graph.getVertexCount());
    Task seq = graph.getSuccessors(dataFirst).iterator().next();
    assertEquals(UtilityType.Sequelizer, PropertyServiceFunctionUtility.getUtilityType(seq));
  }
}
