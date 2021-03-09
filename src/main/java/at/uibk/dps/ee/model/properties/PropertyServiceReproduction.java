package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.graph.EnactmentGraph;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Element;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container for the methods managing the reproduction (happening
 * at run time due to the parallel for) of graph elements.
 * 
 * @author Fedor Smirnov
 *
 */
public final class PropertyServiceReproduction extends AbstractPropertyService {

  /**
   * No constructor
   */
  private PropertyServiceReproduction() {}

  /**
   * Properties shared by tasks and dependencies.
   * 
   * @author Fedor Smirnov
   *
   */
  protected enum Property {
    /**
     * Indicates whether an element was created by reproduction or not.
     */
    IsReproduced,
    /**
     * Indicates in which scope an element was reproduced
     */
    ReproductionScope,
    /**
     * Indicates the distribution node responsible for the reproduction of this
     * element (annotated to originals)
     */
    DistributionNode
  }

  /**
   * Returns true if the given node belongs to the given distribution node.
   * 
   * @param task the task to check
   * @param distributionNode the distribution node
   * @return true if the given node belongs to the given distribution node
   */
  public static boolean belongsToDistributionNode(final Task task, final Task distributionNode) {
    final String attrName = Property.DistributionNode.name();
    if (!isAttributeSet(task, attrName)) {
      return false;
    }
    return distributionNode.getId().equals((String) getAttribute(task, attrName));
  }

  /**
   * Annotates the distribution node id of the given (original) task
   * 
   * @param task the task to annotate
   * @param distributionNode the distribution node
   */
  public static void annotateDistributionNode(final Task task, final String distributionNodeId) {
    final String attrName = Property.DistributionNode.name();
    task.setAttribute(attrName, distributionNodeId);
  }

  /**
   * Annotates the given element with the provided scope string.
   * 
   * @param element the given element
   * @param scopeString the provided scope string
   */
  protected static void setReproductionScope(final Element element, final String scopeString) {
    if (!isReproduced(element)) {
      throw new IllegalArgumentException(
          "The element " + element.getId() + " was not created by reproduction.");
    }
    final String attrName = Property.ReproductionScope.name();
    element.setAttribute(attrName, scopeString);
  }

  /**
   * Returns the reproduction scope of the given element.
   * 
   * @param element the given element
   * @return the reproduction scope of the given element
   */
  public static String getReproductionScope(final Element element) {
    if (!isReproduced(element)) {
      throw new IllegalArgumentException(
          "The element " + element.getId() + " was not created by reproduction.");
    }
    final String attrName = Property.ReproductionScope.name();
    return (String) getAttribute(element, attrName);
  }

  /**
   * Returns true if the given element was created by reproduction.
   * 
   * @param element the given element
   * @return true if the given element was created by reproduction
   */
  public static boolean isReproduced(final Element element) {
    final String attrName = Property.IsReproduced.name();
    if (!isAttributeSet(element, attrName)) {
      return false;
    }
    return (boolean) getAttribute(element, attrName);
  }

  /**
   * Annotates the given element as created by reproduction.
   * 
   * @param element the given element
   */
  protected static void makeReproduced(final Element element) {
    final String attrName = Property.IsReproduced.name();
    element.setAttribute(attrName, true);
  }

  /**
   * Creates a dependency connecting the provided end nodes, annotates it as
   * offspring of the given parent, and returns it.
   * 
   * @param src the source node
   * @param dest the destination node
   * @param jsonKey the jsonKey
   * @param graph the enactment graph
   * @param parent the parent dependency
   * @param reproductionScope the reproduction scope
   * 
   * @return the created dependency
   */
  public static Dependency addDataDependencyOffspring(final Task src, final Task dest,
      final String jsonKey, final EnactmentGraph graph, final Dependency parent,
      final String reproductionScope) {
    Dependency dependency = null;
    switch (PropertyServiceDependency.getType(parent)) {
      case Data:
        dependency = PropertyServiceDependency.addDataDependency(src, dest, jsonKey, graph);
        break;
      case ControlIf:
        final boolean activation = PropertyServiceDependencyControlIf.getActivation(parent);
        dependency = PropertyServiceDependencyControlIf.addIfDependency(src, dest, jsonKey,
            activation, graph);
        break;
      default:
        throw new IllegalArgumentException(
            "Reproduction of dependencies not yet implemented for dependency type: "
                + PropertyServiceDependency.getType(parent).name());
    }
    dependency.setParent(parent);
    makeReproduced(dependency);
    setReproductionScope(dependency, reproductionScope);
    return dependency;
  }

  /**
   * Creates an offspring task for the given original.
   * 
   * @param original the original
   * @param scope the reproduction scope
   * @param offspringId the offspring ID
   * @return an offspring task for the given original.
   */
  public static Task createOffspringTask(final Task original, final String scope,
      final String offspringId) {
    final Task result = TaskPropertyService.isProcess(original) ? new Task(offspringId)
        : new Communication(offspringId);
    result.setParent(original);
    makeReproduced(result);
    setReproductionScope(result, scope);
    return result;
  }
}
