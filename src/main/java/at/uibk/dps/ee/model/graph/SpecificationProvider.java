package at.uibk.dps.ee.model.graph;

/**
 * Interface offering access to all parts of the enactment specification.
 * 
 * @author Fedor Smirnov
 */
public interface SpecificationProvider
    extends ResourceGraphProvider, EnactmentGraphProvider, MappingProvider {

  /**
   * Returns the whole specification graph.
   * 
   * @return the specification graph.
   */
  EnactmentSpecification getSpecification();
}
