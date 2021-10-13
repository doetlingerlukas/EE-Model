package at.uibk.dps.ee.model.properties;

import java.util.ArrayList;
import java.util.List;
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
  private static final String propNameWhileRepDataRefList =
      Property.WhileRepDataReferenceList.name();
  private static final String propNameWhileRepFuncRefList =
      Property.WhileRepFunctionReferenceList.name();
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
     * Used to annotate the edge with the data node which shall be used as source
     * when the edge is replicated during the while transformation indicated in the
     * whileRepFunctionList (on the same list index)
     */
    WhileRepDataReferenceList,
    /**
     * Used to annotate the edge with the while functions which necessitate a
     * reconnection of the edge to a different source node
     */
    WhileRepFunctionReferenceList,
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
  @SuppressWarnings("unchecked")
  public static boolean isWhileAnnotated(final Dependency dependency) {
    if (isAttributeSet(dependency, propNameWhileRepDataRefList)) {
      final List<String> whileDataRefs =
          (List<String>) getAttribute(dependency, propNameWhileRepDataRefList);
      return !whileDataRefs.isEmpty();
    } else {
      return false;
    }
  }

  /**
   * Returns the list of the while data references for the given dependency.
   * 
   * @param dep the given dependency
   * @return the list of the data references for the given dependency
   */
  @SuppressWarnings("unchecked")
  public static List<String> getWhileDataReferences(final Dependency dep) {
    if (isAttributeSet(dep, propNameWhileRepDataRefList)) {
      return (List<String>) getAttribute(dep, propNameWhileRepDataRefList);
    } else {
      throw new IllegalArgumentException("No data references annotated for dependency " + dep);
    }
  }

  /**
   * Returns the list of the while function references for the given dependency.
   * 
   * @param dependency the given dependency
   * @return the list of the data references for the given dependency
   */
  @SuppressWarnings("unchecked")
  public static List<String> getWhileFuncReferences(final Dependency dependency) {
    if (isAttributeSet(dependency, propNameWhileRepFuncRefList)) {
      return (List<String>) getAttribute(dependency, propNameWhileRepFuncRefList);
    } else {
      throw new IllegalArgumentException(
          "No functions references annotated for dependency " + dependency);
    }
  }

  /**
   * Returns true iff the given dependency has an annotation for the given while
   * reference.
   * 
   * @param dependency the considered dependency
   * @param whileRef the given while reference
   * @return true iff the given dependency has an annotation for the given while
   *         reference
   */
  public static boolean isAnnotatedForGivenWhile(final Dependency dependency,
      final String whileRef) {
    return isWhileAnnotated(dependency) && getWhileFuncReferences(dependency).contains(whileRef);
  }

  /**
   * Returns the reference to the data node to which to transfer the node during
   * the transformation by the given while node.
   * 
   * @param dependency the given dependency
   * @param whileRef the while function triggering the transformation
   * @return the reference to the data node to which to transfer the node during
   *         the transformation by the given while node
   */
  public static String getDataRefForWhile(final Dependency dependency, final String whileRef) {
    if (!isAnnotatedForGivenWhile(dependency, whileRef)) {
      throw new IllegalArgumentException(
          "Dependency " + dependency + " is not while annotated for while " + whileRef);
    }
    final List<String> whileRefs = getWhileFuncReferences(dependency);
    final List<String> dataRefs = getWhileDataReferences(dependency);
    return dataRefs.get(whileRefs.indexOf(whileRef));
  }

  /**
   * Adds an annotation specifying a srs node to which this dependency is
   * transferred during the while transformation triggered by the given function.
   * 
   * @param dependency the dependency to annotate
   * @param whileDataRef the reference to the data src node
   * @param whileFuncRef the reference to the while function triggering the
   *        transformation
   */
  public static void addWhileInputReference(final Dependency dependency, final String whileDataRef,
      final String whileFuncRef) {
    addWhileDataReference(dependency, whileDataRef);
    addWhileFuncReference(dependency, whileFuncRef);
  }

  /**
   * Removes the while reference to the provided while function from the given
   * dependency
   * 
   * @param dependency the given dependency
   * @param whileFuncRef the while function whose reference is being removed
   */
  public static void removeWhileInputReference(final Dependency dependency,
      final String whileFuncRef) {
    if (!isAnnotatedForGivenWhile(dependency, whileFuncRef)) {
      throw new IllegalArgumentException("Dependency " + dependency
          + " not annotated with reference for given while " + whileFuncRef);
    }
    final List<String> refFuncListOriginal = getWhileFuncReferences(dependency);
    final List<String> refDataListOriginal = getWhileDataReferences(dependency);

    final List<String> refFuncList = new ArrayList<>(refFuncListOriginal);
    final List<String> refDataList = new ArrayList<>(refDataListOriginal);
    final int idx = refFuncList.indexOf(whileFuncRef);
    refFuncList.remove(idx);
    refDataList.remove(idx);
    dependency.setAttribute(propNameWhileRepDataRefList, refDataList);
    dependency.setAttribute(propNameWhileRepFuncRefList, refFuncList);
  }

  /**
   * Adds a while data ref to the list of the given dependency
   * 
   * @param dependency the given dependency
   * @param whileDataRef the reference to add
   */
  static void addWhileDataReference(final Dependency dependency, final String whileDataRef) {
    if (!isAttributeSet(dependency, propNameWhileRepDataRefList)) {
      dependency.setAttribute(propNameWhileRepDataRefList, new ArrayList<>());
    }
    final List<String> refList = getWhileDataReferences(dependency);
    refList.add(whileDataRef);
    dependency.setAttribute(propNameWhileRepDataRefList, refList);
  }

  /**
   * Adds a while data func to the list of the given dependency
   * 
   * @param dependency the given dependency
   * @param whileFuncRef the reference to add
   */
  static void addWhileFuncReference(final Dependency dependency, final String whileFuncRef) {
    if (!isAttributeSet(dependency, propNameWhileRepFuncRefList)) {
      dependency.setAttribute(propNameWhileRepFuncRefList, new ArrayList<>());
    }
    final List<String> refList = getWhileFuncReferences(dependency);
    refList.add(whileFuncRef);
    dependency.setAttribute(propNameWhileRepFuncRefList, refList);
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
    dependency.setAttribute(propNameWhileRepDataRefList, new ArrayList<>());
    dependency.setAttribute(propNameWhileRepFuncRefList, new ArrayList<>());
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
  static void resetTransmissionAnnotation(final Dependency dependency) {
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
  static void setTransmissionDone(final Dependency dependency, final boolean done) {
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
  static Dependency createDependency(final Task src, final Task dest, final EnactmentGraph graph) {
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
  static void checkDataDependencyEndPoints(final Task src, final Task dest) {
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
