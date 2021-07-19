package at.uibk.dps.ee.model.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

public class EnactmentSpecificationTest {

  @Test
  public void test() {

    EnactmentGraph graph = new EnactmentGraph();
    ResourceGraph rGraph = new ResourceGraph();
    Mappings<Task, Resource> mappings = new Mappings<>();

    EnactmentSpecification tested = new EnactmentSpecification(graph, rGraph, mappings);

    assertEquals(graph, tested.getEnactmentGraph());
    assertEquals(rGraph, tested.getResourceGraph());
    assertEquals(mappings, tested.getMappings());
  }
}
