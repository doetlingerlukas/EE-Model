package at.uibk.dps.ee.model.persistance;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.graph.EnactmentSpecification;
import at.uibk.dps.ee.model.graph.MappingsConcurrent;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Specification;
import net.sf.opendse.model.Task;

/**
 * Transforms between the apollo and the odse spec representation.
 * 
 * @author Fedor Smirnov
 *
 */
public class EnactmentSpecTransformer {

  private EnactmentSpecTransformer() {}

  public static Specification toOdse(EnactmentSpecification eSpec) {
    Architecture<Resource, Link> arch = ResourceGraphTransformer.toOdse(eSpec.getResourceGraph());
    Application<Task, Dependency> appl =
        EnactmentGraphTransformer.toOdse(eSpec.getEnactmentGraph());
    Mappings<Task, Resource> mappings = MappingsTransformer.toOdse(eSpec.getMappings());
    Specification result = new Specification(appl, arch, mappings);
    eSpec.getAttributeNames()
        .forEach(attrName -> result.setAttribute(attrName, eSpec.getAttribute(attrName)));
    return result;
  }

  public static EnactmentSpecification toApollo(Specification spec) {
    EnactmentGraph eGraph = EnactmentGraphTransformer.toApollo(spec.getApplication());
    ResourceGraph rGraph = ResourceGraphTransformer.toApollo(spec.getArchitecture());
    MappingsConcurrent mappings = MappingsTransformer.toApollo(spec.getMappings());
    EnactmentSpecification result =
        new EnactmentSpecification(eGraph, rGraph, mappings, ConstantsEEModel.SpecIdDefault);
    spec.getAttributeNames()
        .forEach(attrName -> result.setAttribute(attrName, spec.getAttribute(attrName)));
    return result;
  }
}
