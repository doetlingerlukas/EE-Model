package at.uibk.dps.ee.model.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import net.sf.opendse.model.Edge;
import net.sf.opendse.model.Graph;
import net.sf.opendse.model.Node;

/**
 * Parent class for all graph objects used by Apollo. Ensures the possibility of
 * concurrent access to graph elements and restricts the interfaces of the JUNG
 * graph to keep the objects manageable.
 * 
 * @author Fedor Smirnov
 *
 * @param <V> the type of graph nodes
 * @param <E> the type of graph edges
 */
public class AbstractConcurrentGraph<V extends Node, E extends Edge> extends Graph<V, E> {

  private static final long serialVersionUID = 1L;

  protected static final String excMessageWrongMethod =
      "This method is not thread-safe. Use the appropriate method from the AbstractConcurrentGraph instead.";
  protected static final String edgeString = "edge ";
  protected static final String notInGraphString = " is not in the graph";

  protected final ConcurrentHashMap<String, V> verticesConcurrent = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, E> edgesConcurrent = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, V> sources = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, V> dests = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, E>> inEdges =
      new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, E>> outEdges =
      new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<E, EdgeType> edgeTypes = new ConcurrentHashMap<>();

  // adding/removing vertices
  @Override
  public boolean addVertex(final V vertex) {
    verticesConcurrent.put(vertex.getId(), vertex);
    return super.addVertex(vertex);
  }

  @Override
  public Collection<V> getVertices() {
    return verticesConcurrent.values();
  }

  @Override
  public V getVertex(final String vertexId) {
    if (!containsVertex(vertexId)) {
      throw new IllegalStateException("Vertex " + vertexId + " not in the graph");
    }
    return verticesConcurrent.get(vertexId);
  }

  @Override
  public boolean removeVertex(final V vertex) {
    verticesConcurrent.remove(vertex.getId(), vertex);
    if (!getInEdges(vertex).isEmpty()) {
      inEdges.get(vertex.getId()).values().forEach(dep -> removeEdge(dep));
    }
    if (!getOutEdges(vertex).isEmpty()) {
      outEdges.get(vertex.getId()).values().forEach(dep -> removeEdge(dep));
    }
    inEdges.remove(vertex.getId());
    outEdges.remove(vertex.getId());
    return super.removeVertex(vertex);
  }

  /**
   * Returns true if the graph contains a vertex with the given ID.
   * 
   * @param vertexId the given ID
   * @return true if the graph contains a vertex with the given ID
   */
  public boolean containsVertex(final String vertexId) {
    return verticesConcurrent.containsKey(vertexId);
  }

  @Override
  public boolean containsVertex(final V vertex) {
    return containsVertex(vertex.getId());
  }

  // adding/removing edges
  @Override
  public boolean addEdge(final E dependency, final V src, final V dst, final EdgeType edgeType) {
    final boolean result = super.addEdge(dependency, new Pair<V>(src, dst), edgeType);
    if (!containsVertex(src.getId())) {
      addVertex(src);
    }
    if (!containsVertex(dst.getId())) {
      addVertex(dst);
    }
    edgesConcurrent.put(dependency.getId(), dependency);
    edgeTypes.put(dependency, edgeType);
    sources.put(dependency.getId(), src);
    dests.put(dependency.getId(), dst);
    addInEdge(dst, dependency);
    addOutEdge(src, dependency);
    if (edgeType.equals(EdgeType.UNDIRECTED)) {
      addInEdge(src, dependency);
      addOutEdge(dst, dependency);
    }
    return result;
  }

  @Override
  public boolean removeEdge(final E edge) {
    final V source = getSource(edge);
    final V dest = getDest(edge);
    edgesConcurrent.remove(edge.getId());
    removeInEdge(dest, edge);
    removeOutEdge(source, edge);
    if (edgeTypes.get(edge).equals(EdgeType.UNDIRECTED)) {
      removeInEdge(source, edge);
      removeOutEdge(dest, edge);
    }
    edgeTypes.remove(edge);
    sources.remove(edge.getId());
    dests.remove(edge.getId());
    return super.removeEdge(edge);
  }

  @Override
  public int getEdgeCount() {
    return edgesConcurrent.keySet().size();
  }

  @Override
  public int getVertexCount() {
    return verticesConcurrent.keySet().size();
  }

  @Override
  public EdgeType getEdgeType(final E edge) {
    return edgeTypes.get(edge);
  }

  /**
   * Returns true iff the graph contains an edge with the specified ID.
   * 
   * @param edgeId the specified ID
   * @return true iff the graph contains an edge with the specified ID
   */
  public boolean containsEdge(final String edgeId) {
    return edgesConcurrent.containsKey(edgeId);
  }

  @Override
  public boolean containsEdge(final E edge) {
    return containsEdge(edge.getId());
  }

  @Override
  public Collection<E> getEdges() {
    return edgesConcurrent.values();
  }

  @Override
  public E getEdge(final String edgeId) {
    if (!containsEdge(edgeId)) {
      throw new IllegalArgumentException(edgeString + edgeId + notInGraphString);
    }
    return edgesConcurrent.get(edgeId);
  }

  // methods for node-edge relations
  @Override
  public Collection<E> getOutEdges(final V vertex) {
    final Set<E> result = new HashSet<>();
    if (outEdges.containsKey(vertex.getId())) {
      result.addAll(outEdges.get(vertex.getId()).values());
    }
    return result;
  }

  @Override
  public Collection<E> getInEdges(final V vertex) {
    final Set<E> result = new HashSet<>();
    if (inEdges.containsKey(vertex.getId())) {
      result.addAll(inEdges.get(vertex.getId()).values());
    }
    return result;
  }

  @Override
  public V getSource(final E edge) {
    if (!containsEdge(edge.getId())) {
      throw new IllegalArgumentException(edgeString + edge + notInGraphString);
    }
    if (!sources.containsKey(edge.getId())) {
      throw new IllegalStateException("Source of " + edge.getId() + notInGraphString);
    }
    return sources.get(edge.getId());
  }

  @Override
  public V getDest(final E edge) {
    if (!containsEdge(edge.getId())) {
      throw new IllegalArgumentException(edgeString + edge + notInGraphString);
    }
    if (!containsEdge(edge.getId())) {
      throw new IllegalStateException("Dest of " + edge.getId() + notInGraphString);
    }
    return dests.get(edge.getId());
  }

  /**
   * Returns true if the two given nodes of the graph are connected
   * 
   * @param first the first node
   * @param second the second node
   * @return true if the two given nodes of the graph are connected
   */
  public boolean areNodesConnected(final V first, final V second) {
    if (!containsVertex(first.getId()) || !containsVertex(second.getId())) {
      throw new IllegalArgumentException("One of the requested end points not in the graph");
    }
    return getPredecessors(first).contains(second) || getPredecessors(second).contains(first);
  }

  @Override
  public E findEdge(final V firstNode, final V secondNode) {
    if (!areNodesConnected(firstNode, secondNode)) {
      throw new IllegalArgumentException("The given end points are not connected");
    }
    Optional<E> optE = getInEdges(firstNode).stream()
        .filter(edge -> getSource(edge).equals(secondNode)).findFirst();
    if (optE.isEmpty()) {
      optE = getOutEdges(firstNode).stream().filter(edge -> getDest(edge).equals(secondNode))
          .findFirst();
    }
    return optE.get();
  }


  // helper methods

  /**
   * Unregisters the given edge as the out edge of the given vertex
   * 
   * @param vertex the given vertex
   * @param edge the given edge
   */
  protected void removeOutEdge(final V vertex, final E edge) {
    removeIncidentEdge(vertex, edge, false);
  }

  /**
   * Unregisters the given edge as the in edge of the given vertex
   * 
   * @param vertex the given vertex
   * @param edge the given edge
   */
  protected void removeInEdge(final V vertex, final E edge) {
    removeIncidentEdge(vertex, edge, true);
  }

  /**
   * Unregisters the given edge as the in/out edge of the given vertex
   * 
   * @param vertex the given vertex
   * @param edge the given edge
   * @param inEdge true iff unregistering an in edge
   */
  protected void removeIncidentEdge(final V vertex, final E edge, final boolean inEdge) {
    final ConcurrentHashMap<String, ConcurrentHashMap<String, E>> map = inEdge ? inEdges : outEdges;
    if (!map.containsKey(vertex.getId())) {
      throw new IllegalArgumentException(
          "Task " + vertex + " has no " + (inEdge ? "inEdges" : "outEdges"));
    }
    if (!map.get(vertex.getId()).containsKey(edge.getId())) {
      throw new IllegalArgumentException(edgeString + edge + " no incident to " + vertex);
    }
    map.get(vertex.getId()).remove(edge.getId());
  }

  /**
   * Registers the given edge as an out edge of the given vertex.
   * 
   * @param vertex the given vertex
   * @param edge the given edge
   */
  protected void addOutEdge(final V vertex, final E edge) {
    addIncidentEdge(vertex, edge, false);
  }

  /**
   * Registers the given edge as an in edge of the given vertex.
   * 
   * @param vertex the given vertex
   * @param edge the given edge
   */
  protected void addInEdge(final V vertex, final E edge) {
    addIncidentEdge(vertex, edge, true);
  }

  /**
   * Registers the given edge as an in/out edge of the given vertex.
   * 
   * @param vertex the given vertex
   * @param edge the given edge
   * @param inEdge true iff registering an in edge
   */
  protected void addIncidentEdge(final V vertex, final E edge, final boolean inEdge) {
    final ConcurrentHashMap<String, ConcurrentHashMap<String, E>> map = inEdge ? inEdges : outEdges;
    if (!map.containsKey(vertex.getId())) {
      map.put(vertex.getId(), new ConcurrentHashMap<String, E>());
    }
    map.get(vertex.getId()).put(edge.getId(), edge);
  }

  // restricting access to unused methods exposed by the JUNG parent
  @Override
  public V getVertex(final V vertex) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public E getEdge(final E edge) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(final E edge, final V vertex1, final V vertex2) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(final E edge, final Collection<? extends V> vertices) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(final E edge, final Collection<? extends V> vertices,
      final EdgeType edgeType) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(final E edge, final Pair<? extends V> endpoints) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(final E edge, final Pair<? extends V> endpoints, final EdgeType edgeType) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public Pair<V> getEndpoints(final E edge) {
    if (!containsEdge(edge.getId())) {
      throw new IllegalArgumentException("Edge " + edge + notInGraphString);
    }
    final V first = getSource(edge);
    final V second = getDest(edge);
    return new Pair<V>(first, second);
  }

  @Override
  public Collection<E> getIncidentEdges(final V vertex) {
    if (!containsVertex(vertex.getId())) {
      throw new IllegalArgumentException("Vertex " + vertex.getId() + notInGraphString);
    }
    final Set<E> result = new HashSet<>(getInEdges(vertex));
    result.addAll(getOutEdges(vertex));
    return result;
  }

  @Override
  public Collection<V> getIncidentVertices(final E edge) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public Collection<V> getNeighbors(final V vertex) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public V getOpposite(final V vertex, final E edge) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public Collection<V> getPredecessors(final V vertex) {
    return getInEdges(vertex).stream().map(edge -> getSource(edge)).collect(Collectors.toSet());
  }

  @Override
  public Collection<V> getSuccessors(final V vertex) {
    return getOutEdges(vertex).stream().map(edge -> getDest(edge)).collect(Collectors.toSet());
  }
}
