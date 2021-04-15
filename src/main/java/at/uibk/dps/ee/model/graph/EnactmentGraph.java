package at.uibk.dps.ee.model.graph;

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
  
  /**
   * Default constructor.
   */
  public EnactmentGraph() {
  }
  
  /**
   * Constructor to build an {@link EnactmentGraph} out of an application.
   * 
   * @param application the given application
   */
  public EnactmentGraph(Application<Task, Dependency> application) {
    super();
    application.getEdges().forEach(edge -> {
      Task src = application.getSource(edge);
      Task dst = application.getDest(edge);
      this.addEdge(edge, src, dst, EdgeType.DIRECTED);
    });
    application.forEach(task -> this.addVertex(task));
  }
}
