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
public final class ResourceGraphTransformer {

  /**
   * No constructors
   */
  private ResourceGraphTransformer() {}

  /**
   * Transforms an apollo resource graph to an odes architecture
   * 
   * @param rGraph the resource graph (Apollo)
   * @return the architecture (odse)
   */
  public static Architecture<Resource, Link> toOdse(final ResourceGraph rGraph) {
    final Architecture<Resource, Link> result = new Architecture<>();
    rGraph.forEach(res -> result.addVertex(res));
    rGraph.getEdges().forEach(edge -> result.addEdge(edge, rGraph.getSource(edge),
        rGraph.getDest(edge), EdgeType.UNDIRECTED));
    return result;
  }

  /**
   * Transforms an odse architecture to an apollo resource graph.
   * 
   * @param architecture the odse architecture
   * @return the apollo resource graph
   */
  public static ResourceGraph toApollo(final Architecture<Resource, Link> architecture) {
    final ResourceGraph result = new ResourceGraph();
    architecture.forEach(res -> result.addVertex(res));
    architecture.getEdges()
        .forEach(edge -> result.addEdge(edge, architecture.getEndpoints(edge).getFirst(),
            architecture.getEndpoints(edge).getSecond(), EdgeType.UNDIRECTED));
    return result;
  }
}
