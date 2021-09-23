package at.uibk.dps.ee.model.graph;

/**
 * Interface for the classes offering access to the mappings.
 * 
 * @author Fedor Smirnov
 *
 */
public interface MappingProvider {

  /**
   * Returns the mappings.
   * 
   * @return the mappings
   */
  MappingsConcurrent getMappings();
}
