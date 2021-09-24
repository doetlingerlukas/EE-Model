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

  private EnactmentGraphTransformer() {}

  public static EnactmentGraph toApollo(Application<Task, Dependency> application) {
    EnactmentGraph result = new EnactmentGraph();
    application.forEach(task -> result.addVertex(task));
    application.getEdges().forEach(edge -> result.addEdge(edge, application.getSource(edge),
        application.getDest(edge), EdgeType.DIRECTED));
    return result;
  }

  public static Application<Task, Dependency> toOdse(EnactmentGraph eGraph) {
    Application<Task, Dependency> appl = new Application<>();
    eGraph.getVertices().forEach(task -> appl.addVertex(task));
    eGraph.getEdges().forEach(edge -> appl.addEdge(edge, eGraph.getSource(edge),
        eGraph.getDest(edge), EdgeType.DIRECTED));
    return appl;
  }
}
