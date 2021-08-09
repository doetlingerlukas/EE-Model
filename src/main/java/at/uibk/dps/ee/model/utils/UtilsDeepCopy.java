package at.uibk.dps.ee.model.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.graph.EnactmentSpecification;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Element;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static container for convenience methods to generate deep copies (no relation
 * between original and copy!) of specifications and its components.
 * 
 * @author Fedor Smirnov
 */
public final class UtilsDeepCopy {

  /**
   * No constructor
   */
  private UtilsDeepCopy() {}

  /**
   * Generates a deep copy of the provided spec (objects on all levels have
   * different references, but identical attributes and relations).
   * 
   * @param original the specification which is being copied
   * @return the deep copy of the specification
   */
  public static EnactmentSpecification deepCopySpec(EnactmentSpecification original) {
    EnactmentGraph deepCopyEGraph = deepCopyEGraph(original.getEnactmentGraph());
    ResourceGraph deepCopyRGraph = deepCopyRGraph(original.getResourceGraph());
    Mappings<Task, Resource> deepCopyMappings =
        deepCopyMappings(original.getMappings(), deepCopyEGraph, deepCopyRGraph);
    return new EnactmentSpecification(deepCopyEGraph, deepCopyRGraph, deepCopyMappings);
  }

  /**
   * Generates a deep copy of the provided enactment graph (objects on all levels
   * have different references, but identical attributes and relations).
   * 
   * @param original the enactment graph which is being copied
   * @return the deep copy of the enactment graph
   */
  public static EnactmentGraph deepCopyEGraph(EnactmentGraph original) {
    EnactmentGraph result = new EnactmentGraph();
    original.getVertices()
        .forEach(originalNode -> result.addVertex(deepCopyEGraphNode(originalNode)));
    original.getEdges()
        .forEach(originalEdge -> addDeepCopyDependency(originalEdge, original, result));
    return result;
  }

  /**
   * Generates a deep copy of the provided resource graph (objects on all levels
   * have different references, but identical attributes and relations).
   * 
   * @param original the resource graph which is being copied
   * @return the deep copy of the resource graph
   */
  public static ResourceGraph deepCopyRGraph(ResourceGraph original) {
    ResourceGraph result = new ResourceGraph();
    original.getVertices().forEach(originalRes -> result.addVertex(deepCopyResource(originalRes)));
    original.getEdges().forEach(originalLink -> addDeepCopyLink(originalLink, original, result));
    return result;
  }

  /**
   * Generates a deep copy of the provided mappings (objects on all levels have
   * different references, but identical attributes and relations).
   * 
   * @param original the original mappings
   * @param deepCopyEGraph a deep copy of the enactment graph
   * @param deepCopyRGraph a deep copy of the resource graph
   * @return a deep copy of the mappings
   */
  public static Mappings<Task, Resource> deepCopyMappings(Mappings<Task, Resource> original,
      EnactmentGraph deepCopyEGraph, ResourceGraph deepCopyRGraph) {
    Mappings<Task, Resource> result = new Mappings<>();
    original.getAll().forEach(originalMapping -> result
        .add(deepCopyMapping(originalMapping, deepCopyEGraph, deepCopyRGraph)));
    return result;
  }

  /**
   * Method to create deep copies of a task or a communication
   * 
   * @param original the original egraph node
   * @return the deep copy
   */
  public static Task deepCopyEGraphNode(Task original) {
    return TaskPropertyService.isProcess(original) ? deepCopyTask(original)
        : deepCopyCommunication(original);
  }

  /**
   * Creates a deep copy of a task.
   * 
   * @param original the original task
   * @return the deep copy of the task
   */
  public static Task deepCopyTask(Task original) {
    return deepCopyElement(Task.class, original);
  }

  /**
   * Creates a deep copy of a communication
   * 
   * @param original the original communication
   * @return the deep copy of the communication
   */
  public static Communication deepCopyCommunication(Task original) {
    return deepCopyElement(Communication.class, original);
  }

  /**
   * Creates a deep copy of the given resource.
   * 
   * @param original the original resource
   * @return a deep copy of the original resource
   */
  public static Resource deepCopyResource(Resource original) {
    return deepCopyElement(Resource.class, original);
  }

  /**
   * Make a deep copy the provided element of the provided type.
   * 
   * @param <E> the type of the processed element
   * @param clazz the provided type
   * @param original the original element (of type E)
   * @return a deep copy the provided element of the provided type
   */
  protected static <E extends Element> E deepCopyElement(Class<E> clazz, Element original) {
    String id = original.getId();
    try {
      E result = clazz.getDeclaredConstructor(String.class).newInstance(id);
      original.getAttributeNames()
          .forEach(attrName -> result.setAttribute(attrName, original.getAttribute(attrName)));
      return result;
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      throw new IllegalArgumentException(
          "Error when trying to construct an element of type " + clazz.getCanonicalName());
    }
  }

  /**
   * Creates a deep copy of the given original mapping.
   * 
   * @param original the original mapping
   * @param copyEGraph copy of the enactment graph
   * @param copyRGraph copy of the resource graph
   * @return deep copy of the given mapping
   */
  public static Mapping<Task, Resource> deepCopyMapping(Mapping<Task, Resource> original,
      EnactmentGraph copyEGraph, ResourceGraph copyRGraph) {
    Task taskCopy = Optional.of(copyEGraph.getVertex(original.getSource().getId()))
        .orElseThrow(() -> new IllegalStateException(
            "Src of mapping " + original.getId() + " not in the copied e graph."));
    Resource resCopy = Optional.of(copyRGraph.getVertex(original.getTarget().getId()))
        .orElseThrow(() -> new IllegalStateException(
            "Target of mapping " + original.getId() + " not in the copied r graph."));
    Mapping<Task, Resource> result =
        new Mapping<Task, Resource>(original.getId(), taskCopy, resCopy);
    original.getAttributeNames()
        .forEach(attrName -> result.setAttribute(attrName, original.getAttribute(attrName)));
    return result;
  }

  /**
   * Creates a deep copy of the given dependency and adds it at the appropriate
   * position of the copied enactment graph.
   * 
   * @param original the original dependency
   * @param originalGraph the original egraph
   * @param copyEGraph the copied egraph
   * @return the deep copy of the dependency (added to the copy graph by this
   *         method)
   */
  public static Dependency addDeepCopyDependency(Dependency original, EnactmentGraph originalGraph,
      EnactmentGraph copyEGraph) {
    Dependency result = deepCopyElement(Dependency.class, original);
    Task srcTask = Optional.of(copyEGraph.getVertex(originalGraph.getSource(original).getId()))
        .orElseThrow(() -> new IllegalStateException(
            "Src of edge " + original + " not in the copied e graph"));
    Task dstTask = Optional.of(copyEGraph.getVertex(originalGraph.getDest(original).getId()))
        .orElseThrow(() -> new IllegalStateException(
            "Dst of edge " + original + " not in the copied e graph"));
    copyEGraph.addEdge(result, srcTask, dstTask, EdgeType.DIRECTED);
    return result;
  }

  /**
   * Creates a deep copy of the given dependency and adds it at the appropriate
   * position of the copied resource graph.
   * 
   * @param original the original link
   * @param originalGraph the original resource graph
   * @param copyGraph the copied resource graph
   * @return the deep copy of the link (added to the copy graph by this method)
   */
  public static Link addDeepCopyLink(Link original, ResourceGraph originalGraph,
      ResourceGraph copyGraph) {
    Link result = deepCopyElement(Link.class, original);
    Resource srcRes =
        Optional.of(copyGraph.getVertex(originalGraph.getEndpoints(original).getFirst().getId()))
            .orElseThrow(() -> new IllegalStateException(
                "Src of link " + original + " not in the copied r graph"));
    Resource dstRes =
        Optional.of(copyGraph.getVertex(originalGraph.getEndpoints(original).getSecond().getId()))
            .orElseThrow(() -> new IllegalStateException(
                "Dst of link " + original + " not in the copied r graph"));
    copyGraph.addEdge(result, srcRes, dstRes, EdgeType.UNDIRECTED);
    return result;
  }
}
