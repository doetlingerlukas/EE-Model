package at.uibk.dps.ee.model.persistance;

import at.uibk.dps.ee.model.graph.ResourceGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

/**
 * Transforms between the apollo and the odse resources representation.
 * 
 * @author Fedor Smirnov
 *
 */
public class ResourceGraphTransformer {

  private ResourceGraphTransformer() {}

  public static Architecture<Resource, Link> toOdse(ResourceGraph rGraph) {
    Architecture<Resource, Link> result = new Architecture<>();
    rGraph.forEach(res -> result.addVertex(res));
    rGraph.getEdges().forEach(edge -> result.addEdge(edge, rGraph.getSource(edge),
        rGraph.getDest(edge), EdgeType.UNDIRECTED));
    return result;
  }

  public static ResourceGraph toApollo(Architecture<Resource, Link> architecture) {
    ResourceGraph result = new ResourceGraph();
    architecture.forEach(res -> result.addVertex(res));
    architecture.getEdges()
        .forEach(edge -> result.addEdge(edge, architecture.getEndpoints(edge).getFirst(),
            architecture.getEndpoints(edge).getSecond(), EdgeType.UNDIRECTED));
    return result;
  }
}
