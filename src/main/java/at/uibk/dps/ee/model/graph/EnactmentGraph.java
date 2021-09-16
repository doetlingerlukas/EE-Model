package at.uibk.dps.ee.model.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import net.sf.opendse.model.Application;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

/**
 * The {@link EnactmentGraph} models the data and the control flow of the
 * workflow to enact.
 * 
 * @author Fedor Smirnov
 */
public class EnactmentGraph extends Application<Task, Dependency> {

  private static final long serialVersionUID = 1L;

  protected final String excMessageWrongMethod =
      "This method is not thread-safe. Use the appropriate method from the EnactmentGraph instead.";

  protected final ConcurrentHashMap<String, Task> vertices = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, Dependency> edges = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, Task> sources = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, Task> dests = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, Dependency>> inEdges =
      new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, Dependency>> outEdges =
      new ConcurrentHashMap<>();

  /**
   * Default constructor.
   */
  public EnactmentGraph() {
    super();
  }

  /**
   * Constructor to build an {@link EnactmentGraph} out of an application.
   * 
   * @param application the given application
   */
  public EnactmentGraph(final Application<Task, Dependency> application) {
    super();
    application.getEdges().forEach(edge -> {
      final Task src = application.getSource(edge);
      final Task dst = application.getDest(edge);
      this.addEdge(edge, src, dst, EdgeType.DIRECTED);
    });
    application.forEach(task -> this.addVertex(task));
  }

  @Override
  public Collection<Dependency> getOutEdges(Task vertex) {
    Set<Dependency> result = new HashSet<>();
    if (outEdges.containsKey(vertex.getId())) {
      result.addAll(outEdges.get(vertex.getId()).values());
    }
    return result;
  }

  @Override
  public Collection<Dependency> getInEdges(Task vertex) {
    Set<Dependency> result = new HashSet<>();
    if (inEdges.containsKey(vertex.getId())) {
      result.addAll(inEdges.get(vertex.getId()).values());
    }
    return result;
  }

  @Override
  public Task getSource(Dependency edge) {
    if (!containsEdgeWithId(edge.getId())) {
      throw new IllegalArgumentException("Edge " + edge + " not in graph.");
    }
    if (!sources.containsKey(edge.getId())) {
      throw new IllegalStateException("Source of edge " + edge.getId() + " not in the graph");
    }
    return sources.get(edge.getId());
  }

  @Override
  public Task getDest(Dependency edge) {
    if (!containsEdgeWithId(edge.getId())) {
      throw new IllegalArgumentException("Edge " + edge + " not in graph.");
    }
    if (!containsEdgeWithId(edge.getId())) {
      throw new IllegalStateException("Dest of edge " + edge.getId() + " not in the graph");
    }
    return dests.get(edge.getId());
  }

  @Override
  public boolean addEdge(Dependency dependency, Pair<? extends Task> endpoints,
      EdgeType edgetType) {
    boolean result = super.addEdge(dependency, endpoints, edgetType);
    edges.put(dependency.getId(), dependency);
    Task source = endpoints.getFirst();
    Task dest = endpoints.getSecond();
    sources.put(dependency.getId(), source);
    dests.put(dependency.getId(), dest);
    addInEdge(dest, dependency);
    addOutEdge(source, dependency);
    return result;
  }

  @Override
  public boolean removeEdge(Dependency edge) {
    Task source = getSource(edge);
    Task dest = getDest(edge);
    edges.remove(edge.getId());
    removeInEdge(dest, edge);
    removeOutEdge(source, edge);
    sources.remove(edge.getId());
    dests.remove(edge.getId());
    return super.removeEdge(edge);
  }

  @Override
  public boolean addVertex(final Task vertex) {
    vertices.put(vertex.getId(), vertex);
    return super.addVertex(vertex);
  }

  @Override
  public Collection<Task> getVertices() {
    return vertices.values();
  }

  @Override
  public boolean removeVertex(final Task vertex) {
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

  public boolean containsEdgeWithId(String edgeId) {
    return edges.containsKey(edgeId);
  }


  @Override
  public Dependency getEdge(String id) {
    if (!edges.containsKey(id)) {
      throw new IllegalArgumentException("Edge " + id + " not in the enactment graph");
    }
    return edges.get(id);
  }
  
  /**
   * Do not use this method.
   */
  @Override
  public Dependency getEdge(Dependency e) {
    throw new IllegalStateException(excMessageWrongMethod);
  }



  /**
   * Thread-safe method to get a task for a given id.
   * 
   * @param taskId the task id
   * @return the task with the required id
   */
  public Task getTask(final String taskId) {
    if (!containsVertexWithId(taskId)) {
      throw new IllegalStateException("Vertex " + taskId + " not in the enactment graph");
    }
    return vertices.get(taskId);
  }

  public boolean containsVertexWithId(String vertexId) {
    return vertices.containsKey(vertexId);
  }

  protected void removeOutEdge(Task task, Dependency edge) {
    removeIncidentEdge(task, edge, false);
  }

  protected void removeInEdge(Task task, Dependency edge) {
    removeIncidentEdge(task, edge, true);
  }

  protected void removeIncidentEdge(Task task, Dependency edge, boolean inEdge) {
    ConcurrentHashMap<String, ConcurrentHashMap<String, Dependency>> map =
        inEdge ? inEdges : outEdges;
    if (!map.containsKey(task.getId())) {
      throw new IllegalArgumentException(
          "Task " + task + " has no " + (inEdge ? "inEdges" : "outEdges"));
    }
    if (!map.get(task.getId()).containsKey(edge.getId())) {
      throw new IllegalArgumentException("Edge " + edge + " no incident to " + task);
    }
    map.get(task.getId()).remove(edge.getId());
  }

  protected void addOutEdge(Task task, Dependency edge) {
    addIncidentEdge(task, edge, false);
  }

  protected void addInEdge(Task task, Dependency edge) {
    addIncidentEdge(task, edge, true);
  }

  protected void addIncidentEdge(Task task, Dependency edge, boolean inEdge) {
    ConcurrentHashMap<String, ConcurrentHashMap<String, Dependency>> map =
        inEdge ? inEdges : outEdges;
    if (!map.containsKey(task.getId())) {
      map.put(task.getId(), new ConcurrentHashMap<String, Dependency>());
    }
    map.get(task.getId()).put(edge.getId(), edge);
  }
}
