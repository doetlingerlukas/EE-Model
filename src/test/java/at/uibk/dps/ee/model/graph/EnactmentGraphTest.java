package at.uibk.dps.ee.model.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Application;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

public class EnactmentGraphTest {

  @Test
  public void test() {
    Application<Task, Dependency> original = new Application<>();

    Task t0 = new Task("t0");
    Task c0 = new Communication("c0");
    Task t1 = new Task("t1");

    Dependency d0 = new Dependency("d0");
    Dependency d1 = new Dependency("d1");

    original.addEdge(d0, t0, c0, EdgeType.DIRECTED);
    original.addEdge(d1, c0, t1, EdgeType.DIRECTED);

    EnactmentGraph tested = new EnactmentGraph(original);

    assertNotNull(tested.getVertex(t0));
    assertNotNull(tested.getVertex(c0));
    assertNotNull(tested.getVertex(t1));

    assertEquals(d0, original.findEdge(t0, c0));
    assertEquals(d1, original.findEdge(c0, t1));
  }
  
  @Test
  void testAddTwoEdges() {
    Task src = new Task("src");
    Task dst = new Task("dst");
    Task src2 = new Task("src2");
    Dependency dep = new Dependency("dep");
    Dependency dep2 = new Dependency("dep2");
    EnactmentGraph eGraph = new EnactmentGraph();
    
    eGraph.addEdge(dep, src, dst, EdgeType.DIRECTED);
    eGraph.addEdge(dep2, src2, dst, EdgeType.DIRECTED);
    
    assertTrue(eGraph.containsEdgeWithId(dep.getId()));
    assertTrue(eGraph.containsEdgeWithId(dep2.getId()));
    
    assertTrue(eGraph.getInEdges(dst).contains(dep));
    assertTrue(eGraph.getInEdges(dst).contains(dep2));
    
    assertTrue(eGraph.getOutEdges(src).contains(dep));
    assertTrue(eGraph.getOutEdges(src2).contains(dep2));
    
    assertEquals(src, eGraph.getSource(dep));
    assertEquals(src2, eGraph.getSource(dep2));
    assertEquals(dst, eGraph.getDest(dep));
    assertEquals(dst, eGraph.getDest(dep2));
  }
  
  @Test
  void testAddRemoveEdge() {
    Task src = new Task("src");
    Task dst = new Task("dst");
    Dependency dep = new Dependency("dep");
    EnactmentGraph eGraph = new EnactmentGraph();
    
    assertFalse(eGraph.containsEdgeWithId(dep.getId()));
    assertFalse(eGraph.containsVertexWithId(src.getId()));
    assertFalse(eGraph.containsVertexWithId(dst.getId()));
    
    eGraph.addEdge(dep, src, dst, EdgeType.DIRECTED);
    
    assertTrue(eGraph.containsEdgeWithId(dep.getId()));
    assertTrue(eGraph.containsVertexWithId(src.getId()));
    assertTrue(eGraph.containsVertexWithId(dst.getId()));
    
    assertEquals(src, eGraph.getSource(dep));
    assertEquals(dst, eGraph.getDest(dep));
    
    assertTrue(eGraph.getInEdges(dst).size() == 1);
    assertTrue(eGraph.getOutEdges(src).size() == 1);
    assertEquals(dep, eGraph.getInEdges(dst).iterator().next());
    assertEquals(dep, eGraph.getOutEdges(src).iterator().next());
    
    assertEquals(dep, eGraph.getEdge(dep.getId()));
    
    eGraph.removeEdge(dep);
    
    assertThrows(IllegalArgumentException.class, () -> {
      eGraph.getEdge(dep.getId());
    });
    
    assertFalse(eGraph.containsEdgeWithId(dep.getId()));
    assertTrue(eGraph.containsVertexWithId(src.getId()));
    assertTrue(eGraph.containsVertexWithId(dst.getId()));
    
    assertTrue(eGraph.getInEdges(dst).isEmpty());
    assertTrue(eGraph.getOutEdges(src).isEmpty());
    
    assertThrows(IllegalArgumentException.class, () -> {
      eGraph.getSource(dep);
    });
    
    assertThrows(IllegalArgumentException.class, () -> {
      eGraph.getDest(dep);
    });
    
  }
  
  @Test
  void testIllegalMethods() {
    EnactmentGraph graph = new EnactmentGraph();
    assertThrows(IllegalStateException.class, () -> {
      graph.getEdge(new Dependency("dep"));
    });
  }

  @Test
  void testAddRemoveVertex() {
    EnactmentGraph tested = new EnactmentGraph();
    Task t1 = new Task("t1");
    Task t2 = new Task("t2");
    Task t3 = new Task("t3");
    Dependency dep = new Dependency("dep");
    assertThrows(IllegalStateException.class, () -> {
      tested.getTask("t1");
    });
    assertThrows(IllegalStateException.class, () -> {
      tested.getTask("t2");
    });
    assertThrows(IllegalStateException.class, () -> {
      tested.getTask("t3");
    });
    tested.addVertex(t1);
    assertEquals(t1, tested.getTask("t1"));
    tested.addEdge(dep, t2, t3, EdgeType.DIRECTED);
    assertEquals(t2, tested.getTask("t2"));
    assertEquals(t3, tested.getTask("t3"));
    tested.removeVertex(t1);
    assertThrows(IllegalStateException.class, () -> {
      tested.getTask("t1");
    });
    tested.removeEdge(dep);
    assertEquals(t2, tested.getTask("t2"));
    assertEquals(t3, tested.getTask("t3"));
    tested.removeVertex(t2);
    assertThrows(IllegalStateException.class, () -> {
      tested.getTask("t2");
    });
    tested.removeVertex(t3);
    assertThrows(IllegalStateException.class, () -> {
      tested.getTask("t3");
    });
  }
}
