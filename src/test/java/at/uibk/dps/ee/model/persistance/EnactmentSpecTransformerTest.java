package at.uibk.dps.ee.model.persistance;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.graph.EnactmentSpecification;
import at.uibk.dps.ee.model.graph.MappingsConcurrent;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import net.sf.opendse.model.Application;
import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Specification;
import net.sf.opendse.model.Task;

class EnactmentSpecTransformerTest {

  Task task;
  Resource res;

  Specification odseSpec;
  EnactmentSpecification apolloSpec;

  String attrName = "attr";
  int attrValue = 42;



  @BeforeEach
  void setup() {
    task = new Task("task");
    res = new Resource("res");

    Architecture<Resource, Link> arch = new Architecture<>();
    ResourceGraph rGraph = new ResourceGraph();
    arch.addVertex(res);
    rGraph.addVertex(res);

    Application<Task, Dependency> appl = new Application<>();
    EnactmentGraph eGraph = new EnactmentGraph();
    appl.addVertex(task);
    eGraph.addVertex(task);

    Mappings<Task, Resource> mappings = new Mappings<>();
    MappingsConcurrent mappingsC = new MappingsConcurrent();
    Mapping<Task, Resource> mapping = new Mapping<Task, Resource>("mapping", task, res);
    mappings.add(mapping);
    mappingsC.addMapping(mapping);

    odseSpec = new Specification(appl, arch, mappings);
    apolloSpec = new EnactmentSpecification(eGraph, rGraph, mappingsC, "");

    odseSpec.setAttribute(attrName, attrValue);
    apolloSpec.setAttribute(attrName, attrValue);
  }

  @Test
  void testApolloToOdse() {
    Specification result = EnactmentSpecTransformer.toOdse(apolloSpec);
    assertTrue(result.getApplication().getVertex(task.getId()) != null);
    assertTrue(result.getArchitecture().getVertex(res.getId()) != null);
    assertTrue(result.getMappings().getAll().size() == 1);
    Mapping<Task, Resource> m = result.getMappings().iterator().next();
    assertEquals(task, m.getSource());
    assertEquals(res, m.getTarget());
    assertEquals(attrValue, (int) result.getAttribute(attrName));
  }

  @Test
  void testOdseToApollo() {
    EnactmentSpecification result = EnactmentSpecTransformer.toApollo(odseSpec);
    assertTrue(result.getEnactmentGraph().containsVertex(task.getId()));
    assertTrue(result.getResourceGraph().containsVertex(res.getId()));
    assertTrue(result.getMappings().mappingStream().count() == 1);
    Mapping<Task, Resource> m = result.getMappings().iterator().next();
    assertEquals(task, m.getSource());
    assertEquals(res, m.getTarget());
    assertEquals(attrValue, (int) result.getAttribute(attrName));
  }

}
