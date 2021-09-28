package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container offering the access to the {@link Link}s connecting
 * the resources in the resource graph.
 * 
 * @author Fedor Smirnov
 *
 */
public final class PropertyServiceLink extends AbstractPropertyService {

  /**
   * Connects the given resources by generating a bidirectional {@link Link} with
   * a unique ID and adding it to the graph.
   * 
   * @param graph the resource graph
   * @param resourceA first resource to connect
   * @param resourceB second resource to connect
   */
  public static void connectResources(final ResourceGraph graph, final Resource resourceA,
      final Resource resourceB) {
    final Link link = new Link(generateLinkID(resourceA, resourceB));
    graph.addEdge(link, resourceA, resourceB, EdgeType.UNDIRECTED);
  }

  /**
   * Generates a unique ID to connect the given resources.
   * 
   * @param resourceA first end point
   * @param resourceB second end point
   * @return a unique ID to connect the given resources
   */
  static String generateLinkID(final Resource resourceA, final Resource resourceB) {
    return resourceA.getId() + ConstantsEEModel.KeywordSeparator1 + resourceB.getId();
  }
}
