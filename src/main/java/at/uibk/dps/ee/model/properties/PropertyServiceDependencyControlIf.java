package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceDependency.TypeDependency;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container for accessing the properties of the control
 * depedencies modeling the implications of if compounds.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceDependencyControlIf extends AbstractPropertyService {

  /**
   * No constructor
   */
  private PropertyServiceDependencyControlIf() {}

  /**
   * Properties defining the attribute names.
   * 
   * @author Fedor Smirnov
   */
  protected enum Property {
    /**
     * Boolean value: True => edge is active when condition variable is true; False
     * => edge is active when condition variable is false.
     */
    Activation
  }

  /**
   * Adds an if dependency between the provided end nodes to the given graph and
   * annotates it with the given activation bool.
   * 
   * @param src the src node
   * @param dst the dst node
   * @param jsonKey the jsonkey
   * @param activation the activation to annotate
   * @param graph the enactment graph
   * @return the created {@link Dependency}
   */
  public static Dependency addIfDependency(final Task src, final Task dst, final String jsonKey,
      final boolean activation, final EnactmentGraph graph) {
    final Dependency toAdd = createControlIfDependency(src, dst, jsonKey, activation, graph);
    graph.addEdge(toAdd, src, dst, EdgeType.DIRECTED);
    return toAdd;
  }

  /**
   * Creates a control if dependency which will be used to connect the given src
   * to the given dest and annotates it with the given activation.
   * 
   * @param src the src node
   * @param dest the dest node
   * @param activation the activation bool
   * @return a control if dependency which will be used to connect the given src
   *         to the given dest and annotates it with the given activation
   */
  static Dependency createControlIfDependency(final Task src, final Task dest, final String jsonKey,
      final boolean activation, final EnactmentGraph graph) {
    final Dependency result = PropertyServiceDependency.createDependency(src, dest, graph);
    PropertyServiceDependency.setType(result, TypeDependency.ControlIf);
    PropertyServiceDependencyControlIf.setActivation(result, activation);
    PropertyServiceDependency.setJsonKey(result, jsonKey);
    return result;
  }

  /**
   * Returns the activation of the provided dependency.
   * 
   * @param dependency the provided dependency.
   * @return the activation of the provided dependency
   */
  public static boolean getActivation(final Dependency dependency) {
    checkDependency(dependency);
    final String attrName = Property.Activation.name();
    return (boolean) getAttribute(dependency, attrName);
  }

  /**
   * Sets the activation for the provided dependency.
   * 
   * @param dependency the provided dependency
   * @param activation the activation to set
   */
  public static void setActivation(final Dependency dependency, final boolean activation) {
    checkDependency(dependency);
    final String attrName = Property.Activation.name();
    dependency.setAttribute(attrName, activation);
  }

  /**
   * Checks the provided {@link Dependency}. Throws an exception if it does not
   * model an if control flow.
   * 
   * @param dependency the dependency to check.
   */
  static void checkDependency(final Dependency dependency) {
    if (!PropertyServiceDependency.getType(dependency).equals(TypeDependency.ControlIf)) {
      throw new IllegalArgumentException(
          "The dependency " + dependency.getId() + " does not model a control if implication.");
    }
  }
}
