package at.uibk.dps.ee.model.persistance;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

class ResourceGraphTransformerTest {

  Resource first;
  Resource second;
  Link link;

  @BeforeEach
  void setup() {
    first = new Resource("first");
    second = new Resource("second");
    link = new Link("link");
  }

  @Test
  void testOdseToApollo() {
    Architecture<Resource, Link> arch = new Architecture<>();
    arch.addEdge(link, first, second, EdgeType.UNDIRECTED);
    ResourceGraph rGraph = ResourceGraphTransformer.toApollo(arch);
    assertTrue(rGraph.containsEdge(link.getId()));
    assertTrue(rGraph.containsVertex(first.getId()));
    assertTrue(rGraph.containsVertex(second.getId()));
    assertEquals(link, rGraph.findEdge(first, second));
  }

  @Test
  void testApolloToOdse() {
    ResourceGraph rGraph = new ResourceGraph();
    rGraph.addEdge(link, first, second, EdgeType.UNDIRECTED);
    Architecture<Resource, Link> arch = ResourceGraphTransformer.toOdse(rGraph);
    assertTrue(arch.containsEdge(link));
    assertTrue(arch.containsVertex(first));
    assertTrue(arch.containsVertex(second));
    assertEquals(link, arch.findEdge(first, second));
  }
}
