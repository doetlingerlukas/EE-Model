package at.uibk.dps.ee.model.properties;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceDependency.TypeDependency;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlowCollections.OperationType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

public class PropertyServiceReproductionTest {

  @Test
  public void testIsReproduced() {
    Task task = new Task("task");
    assertFalse(PropertyServiceReproduction.isReproduced(task));
    PropertyServiceReproduction.makeReproduced(task);
    assertTrue(PropertyServiceReproduction.isReproduced(task));
  }

  @Test
  public void testGetSetReproductionScope() {
    Task task = new Task("task");
    String scope = "scope";
    PropertyServiceReproduction.makeReproduced(task);
    PropertyServiceReproduction.setReproductionScope(task, scope);
    assertEquals(scope, PropertyServiceReproduction.getReproductionScope(task));
  }

  @Test
  public void testGetSetReproductionScopeExc1() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Task("task");
      PropertyServiceReproduction.getReproductionScope(task);
    });
  }

  @Test
  public void testGetSetReproductionScopeExc2() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Task("task");
      String scope = "scope";
      PropertyServiceReproduction.setReproductionScope(task, scope);
    });
  }

  @Test
  public void testDistributionNodeAnnotation() {
    Task task = new Task("task");
    Task distributionNode = PropertyServiceFunctionDataFlowCollections
        .createCollectionDataFlowTask("distribution", OperationType.Distribution, "scope");
    assertFalse(PropertyServiceReproduction.belongsToDistributionNode(task, distributionNode));
    PropertyServiceReproduction.annotateDistributionNode(task, distributionNode.getId());
    assertTrue(PropertyServiceReproduction.belongsToDistributionNode(task, distributionNode));
  }

  @Test
  public void testMakeOffspringTask() {
    Task parent = new Task("parent");
    String scope = "scope";
    String childId = "child";
    Task child = PropertyServiceReproduction.createOffspringTask(parent, scope, childId);
    assertEquals(childId, child.getId());
    assertEquals(parent, child.getParent());
    assertTrue(PropertyServiceReproduction.isReproduced(child));
    assertEquals(scope, PropertyServiceReproduction.getReproductionScope(child));

    Task otherParent = new Communication("comm");
    Task otherChild = PropertyServiceReproduction.createOffspringTask(otherParent, scope, childId);
    assertTrue(otherChild instanceof Communication);
  }

  @Test
  public void testAddDependencyOffspring() {
    EnactmentGraph graph = new EnactmentGraph();
    Task parentSrc = new Communication("parSrc");
    Task parentDst = new Task("parentDst");
    Dependency parent =
        PropertyServiceDependency.addDataDependency(parentSrc, parentDst, "ke", graph);
    Task childSrc = new Task("childSrc");
    Communication childDst = new Communication("childDst");
    String jsonKey = "key";
    String scope = "scope";

    PropertyServiceReproduction.addDataDependencyOffspring(childSrc, childDst, jsonKey, graph,
        parent, scope);
    Dependency child = graph.getOutEdges(childSrc).iterator().next();
    assertTrue(PropertyServiceReproduction.isReproduced(child));
    assertEquals(scope, PropertyServiceReproduction.getReproductionScope(child));
    assertEquals(jsonKey, PropertyServiceDependency.getJsonKey(child));
    assertEquals(parent, child.getParent());
  }

  @Test
  public void testAddDependencyOffspringIf() {
    EnactmentGraph graph = new EnactmentGraph();
    Task parentSrc = new Communication("parSrc");
    Task parentDst = new Task("parentDst");
    PropertyServiceDependencyControlIf.addIfDependency(parentSrc, parentDst, "key", false, graph);
    Dependency parent = graph.getOutEdges(parentSrc).iterator().next();
    Task childSrc = new Communication("childSrc");
    Task childDst = new Task("childDst");
    String jsonKey = "key";
    String scope = "scope";
    PropertyServiceReproduction.addDataDependencyOffspring(childSrc, childDst, jsonKey, graph,
        parent, scope);
    Dependency child = graph.getOutEdges(childSrc).iterator().next();
    assertTrue(PropertyServiceReproduction.isReproduced(child));
    assertEquals(scope, PropertyServiceReproduction.getReproductionScope(child));
    assertEquals(jsonKey, PropertyServiceDependency.getJsonKey(child));
    assertEquals(parent, child.getParent());
    assertEquals(TypeDependency.ControlIf, PropertyServiceDependency.getType(child));
    assertFalse(PropertyServiceDependencyControlIf.getActivation(child));
  }
}
