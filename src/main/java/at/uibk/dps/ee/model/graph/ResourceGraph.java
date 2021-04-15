package at.uibk.dps.ee.model.graph;

import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

/**
 * The {@link ResourceGraph} models the resources available for the execution,
 * as well as their connections to each other.
 * 
 * @author Fedor Smirnov
 *
 */
public class ResourceGraph extends Architecture<Resource, Link> {

  private static final long serialVersionUID = 1L;
  
  /**
   * Default constructor.
   */
  public ResourceGraph() {
  }
  
  /**
   * Constructor to build the {@link ResourceGraph} out of an architecture.
   * 
   * @param arch the given architecture
   */
  public ResourceGraph(Architecture<Resource, Link> arch) {
    super();
    arch.getEdges().forEach(link -> {
      Resource res1 = arch.getEndpoints(link).getFirst();
      Resource res2 = arch.getEndpoints(link).getSecond();
      this.addEdge(link, res1, res2, EdgeType.UNDIRECTED);
    });
    arch.forEach(res -> this.addVertex(res));
  }
}
