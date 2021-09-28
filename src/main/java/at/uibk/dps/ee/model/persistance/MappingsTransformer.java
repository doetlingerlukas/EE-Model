package at.uibk.dps.ee.model.persistance;

import at.uibk.dps.ee.model.graph.MappingsConcurrent;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/**
 * Transforms between the apollo and the odse mapping representation.
 * 
 * @author Fedor Smirnov
 *
 */
public class MappingsTransformer {

  /**
   * No constructor
   */
  private MappingsTransformer() {}

  /**
   * Transforms apollo mappings to odse mappings
   * 
   * @param mappingsApollo apollo mappings
   * @return odse mappings
   */
  public static Mappings<Task, Resource> toOdse(final MappingsConcurrent mappingsApollo) {
    final Mappings<Task, Resource> result = new Mappings<>();
    mappingsApollo.forEach(mapping -> result.add(mapping));
    return result;
  }

  /**
   * Transforms odse mappings to apollo mappings
   * 
   * @param mappingsOdse the odse mappings
   * @return the apollo mappings
   */
  public static MappingsConcurrent toApollo(final Mappings<Task, Resource> mappingsOdse) {
    final MappingsConcurrent result = new MappingsConcurrent();
    mappingsOdse.forEach(mapping -> result.addMapping(mapping));
    return result;
  }
}
