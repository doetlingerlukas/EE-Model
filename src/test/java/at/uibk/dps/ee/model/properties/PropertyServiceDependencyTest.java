package at.uibk.dps.ee.model.properties;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceDependency.TypeDependency;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

public class PropertyServiceDependencyTest {

  @Test
  void testWhileReplicaAnnotation() {
    Dependency dep = new Dependency("dep");

    String whileNameFirst = "while";
    String whileNameSecond = "whileSecond";

    String dataNameFirst = "data";
    String dataNameSecond = "dataSecond";

    assertFalse(PropertyServiceDependency.isWhileAnnotated(dep));
    
    assertThrows(IllegalArgumentException.class, () -> {
      PropertyServiceDependency.resetWhileAnnotation(dep);
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
      PropertyServiceDependency.getWhileDataReferences(dep);
    });

    assertThrows(IllegalArgumentException.class, () -> {
      PropertyServiceDependency.getWhileFuncReferences(dep);
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
      PropertyServiceDependency.getDataRefForWhile(dep, whileNameFirst);
    });
    
    PropertyServiceDependency.addWhileDataReference(dep, dataNameFirst, whileNameFirst);
    PropertyServiceDependency.addWhileDataReference(dep, dataNameSecond, whileNameSecond);
    assertTrue(PropertyServiceDependency.isWhileAnnotated(dep));
    
    assertEquals(dataNameSecond,
        PropertyServiceDependency.getDataRefForWhile(dep, whileNameSecond));
    
    assertThrows(IllegalStateException.class, () -> {
      PropertyServiceDependency.getDataRefForWhile(dep, "otherWhile");
    });

    PropertyServiceDependency.resetWhileAnnotation(dep);
    assertFalse(PropertyServiceDependency.isWhileAnnotated(dep));
    
    

  }

  @Test
  void testPreviousDependencyAnnotation() {
    Dependency dep = new Dependency("dependency");
    assertFalse(PropertyServiceDependency.doesPointToPreviousIteration(dep));
    PropertyServiceDependency.annotatePreviousIterationDependency(dep);
    assertTrue(PropertyServiceDependency.doesPointToPreviousIteration(dep));
  }

  @Test
  public void testExtractionDone() {
    Dependency dep = new Dependency("dep");
    assertFalse(PropertyServiceDependency.isExtractionDone(dep));
    PropertyServiceDependency.setExtractionDone(dep);
    assertTrue(PropertyServiceDependency.isExtractionDone(dep));
    PropertyServiceDependency.resetExtractionDone(dep);
    assertFalse(PropertyServiceDependency.isExtractionDone(dep));
  }

  @Test
  public void testDataConsumptionNotOkay() {
    assertThrows(IllegalStateException.class, () -> {
      Task task1 = new Task("task");
      Task task2 = new Task("othertask");
      Dependency dep =
          PropertyServiceDependency.createDependency(task1, task2, new EnactmentGraph());
      PropertyServiceDependency.setDataConsumed(dep);
    });
  }

  @Test
  public void testDataConsumptionOkay() {
    Task task1 = new Task("task");
    Task task2 = new Task("othertask");
    Dependency dep = PropertyServiceDependency.createDependency(task1, task2, new EnactmentGraph());
    PropertyServiceDependency.annotateFinishedTransmission(dep);
    assertFalse(PropertyServiceDependency.isDataConsumed(dep));
    PropertyServiceDependency.setDataConsumed(dep);
    assertTrue(PropertyServiceDependency.isDataConsumed(dep));
    PropertyServiceDependency.resetTransmission(dep);
    assertFalse(PropertyServiceDependency.isDataConsumed(dep));
    assertFalse(PropertyServiceDependency.isTransmissionDone(dep));
  }

  @Test
  public void testTransmissionAnnotation() {
    Task task1 = new Task("task");
    Task task2 = new Task("othertask");
    Dependency dep = PropertyServiceDependency.createDependency(task1, task2, new EnactmentGraph());
    assertFalse(PropertyServiceDependency.isTransmissionDone(dep));
    PropertyServiceDependency.annotateFinishedTransmission(dep);
    assertTrue(PropertyServiceDependency.isTransmissionDone(dep));
    PropertyServiceDependency.resetTransmissionAnnotation(dep);
    assertFalse(PropertyServiceDependency.isTransmissionDone(dep));
  }

  @Test
  public void testAddDataDependency() {
    Task proc = new Task("process");
    Communication data = new Communication("data");
    String jsonKey = "jsonKey";
    EnactmentGraph graph = new EnactmentGraph();
    PropertyServiceDependency.addDataDependency(proc, data, jsonKey, graph);
    Dependency result = graph.getOutEdges(proc).iterator().next();
    assertEquals(jsonKey, PropertyServiceDependency.getJsonKey(result));
    assertEquals(TypeDependency.Data, PropertyServiceDependency.getType(result));
  }

  @Test
  public void testChecDataDepEndpointsTwoProc() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task proc1 = new Task("process1");
      Task proc2 = new Task("process2");
      PropertyServiceDependency.checkDataDependencyEndPoints(proc1, proc2);
    });
  }

  @Test
  public void testChecDataDepEndpointsTwoData() {
    assertThrows(IllegalArgumentException.class, () -> {
      Communication data1 = new Communication("data1");
      Communication data2 = new Communication("data2");
      PropertyServiceDependency.checkDataDependencyEndPoints(data2, data1);
    });
  }

  @Test
  public void testChecDataDepEndpoints() {
    Task proc = new Task("process");
    Communication data = new Communication("data");
    PropertyServiceDependency.checkDataDependencyEndPoints(proc, data);
    PropertyServiceDependency.checkDataDependencyEndPoints(data, proc);
  }

  @Test
  public void testCreateDependency() {
    String srcString = "src";
    String dstString = "dest";
    Task src = new Task(srcString);
    Task dst = new Task(dstString);
    String expected = srcString + ConstantsEEModel.KeywordSeparator1 + dstString;
    Dependency result = PropertyServiceDependency.createDependency(src, dst, new EnactmentGraph());
    assertEquals(expected, result.getId());
  }

  @Test
  void testMultipleDependencies() {
    String srcString = "src";
    String dstString = "dest";
    Task src = new Task(srcString);
    Task dst = new Communication(dstString);
    EnactmentGraph graph = new EnactmentGraph();
    String expected = srcString + ConstantsEEModel.KeywordSeparator1 + dstString;
    Dependency result = PropertyServiceDependency.addDataDependency(src, dst, "key1", graph);
    assertEquals(expected, result.getId());
    Dependency result2 = PropertyServiceDependency.addDataDependency(src, dst, "key2", graph);
    assertNotEquals(result, result2);
    assertEquals(2, graph.getEdgeCount());
  }

  @Test
  public void testGetSetJsonKey() {
    Dependency dep = new Dependency("dep");
    String key = "myKey";
    PropertyServiceDependency.setJsonKey(dep, key);
    assertEquals(key, PropertyServiceDependency.getJsonKey(dep));
  }

  @Test
  public void testGetSetType() {
    Dependency dep = new Dependency("dep");
    PropertyServiceDependency.setType(dep, TypeDependency.Data);
    assertEquals(TypeDependency.Data, PropertyServiceDependency.getType(dep));
  }
}
