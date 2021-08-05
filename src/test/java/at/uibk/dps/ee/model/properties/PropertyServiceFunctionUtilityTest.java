package at.uibk.dps.ee.model.properties;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Communication;
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
  void testSequelizer() {
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
