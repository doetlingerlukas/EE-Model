package at.uibk.dps.ee.model.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

public class PropertyServiceLinkTest {

  @Test
  public void testLinkCreation() {
    String id1 = "res1";
    String id2 = "res2";
    Resource res1 = new Resource(id1);
    Resource res2 = new Resource(id2);
    String expectedId = id1 + ConstantsEEModel.KeywordSeparator1 + id2;
    ResourceGraph resGraph = new ResourceGraph();
    PropertyServiceLink.connectResources(resGraph, res1, res2);
    assertEquals(2, resGraph.getVertexCount());
    assertEquals(1, resGraph.getEdgeCount());
    Link link = resGraph.getIncidentEdges(res1).iterator().next();
    assertEquals(expectedId, link.getId());
    assertEquals(link, resGraph.getIncidentEdges(res2).iterator().next());
  }
}
