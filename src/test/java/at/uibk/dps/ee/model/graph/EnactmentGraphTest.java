package at.uibk.dps.ee.model.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
