package at.uibk.dps.ee.model.persistance;

import at.uibk.dps.ee.model.graph.EnactmentGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Application;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

/**
 * Transforms between the apollo and the odse application representation.
 * 
 * @author Fedor Smirnov
 */
public class EnactmentGraphTransformer {

  /**
   * No constructor.
   */
  private EnactmentGraphTransformer() {}

  /**
   * Transforms the application representation used by OpenDSE to the one used by
   * Apollo.
   * 
   * @param application application graph (OpenDSE's application representation)
   * @return enactment graph (Apollo's application representation)
   */
  public static EnactmentGraph toApollo(Application<Task, Dependency> application) {
    EnactmentGraph result = new EnactmentGraph();
    application.forEach(task -> result.addVertex(task));
    application.getEdges().forEach(edge -> result.addEdge(edge, application.getSource(edge),
        application.getDest(edge), EdgeType.DIRECTED));
    return result;
  }

  /**
   * Transforms the application representation used by Apollo to the one used by
   * OpenDSE.
   * 
   * @param eGraph enactment graph (Apollo's application representation)
   * @return application graph (OpenDSE's application representation)
   */
  public static Application<Task, Dependency> toOdse(EnactmentGraph eGraph) {
    Application<Task, Dependency> appl = new Application<>();
    eGraph.getVertices().forEach(task -> appl.addVertex(task));
    eGraph.getEdges().forEach(edge -> appl.addEdge(edge, eGraph.getSource(edge),
        eGraph.getDest(edge), EdgeType.DIRECTED));
    return appl;
  }
}
