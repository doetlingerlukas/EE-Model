package at.uibk.dps.ee.model.persistance;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.MappingsConcurrent;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

class MappingsTransformerTest {

  Mapping<Task, Resource> first;
  Mapping<Task, Resource> second;

  @BeforeEach
  void setup() {
    Task task1 = new Task("t1");
    Task task2 = new Task("t2");
    Resource res1 = new Resource("r1");
    Resource res2 = new Resource("r2");
    first = new Mapping<Task, Resource>("m1", task1, res1);
    second = new Mapping<Task, Resource>("m2", task2, res2);
  }

  @Test
  void testOdseToApollo() {
    Mappings<Task, Resource> odseMappings = new Mappings<>();
    odseMappings.add(first);
    odseMappings.add(second);
    MappingsConcurrent result = MappingsTransformer.toApollo(odseMappings);
    assertTrue(result.containsMapping(first));
    assertTrue(result.containsMapping(second));
  }

  @Test
  void testApolloToOdse() {
    MappingsConcurrent apolloMappings = new MappingsConcurrent();
    apolloMappings.addMapping(first);
    apolloMappings.addMapping(second);
    Mappings<Task, Resource> result = MappingsTransformer.toOdse(apolloMappings);
    assertTrue(result.getAll().contains(first));
    assertTrue(result.getAll().contains(second));
  }
}
