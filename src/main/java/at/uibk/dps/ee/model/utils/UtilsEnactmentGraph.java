package at.uibk.dps.ee.model.utils;

import java.util.Set;
import java.util.stream.Collectors;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceData;
import at.uibk.dps.ee.model.properties.PropertyServiceData.NodeType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static container for utility methods used to process enactment graphs.
 * 
 * @author Fedor Smirnov
 *
 */
public final class UtilsEnactmentGraph {

  /**
   * Returns the constant data nodes of the given graph.
   * 
   * @param graph the given graph.
   * @return the constant data nodes of the given graph
   */
  public static Set<Task> getConstantDataNodes(final EnactmentGraph graph) {
    // get the actually constant nodes (constant and while counters)
    final Set<Task> result =
        graph.getVertices().stream().filter(task -> TaskPropertyService.isCommunication(task))
            .filter(dataNode -> PropertyServiceData.isConstantNode(dataNode))
            .collect(Collectors.toSet());
    // add the while data nodes which are not nested
    final Set<Task> relevantWhileNodes = graph.getVertices().stream()
        .filter(node -> TaskPropertyService.isCommunication(node))
        .filter(dataNode -> PropertyServiceData.getNodeType(dataNode).equals(NodeType.WhileStart))
        .filter(whileStart -> graph.getPredecessorCount(whileStart) == 0)
        .collect(Collectors.toSet());
    result.addAll(relevantWhileNodes);
    return result;
  }

  /**
   * Returns the non-constant root data nodes (inputs) of the given graph.
   * 
   * @param graph the given graph.
   * @return the non-constant root data nodes (inputs) of the given graph
   */
  public static Set<Task> getNonConstRootNodes(final EnactmentGraph graph) {
    final Set<Task> result = graph.getVertices().stream()
        .filter(node -> graph.getPredecessorCount(node) == 0).collect(Collectors.toSet());
    result.removeAll(getConstantDataNodes(graph));
    if (result.stream().anyMatch(rootNode -> !PropertyServiceData.isRoot(rootNode))) {
      throw new IllegalStateException("Non root nodes without in edges found.");
    }
    return result;
  }

  /**
   * No constructor.
   */
  private UtilsEnactmentGraph() {}
}
