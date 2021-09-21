package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container to access the attributes of the enactment
 * {@link Dependency}s.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceDependency extends AbstractPropertyService {

  private static final String propNameExtractionDone = Property.ExtractionDone.name();
  private static final String propNameDataConsumed = Property.DataConsumed.name();
  private static final String propNameWhileRepReference = Property.WhileRepDataReference.name();
  private static final String propNameWhileFuncReference =
      Property.WhileRepWhileFuncReference.name();
  private static final String propNamePrevWhileRef = Property.PreviousWhileIteration.name();

  private PropertyServiceDependency() {}

  /**
   * The properties annotated on dependency edges.
   * 
   * @author Fedor Smirnov
   *
   */
  public enum Property {
    /**
     * General type of dependency
     */
    Type,
    /**
     * The json key used by the Function endpoint of the edge
     */
    JsonKey,
    /**
     * Whether or not the data was already transmitted over this edge
     */
    TransmissionDone,
    /**
     * Whether the data transmitted by this edge was already consumed by its
     * destination
     */
    DataConsumed,
    /**
     * Whether or not the data was already extracted from the edge's source
     */
    ExtractionDone,
    /**
     * Used to annotate an edge which shall be used as source when the edge is
     * replicated during a while transformation
     */
    WhileRepDataReference,
    /**
     * The reference for the while function for whose transformation the data
     * dependency modeled by the edge varies
     */
    WhileRepWhileFuncReference,
    /**
     * Annotation for edges which point to previous while itearations.
     */
    PreviousWhileIteration
  }

  /**
   * Different types of dependencies.
   * 
   * @author Fedor Smirnov
   *
   */
  public enum TypeDependency {
    /**
     * Data flow
     */
    Data,
    /**
     * Control flow following from if compounds
     */
    ControlIf
  }

  /**
   * Returns true iff the dependency points to data produced in the previous
   * iteration.
   * 
   * @param dependency the given dependency
   * @return true iff the dependency points to data produced in the previous
   *         iteration
   */
  public static boolean doesPointToPreviousIteration(final Dependency dependency) {
    if (!isAttributeSet(dependency, propNamePrevWhileRef)) {
      return false;
    }
    return (boolean) dependency.getAttribute(propNamePrevWhileRef);
  }

  /**
   * Annotate that a dependency refers to a previous while iteration
   * 
   * @param dependency the given dependency
   */
  public static void annotatePreviousIterationDependency(final Dependency dependency) {
    dependency.setAttribute(propNamePrevWhileRef, true);
  }

  /**
   * Returns true iff the given edge is annotated with a replica source
   * 
   * @param dependency the edge to check
   * @return true iff the given edge is annotated with a replica source
   */
  public static boolean isWhileAnnotated(final Dependency dependency) {
    return isAttributeSet(dependency, propNameWhileRepReference);
  }

  /**
   * Annotates that the given node shall be used as a source of the replica of the
   * given edge
   * 
   * @param dependency the given edge
   * @param data the node which shall be used as a src when the dependency is
   *        replicated
   */
  public static void annotateWhileReplica(final Dependency dependency, final Task data,
      final String whileFuncName) {
    if (!TaskPropertyService.isCommunication(data)) {
      throw new IllegalArgumentException(
          "Only a data node can be a replica src, task " + data + " is not a data node.");
    }
    if (isWhileAnnotated(dependency)) {
      throw new IllegalArgumentException(
          "Dependency edge " + dependency.getId() + " already while-annotated.");
    }
    dependency.setAttribute(propNameWhileRepReference, data.getId());
    dependency.setAttribute(propNameWhileFuncReference, whileFuncName);
  }

  /**
   * Resets the while annotation for a given dependency.
   * 
   * @param dependency the given dependency
   */
  public static void resetWhileAnnotation(final Dependency dependency) {
    if (!isWhileAnnotated(dependency)) {
      throw new IllegalArgumentException(
          "Dependency edge " + dependency.getId() + " not while-annotated, so no reset.");
    }
    dependency.setAttribute(propNameWhileRepReference, null);
    dependency.setAttribute(propNameWhileFuncReference, null);
  }

  /**
   * Returns a reference to the function for whose transformation the while
   * function is relevant.
   * 
   * @param dependency the annotated edge
   * @return reference to a while function
   */
  public static String getReplicaWhileFuncReference(final Dependency dependency) {
    if (!isWhileAnnotated(dependency)) {
      throw new IllegalArgumentException("Dependency " + dependency + " is not while annotated.");
    }
    return (String) getAttribute(dependency, propNameWhileFuncReference);
  }

  /**
   * Returns the reference to the node which is used as source for the replicas of
   * the given dependency.
   * 
   * @param dependency the given dependency
   * @return the reference to the node which is used as source for the replicas of
   *         the given dependency
   */
  public static String getReplicaSrcReference(final Dependency dependency) {
    if (!isWhileAnnotated(dependency)) {
      throw new IllegalArgumentException("Dependency " + dependency + " is not while annotated.");
    }
    return (String) getAttribute(dependency, propNameWhileRepReference);
  }

  /**
   * Resets the transmission annotations on the given edge
   * 
   * @param dependency the given edge
   */
  public static void resetTransmission(final Dependency dependency) {
    resetTransmissionAnnotation(dependency);
    dependency.setAttribute(propNameDataConsumed, false);
  }

  /**
   * Annotates data consumption on the given edge
   * 
   * @param dependency the given edge
   */
  public static void setDataConsumed(final Dependency dependency) {
    if (!isTransmissionDone(dependency)) {
      throw new IllegalStateException(
          "Data consumption can only occur after transmission: " + dependency);
    }
    dependency.setAttribute(propNameDataConsumed, true);
  }

  /**
   * Returns true if the edge annotation suggests that the data has been consumed
   * by the edge source.
   * 
   * @param dependency the edge
   * @return true if the edge annotation suggests that the data has been consumed
   *         by the edge source
   */
  public static boolean isDataConsumed(final Dependency dependency) {
    if (!isAttributeSet(dependency, propNameDataConsumed)) {
      return false;
    }
    return (boolean) getAttribute(dependency, propNameDataConsumed);
  }

  /**
   * Returns true iff the data extraction over the given edge is finished.
   * 
   * @param dependency the given edge
   * @return true iff the data extraction over the given edge is finished
   */
  public static boolean isExtractionDone(final Dependency dependency) {
    if (!isAttributeSet(dependency, propNameExtractionDone)) {
      return false;
    }
    return (boolean) getAttribute(dependency, propNameExtractionDone);
  }

  /**
   * Annotates that the extraction over the given edge has been finished.
   * 
   * @param dependency the given edge
   */
  public static void setExtractionDone(final Dependency dependency) {
    dependency.setAttribute(propNameExtractionDone, true);
  }

  /**
   * Annotates that the extraction over the given edge has not been finished.
   * 
   * @param dependency the given edge
   */
  public static void resetExtractionDone(final Dependency dependency) {
    dependency.setAttribute(propNameExtractionDone, false);
  }

  /**
   * Returns true if the data transmission over the edge has occurred.
   * 
   * @param dependency the dependency edge
   * @return true if the data transmission over the edge has occurred
   */
  public static boolean isTransmissionDone(final Dependency dependency) {
    final String attrName = Property.TransmissionDone.name();
    if (!isAttributeSet(dependency, attrName)) {
      return false;
    }
    return (boolean) getAttribute(dependency, attrName);
  }

  /**
   * Resets the transmission annotation.
   * 
   * @param dependency the given dependency
   */
  protected static void resetTransmissionAnnotation(final Dependency dependency) {
    setTransmissionDone(dependency, false);
  }

  /**
   * Annotates that the data transmission over the given dependency has already
   * occurred.
   * 
   * @param dependency the given dependency
   */
  public static void annotateFinishedTransmission(final Dependency dependency) {
    setTransmissionDone(dependency, true);
  }

  /**
   * Annotates whether the transmission on the given edge has already occurred.
   * 
   * @param dependency the given edge
   * @param done true: the transmission is done, false: the transmission is not
   *        done
   */
  protected static void setTransmissionDone(final Dependency dependency, final boolean done) {
    final String attrName = Property.TransmissionDone.name();
    dependency.setAttribute(attrName, done);
  }

  /**
   * Returns a dependency with a unique ID made from the IDs of its endpoints.
   * 
   * @param src the edge source
   * @param dest the edge destination
   * @return a dependency with a unique ID made from the IDs of its endpoints
   */
  protected static Dependency createDependency(final Task src, final Task dest,
      final EnactmentGraph graph) {
    String dependencyId = src.getId() + ConstantsEEModel.KeywordSeparator1 + dest.getId();
    while (graph.containsEdge(dependencyId)) {
      dependencyId = dependencyId.concat(ConstantsEEModel.KeyWordEdgeUniqueness);
    }
    return new Dependency(dependencyId);
  }

  /**
   * Creates and adds a data dependency to connect the provided nodes. Returns the
   * created dependency.
   * 
   * @param src the source node
   * @param dest the destination node
   * @param jsonKey the key used (by the function node end point) to refer to the
   *        data content (of the data node end point) by the function node end
   *        point.
   * @param graph the enactment graph
   * @return the created dependency
   */
  public static Dependency addDataDependency(final Task src, final Task dest, final String jsonKey,
      final EnactmentGraph graph) {
    checkDataDependencyEndPoints(src, dest);
    final Dependency dependency = createDependency(src, dest, graph);
    setType(dependency, TypeDependency.Data);
    setJsonKey(dependency, jsonKey);
    graph.addEdge(dependency, src, dest, EdgeType.DIRECTED);
    return dependency;
  }

  /**
   * Checks that we have a data AND a function node as end points, throws an
   * exception otherwise.
   * 
   * @param src the src node
   * @param dest the dest node
   */
  protected static void checkDataDependencyEndPoints(final Task src, final Task dest) {
    final boolean functionPresent =
        TaskPropertyService.isProcess(src) || TaskPropertyService.isProcess(dest);
    final boolean dataPresent =
        TaskPropertyService.isCommunication(src) || TaskPropertyService.isCommunication(dest);
    if (!(functionPresent && dataPresent)) {
      throw new IllegalArgumentException("Dependency end points " + src.getId() + " and "
          + dest.getId() + " are not a function AND a data node.");
    }
  }

  /**
   * Returns the json key annotated at the given dependency.
   * 
   * @param dep the given dependency
   * @return the json key annotated at the given dependency
   */
  public static String getJsonKey(final Dependency dep) {
    final String attrName = Property.JsonKey.name();
    return (String) getAttribute(dep, attrName);
  }

  /**
   * Sets the json key of the provided dependency
   * 
   * @param dep the provided dependency
   * @param jsonKey the json key to set
   */
  public static void setJsonKey(final Dependency dep, final String jsonKey) {
    final String attrName = Property.JsonKey.name();
    dep.setAttribute(attrName, jsonKey);
  }

  /**
   * Returns the type of the given dependency.
   * 
   * @param dep the given dependency
   */
  public static TypeDependency getType(final Dependency dep) {
    final String attrName = Property.Type.name();
    return TypeDependency.valueOf((String) getAttribute(dep, attrName));
  }

  /**
   * Sets the type of the given dependency.
   * 
   * @param dep the given dependency
   * @param type the type to set
   */
  public static void setType(final Dependency dep, final TypeDependency type) {
    final String attrName = Property.Type.name();
    dep.setAttribute(attrName, type.name());
  }
}
