package at.uibk.dps.ee.model.utils;

import java.util.Set;
import java.util.stream.Collectors;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceData;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static container for utility methods used to process enactment graphs.
 * 
 * @author Fedor Smirnov
 *
 */
public final class EnactmentGraphUtils {

  /**
   * Returns the constant data nodes of the given graph.
   * 
   * @param graph the given graph.
   * @return the constant data nodes of the given graph
   */
  public static Set<Task> getConstantDataNodes(EnactmentGraph graph) {
    return graph.getVertices().stream().filter(task -> TaskPropertyService.isCommunication(task))
        .filter(dataNode -> PropertyServiceData.isConstantNode(dataNode))
        .collect(Collectors.toSet());
  }

  /**
   * Returns the non-constant root data nodes (inputs) of the given graph.
   * 
   * @param graph the given graph.
   * @return the non-constant root data nodes (inputs) of the given graph
   */
  public static Set<Task> getNonConstRootNodes(EnactmentGraph graph) {
    try {
      final Set<Task> result =
          graph.getVertices().stream().filter(task -> graph.getInEdges(task).size() == 0)
              .filter(task -> !PropertyServiceData.isConstantNode(task))
              .collect(Collectors.toSet());
      if (result.stream().anyMatch(task -> !PropertyServiceData.isRoot(task))) {
        throw new IllegalStateException("Non-root nodes without in edges present.");
      }
      return result;
    } catch (Exception exc) {
      exc.printStackTrace();
      throw exc;
    }
  }

  /**
   * No constructor.
   */
  private EnactmentGraphUtils() {}
}
