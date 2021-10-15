package at.uibk.dps.ee.model.graph;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Task;

class AbstractConcurrentGraphTest {

  protected class MockGraph extends AbstractConcurrentGraph<Task, Link> {
    private static final long serialVersionUID = 1L;
  }

  MockGraph tested;

  @BeforeEach
  void setup() {
    tested = new MockGraph();
  }

  @Test
  void testRemoveEdgeWithVertex() {

    Task t1 = new Task("t1");
    Task t2 = new Task("t2");
    Link link = new Link("link");

    tested.addEdge(link, t1, t2, EdgeType.DIRECTED);
    assertTrue(tested.containsEdge(link.getId()));
    tested.removeVertex(t1);
    assertFalse(tested.containsEdge(link.getId()));
  }

  @Test
  void testRemoveEdgeWithOtherVertex() {

    Task t1 = new Task("t1");
    Task t2 = new Task("t2");
    Link link = new Link("link");

    tested.addEdge(link, t1, t2, EdgeType.UNDIRECTED);
    assertTrue(tested.containsEdge(link.getId()));
    tested.removeVertex(t2);
    assertFalse(tested.containsEdge(link.getId()));
  }

  @Test
  void testGetEndpointsDirected() {
    // directed case
    Task task1 = new Task("task1");
    Task task2 = new Task("task2");
    Link dep = new Link("dep");
    tested.addEdge(dep, task1, task2, EdgeType.DIRECTED);
    assertEquals(task1, tested.getEndpoints(dep).getFirst());
    assertEquals(task2, tested.getEndpoints(dep).getSecond());
  }

  @Test
  void testGetEndpointsUndirected() {
    // directed case
    Task task1 = new Task("task1");
    Task task2 = new Task("task2");
    Link dep = new Link("dep");
    tested.addEdge(dep, task2, task1, EdgeType.UNDIRECTED);
    assertEquals(task2, tested.getEndpoints(dep).getFirst());
    assertEquals(task1, tested.getEndpoints(dep).getSecond());
  }

  @Test
  void testGetEndpointsUndirectedExc() {
    assertThrows(IllegalArgumentException.class, () -> {
      tested.getEndpoints(new Link("link"));
    });
  }

  @Test
  void testForbiddenMethods() {
    assertThrows(IllegalAccessError.class, () -> {
      tested.getEdge(new Link("link"));
    });
    assertThrows(IllegalAccessError.class, () -> {
      tested.getVertex(new Task("task"));
    });
    assertThrows(IllegalAccessError.class, () -> {
      tested.addEdge(new Link("link"), new HashSet<>());
    });
    assertThrows(IllegalAccessError.class, () -> {
      tested.addEdge(new Link("link"), new Task("t1"), new Task("t2"));
    });
    assertThrows(IllegalAccessError.class, () -> {
      tested.addEdge(new Link("link"), new HashSet<>(), EdgeType.DIRECTED);
    });
    assertThrows(IllegalAccessError.class, () -> {
      Task[] tarr = {new Task("t1"), new Task("t2")};
      tested.addEdge(new Link("link"), new Pair<>(tarr));
    });
    assertThrows(IllegalAccessError.class, () -> {
      Task[] tarr = {new Task("t1"), new Task("t2")};
      tested.addEdge(new Link("link"), new Pair<>(tarr), EdgeType.DIRECTED);
    });
    assertThrows(IllegalAccessError.class, () -> {
      tested.getIncidentVertices(new Link("link"));
    });
    assertThrows(IllegalAccessError.class, () -> {
      tested.getNeighbors(new Task("task"));
    });
    assertThrows(IllegalAccessError.class, () -> {
      tested.getOpposite(new Task("t"), new Link("l"));
    });
  }
}
