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
    assertThrows(IllegalStateException.class, () -> {
      tested.addMapping(wrongAOne);
    });
    assertFalse(tested.addMapping(AOne));
    assertTrue(tested.containsMapping(AOne));
    assertThrows(IllegalStateException.class, () -> {
      tested.containsMapping(wrongAOne);
    });
    assertTrue(tested.removeMapping(AOne));
    assertFalse(tested.containsMapping(AOne));
    assertFalse(tested.removeMapping(AOne));
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
