package at.uibk.dps.ee.model.graph;

import java.util.concurrent.ConcurrentHashMap;
import edu.uci.ics.jung.graph.util.EdgeType;
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

  protected final ConcurrentHashMap<String, Task> vertices = new ConcurrentHashMap<>();

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
  public boolean addVertex(final Task vertex) {
    vertices.put(vertex.getId(), vertex);
    return super.addVertex(vertex);
  }

  @Override
  public boolean removeVertex(final Task vertex) {
    vertices.remove(vertex.getId(), vertex);
    return super.removeVertex(vertex);
  }

  /**
   * Thread-safe method to get a task for a given id.
   * 
   * @param taskId the task id
   * @return the task with the required id
   */
  public Task getTask(final String taskId) {
    if (!vertices.containsKey(taskId)) {
      throw new IllegalStateException("Vertex " + taskId + " not in the enactment graph");
    }
    return vertices.get(taskId);
  }
}
