package at.uibk.dps.ee.model.graph;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

class MappingsConcurrentTest {

  Resource resOne;
  Resource resTwo;

  Task taskA;
  Task taskB;

  Mapping<Task, Resource> AOne;
  Mapping<Task, Resource> wrongAOne;
  Mapping<Task, Resource> BOne;
  Mapping<Task, Resource> ATwo;
  Mapping<Task, Resource> BTwo;

  MappingsConcurrent<Task, Resource> tested;

  @Test
  void testAddRemove() {
    assertFalse(tested.containsMapping(AOne));
    assertTrue(tested.addMapping(AOne));
    assertFalse(tested.addMapping(AOne));
    assertEquals(AOne, tested.iterator().next());
    assertTrue(tested.containsMapping(AOne));
    assertTrue(tested.removeMapping(AOne));
    assertFalse(tested.containsMapping(AOne));
    assertFalse(tested.removeMapping(AOne));
  }

  @Test
  void testSourcesTargets() {
    assertTrue(tested.getTargets(taskA).isEmpty());
    assertTrue(tested.getSources(resOne).isEmpty());

    tested.addMapping(AOne);
    tested.addMapping(ATwo);

    assertEquals(2, tested.getTargets(taskA).size());
    assertTrue(tested.getTargets(taskA).contains(resOne));
    assertTrue(tested.getTargets(taskA).contains(resTwo));

    tested.addMapping(BOne);
    assertEquals(2, tested.getMappings(taskA).size());
    assertTrue(tested.getMappings(taskA).contains(AOne));
    assertTrue(tested.getMappings(taskA).contains(ATwo));

    assertEquals(2, tested.getMappings(resOne).size());
    assertTrue(tested.getMappings(resOne).contains(AOne));
    assertTrue(tested.getMappings(resOne).contains(BOne));

    assertEquals(2, tested.getSources(resOne).size());
    assertTrue(tested.getSources(resOne).contains(taskA));
    assertTrue(tested.getSources(resOne).contains(taskB));

    tested.removeMapping(ATwo);
    assertEquals(1, tested.getTargets(taskA).size());
    assertTrue(tested.getTargets(taskA).contains(resOne));

    tested.removeMapping(BOne);
    assertEquals(1, tested.getSources(resOne).size());
    assertTrue(tested.getSources(resOne).contains(taskA));


  }


  @BeforeEach
  void setup() {
    resOne = new Resource("resOne");
    resTwo = new Resource("resTwo");
    taskA = new Task("taskA");
    taskB = new Task("taskB");
    AOne = new Mapping<Task, Resource>("Aone", taskA, resOne);
    wrongAOne = new Mapping<Task, Resource>("Aone", taskB, resOne);
    ATwo = new Mapping<Task, Resource>("Atwo", taskA, resTwo);
    BOne = new Mapping<Task, Resource>("Bone", taskB, resOne);
    BTwo = new Mapping<Task, Resource>("Btwo", taskB, resTwo);
    tested = new MappingsConcurrent<>();
  }

}
