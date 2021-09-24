package at.uibk.dps.ee.model.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EnactmentSpecificationTest {

  @Test
  public void test() {

    EnactmentGraph graph = new EnactmentGraph();
    ResourceGraph rGraph = new ResourceGraph();
    MappingsConcurrent mappings = new MappingsConcurrent();

    EnactmentSpecification tested = new EnactmentSpecification(graph, rGraph, mappings, "bla");

    assertEquals(graph, tested.getEnactmentGraph());
    assertEquals(rGraph, tested.getResourceGraph());
    assertEquals(mappings, tested.getMappings());
  }
}
