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

  /**
   * No constructor
   */
  private EnactmentSpecTransformer() {}

  /**
   * Transforms an Apollo spec to an Odse spec
   * 
   * @param eSpec the Apollo spec
   * @return the Odse spec
   */
  public static Specification toOdse(final EnactmentSpecification eSpec) {
    final Architecture<Resource, Link> arch =
        ResourceGraphTransformer.toOdse(eSpec.getResourceGraph());
    final Application<Task, Dependency> appl =
        EnactmentGraphTransformer.toOdse(eSpec.getEnactmentGraph());
    final Mappings<Task, Resource> mappings = MappingsTransformer.toOdse(eSpec.getMappings());
    final Specification result = new Specification(appl, arch, mappings);
    eSpec.getAttributeNames()
        .forEach(attrName -> result.setAttribute(attrName, eSpec.getAttribute(attrName)));
    return result;
  }

  /**
   * Transforms an Odse spec to an Apollo spec.
   * 
   * @param spec the odse spec
   * @return the apollo spec
   */
  public static EnactmentSpecification toApollo(Specification spec) {
    final EnactmentGraph eGraph = EnactmentGraphTransformer.toApollo(spec.getApplication());
    final ResourceGraph rGraph = ResourceGraphTransformer.toApollo(spec.getArchitecture());
    final MappingsConcurrent mappings = MappingsTransformer.toApollo(spec.getMappings());
    final EnactmentSpecification result =
        new EnactmentSpecification(eGraph, rGraph, mappings, ConstantsEEModel.SpecIdDefault);
    spec.getAttributeNames()
        .forEach(attrName -> result.setAttribute(attrName, spec.getAttribute(attrName)));
    return result;
  }
}
