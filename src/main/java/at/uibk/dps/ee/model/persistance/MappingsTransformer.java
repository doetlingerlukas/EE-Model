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

  private MappingsTransformer() {}

  public static Mappings<Task, Resource> toOdse(MappingsConcurrent mappingsApollo) {
    Mappings<Task, Resource> result = new Mappings<>();
    mappingsApollo.forEach(mapping -> result.add(mapping));
    return result;
  }

  public static MappingsConcurrent toApollo(Mappings<Task, Resource> mappingsOdse) {
    MappingsConcurrent result = new MappingsConcurrent();
    mappingsOdse.forEach(mapping -> result.addMapping(mapping));
    return result;
  }
}
