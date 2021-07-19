package at.uibk.dps.ee.model.properties;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceDependency.TypeDependency;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

public class PropertyServiceDependencyControlIfTest {

  @Test
  public void testAddDependency() {
    Task src = new Task("task");
    Communication dst = new Communication("comm");
    String key = "key";
    boolean activation = false;
    EnactmentGraph graph = new EnactmentGraph();
    PropertyServiceDependencyControlIf.addIfDependency(src, dst, key, activation, graph);
    assertEquals(1, graph.getEdgeCount());
    Dependency created = graph.getEdges().iterator().next();
    assertFalse(PropertyServiceDependencyControlIf.getActivation(created));
    assertEquals(src, graph.getSource(created));
    assertEquals(dst, graph.getDest(created));
  }

  @Test
  public void testCreateDependency() {
    Task src = new Task("src");
    Task dest = new Task("dest");
    String jsonKey = "jsonkey";
    boolean activation = false;

    Dependency result = PropertyServiceDependencyControlIf.createControlIfDependency(src, dest,
        jsonKey, activation);
    assertEquals(jsonKey, PropertyServiceDependency.getJsonKey(result));
    assertEquals(TypeDependency.ControlIf, PropertyServiceDependency.getType(result));
    assertFalse(PropertyServiceDependencyControlIf.getActivation(result));
  }

  @Test
  public void testGetSetActivation() {
    Dependency dep = new Dependency("dep");
    PropertyServiceDependency.setType(dep, TypeDependency.ControlIf);
    PropertyServiceDependencyControlIf.setActivation(dep, false);
    assertFalse(PropertyServiceDependencyControlIf.getActivation(dep));
  }

  @Test
  public void testCheckDep() {
    assertThrows(IllegalArgumentException.class, () -> {
      Dependency dep = new Dependency("dep");
      PropertyServiceDependency.setType(dep, TypeDependency.Data);
      PropertyServiceDependencyControlIf.checkDependency(dep);
    });
  }
}
