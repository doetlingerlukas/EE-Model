package at.uibk.dps.ee.model.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

public class ResourceGraphTest {

  @Test
  public void testCopyConstructor() {
    Resource res0 = new Resource("r0");
    Resource res1 = new Resource("r1");
    Resource res2 = new Resource("r2");

    Link l0 = new Link("l0");
    Link l1 = new Link("l1");

    Architecture<Resource, Link> original = new Architecture<>();
    original.addEdge(l0, res0, res1, EdgeType.UNDIRECTED);
    original.addEdge(l1, res0, res2, EdgeType.UNDIRECTED);

    ResourceGraph tested = new ResourceGraph(original);

    assertNotNull(tested.getVertex(res0));
    assertNotNull(tested.getVertex(res1));
    assertNotNull(tested.getVertex(res2));

    assertEquals(l0, tested.findEdge(res0, res1));
    assertEquals(l1, tested.findEdge(res0, res2));
  }
}
