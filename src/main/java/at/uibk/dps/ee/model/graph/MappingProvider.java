package at.uibk.dps.ee.model.graph;

import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

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
  Mappings<Task, Resource> getMappings();
}
