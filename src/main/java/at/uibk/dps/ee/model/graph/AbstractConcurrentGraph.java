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

  protected final String excMessageWrongMethod =
      "This method is not thread-safe. Use the appropriate method from the AbstractConcurrentGraph instead.";

  protected final ConcurrentHashMap<String, V> vertices = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, E> edges = new ConcurrentHashMap<>();
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
    vertices.put(vertex.getId(), vertex);
    return super.addVertex(vertex);
  }

  @Override
  public Collection<V> getVertices() {
    return vertices.values();
  }

  @Override
  public V getVertex(String id) {
    if (!containsVertex(id)) {
      throw new IllegalStateException("Vertex " + id + " not in the graph");
    }
    return vertices.get(id);
  }

  @Override
  public boolean removeVertex(final V vertex) {
    vertices.remove(vertex.getId(), vertex);
    if (getInEdges(vertex).size() > 0) {
      inEdges.get(vertex.getId()).values().forEach(dep -> removeEdge(dep));
    }
    if (getOutEdges(vertex).size() > 0) {
      outEdges.get(vertex.getId()).values().forEach(dep -> removeEdge(dep));
    }
    inEdges.remove(vertex.getId());
    outEdges.remove(vertex.getId());
    return super.removeVertex(vertex);
  }

  public boolean containsVertex(String vertexId) {
    return vertices.containsKey(vertexId);
  }

  @Override
  public boolean containsVertex(V vertex) {
    return containsVertex(vertex.getId());
  }

  // adding/removing edges
  @Override
  public boolean addEdge(E dependency, V src, V dst, EdgeType edgeType) {
    boolean result = super.addEdge(dependency, new Pair<V>(src, dst), edgeType);
    edges.put(dependency.getId(), dependency);
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
  public boolean removeEdge(E edge) {
    V source = getSource(edge);
    V dest = getDest(edge);
    edges.remove(edge.getId());
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
  public EdgeType getEdgeType(E edge) {
    return edgeTypes.get(edge);
  }

  public boolean containsEdge(String edgeId) {
    return edges.containsKey(edgeId);
  }

  @Override
  public boolean containsEdge(E edge) {
    return containsEdge(edge.getId());
  }

  @Override
  public Collection<E> getEdges() {
    return edges.values();
  }

  @Override
  public E getEdge(String id) {
    if (!containsEdge(id)) {
      throw new IllegalArgumentException("Edge " + id + " is not in the graph.");
    }
    return edges.get(id);
  }

  // methods for node-edge relations
  @Override
  public Collection<E> getOutEdges(V vertex) {
    Set<E> result = new HashSet<>();
    if (outEdges.containsKey(vertex.getId())) {
      result.addAll(outEdges.get(vertex.getId()).values());
    }
    return result;
  }

  @Override
  public Collection<E> getInEdges(V vertex) {
    Set<E> result = new HashSet<>();
    if (inEdges.containsKey(vertex.getId())) {
      result.addAll(inEdges.get(vertex.getId()).values());
    }
    return result;
  }

  @Override
  public V getSource(E edge) {
    if (!containsEdge(edge.getId())) {
      throw new IllegalArgumentException("Edge " + edge + " not in graph.");
    }
    if (!sources.containsKey(edge.getId())) {
      throw new IllegalStateException("Source of edge " + edge.getId() + " not in the graph");
    }
    return sources.get(edge.getId());
  }

  @Override
  public V getDest(E edge) {
    if (!containsEdge(edge.getId())) {
      throw new IllegalArgumentException("Edge " + edge + " not in graph.");
    }
    if (!containsEdge(edge.getId())) {
      throw new IllegalStateException("Dest of edge " + edge.getId() + " not in the graph");
    }
    return dests.get(edge.getId());
  }

  public boolean areNodesConnected(V first, V second) {
    if (!containsVertex(first.getId()) || !containsVertex(second.getId())) {
      throw new IllegalArgumentException("One of the requested end points not in the graph");
    }
    return getPredecessors(first).contains(second) || getPredecessors(second).contains(first);
  }

  @Override
  public E findEdge(V v1, V v2) {
    if (!areNodesConnected(v1, v2)) {
      throw new IllegalArgumentException("The given end points are not connected");
    }
    Optional<E> optE = getInEdges(v1).stream().filter(edge -> getSource(edge).equals(v2)).findFirst();
    if (optE.isEmpty()) {
      optE = getOutEdges(v1).stream().filter(edge -> getDest(edge).equals(v2)).findFirst();
    }
    return optE.get();
  }


  // helper methods
  protected void removeOutEdge(V task, E edge) {
    removeIncidentEdge(task, edge, false);
  }

  protected void removeInEdge(V task, E edge) {
    removeIncidentEdge(task, edge, true);
  }

  protected void removeIncidentEdge(V task, E edge, boolean inEdge) {
    ConcurrentHashMap<String, ConcurrentHashMap<String, E>> map = inEdge ? inEdges : outEdges;
    if (!map.containsKey(task.getId())) {
      throw new IllegalArgumentException(
          "Task " + task + " has no " + (inEdge ? "inEdges" : "outEdges"));
    }
    if (!map.get(task.getId()).containsKey(edge.getId())) {
      throw new IllegalArgumentException("Edge " + edge + " no incident to " + task);
    }
    map.get(task.getId()).remove(edge.getId());
  }

  protected void addOutEdge(V task, E edge) {
    addIncidentEdge(task, edge, false);
  }

  protected void addInEdge(V task, E edge) {
    addIncidentEdge(task, edge, true);
  }

  protected void addIncidentEdge(V task, E edge, boolean inEdge) {
    ConcurrentHashMap<String, ConcurrentHashMap<String, E>> map = inEdge ? inEdges : outEdges;
    if (!map.containsKey(task.getId())) {
      map.put(task.getId(), new ConcurrentHashMap<String, E>());
    }
    map.get(task.getId()).put(edge.getId(), edge);
  }

  // restricting access to unused methods exposed by the JUNG parent
  @Override
  public V getVertex(V v) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public E getEdge(E e) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(E e, V v1, V v2) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(E edge, Collection<? extends V> vertices) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(E edge, Collection<? extends V> vertices, EdgeType edgeType) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(E edge, Pair<? extends V> endpoints) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public boolean addEdge(E edge, Pair<? extends V> endpoints, EdgeType edgeType) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public Pair<V> getEndpoints(E edge) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public Collection<E> getIncidentEdges(V vertex) {
    if (!containsVertex(vertex.getId())) {
      throw new IllegalArgumentException("Vertex " + vertex.getId() + " is not in the graph.");
    }
    Set<E> result = new HashSet<>(getInEdges(vertex));
    result.addAll(getOutEdges(vertex));
    return result;
  }

  @Override
  public Collection<V> getIncidentVertices(E edge) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public Collection<V> getNeighbors(V vertex) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public V getOpposite(V vertex, E edge) {
    throw new IllegalAccessError(excMessageWrongMethod);
  }

  @Override
  public Collection<V> getPredecessors(V vertex) {
    return getInEdges(vertex).stream().map(edge -> getSource(edge)).collect(Collectors.toSet());
  }

  @Override
  public Collection<V> getSuccessors(V vertex) {
    return getOutEdges(vertex).stream().map(edge -> getDest(edge)).collect(Collectors.toSet());
  }
}
