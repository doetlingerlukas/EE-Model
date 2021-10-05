package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceData.NodeType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Element;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Contains convenience methods to access the properties of the nodes modeling
 * utility functions.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionUtility extends AbstractPropertyService {

  /**
   * No constructor.
   */
  private PropertyServiceFunctionUtility() {}

  /**
   * Properties defining attribute names
   * 
   * @author Fedor Smirnov
   *
   */
  protected enum Property {
    /**
     * The type of utility function
     */
    UtilityType
  }

  /**
   * Types of utility functions
   * 
   * @author Fedor Smirnov
   *
   */
  public enum UtilityType {
    /**
     * Evaluating boolean conditions.
     */
    Condition, CollectionOperation, While,
    /**
     * Establishing sequentiality between two data nodes (used for nested whiles)
     */
    Sequelizer
  }

  /**
   * Adds a sequelizer node to the given graph to make sure that dataFirst
   * precedes dataSecond.
   * 
   * @param dataFirst the first node
   * @param dataSecond the second node
   * @param graph the enactment graph
   * @return the node which was added to the graph
   */
  static Task addSequelizerNode(final Task dataFirst, final Task dataSecond,
      final EnactmentGraph graph) {
    if (!(TaskPropertyService.isCommunication(dataFirst)
        && TaskPropertyService.isCommunication(dataSecond))) {
      throw new IllegalArgumentException("A sequelizer node has to connect 2 data nodes.");
    }
    final String seqNodeId = dataFirst.getId() + "-to-" + dataSecond.getId();
    final Task result = new Task(seqNodeId);
    PropertyServiceFunction.setUsageType(UsageType.Utility, result);
    setUtilityType(result, UtilityType.Sequelizer);
    PropertyServiceDependency.addDataDependency(dataFirst, result,
        ConstantsEEModel.JsonKeySequentiality, graph);
    PropertyServiceDependency.addDataDependency(result, dataSecond,
        ConstantsEEModel.JsonKeySequentiality, graph);
    return result;
  }

  /**
   * Adds and returns a dependency enforcing sequentiality between the given
   * nodes.
   * 
   * @param predecessor the predecessor (function)
   * @param successor the successor (data)
   * @param graph the enactment graph
   * @return dependency enforcing sequentiality between the given nodes
   */
  static Dependency addSequelizerEdge(final Task predecessor, final Task successor,
      final EnactmentGraph graph) {
    PropertyServiceData.setNodeType(successor, NodeType.Sequentiality);
    return PropertyServiceDependency.addDataDependency(predecessor, successor,
        ConstantsEEModel.JsonKeySequentiality, graph);
  }

  /**
   * Adds the elements necessary to enforce sequentiality between the given tasks
   * 
   * @param predecessor the task which has to come first
   * @param successor the task which has to come second
   * @param graph the enactment graph
   */
  public static Element enforceSequentiality(final Task predecessor, final Task successor,
      final EnactmentGraph graph) {
    if (TaskPropertyService.isCommunication(successor)) {
      if (TaskPropertyService.isCommunication(predecessor)) {
        // seq between two data nodes
        return addSequelizerNode(predecessor, successor, graph);
      } else {
        // seq between a function and data
        return addSequelizerEdge(predecessor, successor, graph);
      }
    } else {
      throw new IllegalArgumentException(
          "Seq enforcement: successor " + successor + " is not a data node.");
    }
  }

  /**
   * Annotates the given task with the given utility type.
   * 
   * @param task the task to annotate
   * @param type the type of the task
   */
  static void setUtilityType(final Task task, final UtilityType type) {
    checkTask(task);
    final String attrName = Property.UtilityType.name();
    task.setAttribute(attrName, type.name());
  }

  /**
   * Returns the utility type of the given task.
   * 
   * @param task the given task
   * @return the utility type of the given task
   */
  public static UtilityType getUtilityType(final Task task) {
    checkTask(task);
    final String attrName = Property.UtilityType.name();
    return UtilityType.valueOf((String) getAttribute(task, attrName));
  }

  /**
   * Checks that the given task is a utility function. Throws an exception
   * otherwise.
   * 
   * @param task the task to check
   */
  static void checkTask(final Task task) {
    PropertyServiceFunction.checkTask(task);
    if (!PropertyServiceFunction.getUsageType(task).equals(UsageType.Utility)) {
      throw new IllegalArgumentException("Task " + task.getId() + " is not a utility function.");
    }
  }
}
