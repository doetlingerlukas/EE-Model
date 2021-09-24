package at.uibk.dps.ee.model.persistance;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Application;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

class EnactmentGraphTransformerTest {

  Task src;
  Task dst;
  Dependency dep;

  @BeforeEach
  void setup() {
    src = new Task("src");
    dst = new Task("dst");
    dep = new Dependency("dep");
  }

  @Test
  void testOdseToApollo() {
    Application<Task, Dependency> appl = new Application<>();
    appl.addEdge(dep, src, dst, EdgeType.DIRECTED);
    EnactmentGraph result = EnactmentGraphTransformer.toApollo(appl);
    assertTrue(result.containsEdge(dep.getId()));
    assertTrue(result.containsVertex(src.getId()));
    assertTrue(result.containsVertex(dst.getId()));
    assertEquals(src, result.getSource(dep));
  }

  @Test
  void testApolloToOdse() {
    EnactmentGraph eGraph = new EnactmentGraph();
    eGraph.addEdge(dep, src, dst, EdgeType.DIRECTED);
    Application<Task, Dependency> result = EnactmentGraphTransformer.toOdse(eGraph);
    assertTrue(result.containsEdge(dep));
    assertTrue(result.containsVertex(src));
    assertTrue(result.containsVertex(dst));
    assertEquals(src, result.getSource(dep));
  }

}
