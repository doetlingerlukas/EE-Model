package at.uibk.dps.ee.model.graph;

import net.sf.opendse.model.Element;

/**
 * The {@link EnactmentSpecification} models the whole enactment problem.
 * 
 * @author Fedor Smirnov
 */
public class EnactmentSpecification extends Element {

  protected final EnactmentGraph eGraph;
  protected final ResourceGraph rGraph;
  protected final MappingsConcurrent mappings;
  // TODO implement the routings as soon as we have the first use case

  /**
   * Creates an {@link EnactmentSpecification} containing the provided
   * {@link EnactmentGraph} and {@link ResourceGraph}, connected by the provided
   * {@link MappingsConcurrent}. The message routing is set to null.
   * 
   * @param enactmentGraph the enactment graph (application model)
   * @param resourceGraph the resource graph (resource model)
   * @param mappings the mappings
   */
  public EnactmentSpecification(final EnactmentGraph enactmentGraph,
      final ResourceGraph resourceGraph, final MappingsConcurrent mappings) {
    super("specification");
    this.eGraph = enactmentGraph;
    this.rGraph = resourceGraph;
    this.mappings = mappings;
  }

  /**
   * Returns the enactment graph.
   * 
   * @return the enactment graph
   */
  public EnactmentGraph getEnactmentGraph() {
    return eGraph;
  }

  /**
   * Returns the resource graph
   * 
   * @return the resource graph
   */
  public ResourceGraph getResourceGraph() {
    return rGraph;
  }

  public MappingsConcurrent getMappings() {
    return mappings;
  }
}
