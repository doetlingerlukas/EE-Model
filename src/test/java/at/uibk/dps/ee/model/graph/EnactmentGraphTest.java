package at.uibk.dps.ee.model.graph;

import static org.junit.Assert.*;
import org.junit.Test;
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

}
