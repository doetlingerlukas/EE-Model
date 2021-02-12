package at.uibk.dps.ee.model.graph;

import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Specification;
import net.sf.opendse.model.Task;

/**
 * The {@link EnactmentSpecification} models the whole enactment problem.
 * 
 * @author Fedor Smirnov
 */
public class EnactmentSpecification extends Specification {

  /**
   * Creates an {@link EnactmentSpecification} containing the provided
   * {@link EnactmentGraph} and {@link ResourceGraph}, connected by the provided
   * {@link Mappings}. The message routing is set to null.
   * 
   * @param enactmentGraph the enactment graph (application model)
   * @param resourceGraph the resource graph (resource model)
   * @param mappings the mappings
   */
  public EnactmentSpecification(final EnactmentGraph enactmentGraph,
      final ResourceGraph resourceGraph, final Mappings<Task, Resource> mappings) {
    super(enactmentGraph, resourceGraph, mappings, null);
  }

  /**
   * Returns the enactment graph.
   * 
   * @return the enactment graph
   */
  public EnactmentGraph getEnactmentGraph() {
    return (EnactmentGraph) getApplication();
  }

  /**
   * Returns the resource graph
   * 
   * @return the resource graph
   */
  public ResourceGraph getResourceGraph() {
    return (ResourceGraph) getArchitecture();
  }
}
