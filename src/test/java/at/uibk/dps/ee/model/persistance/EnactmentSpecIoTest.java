package at.uibk.dps.ee.model.persistance;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.graph.EnactmentSpecification;
import at.uibk.dps.ee.model.graph.MappingsConcurrent;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

class EnactmentSpecIoTest {

  protected final String tmpPath = "src/test/java/tmp.xml";

  EnactmentSpecification spec;

  @BeforeEach
  void setup() {
    Task task = new Task("task");
    Resource res = new Resource("res");
    ResourceGraph rGraph = new ResourceGraph();
    rGraph.addVertex(res);
    EnactmentGraph eGraph = new EnactmentGraph();
    eGraph.addVertex(task);
    MappingsConcurrent mappingsC = new MappingsConcurrent();
    Mapping<Task, Resource> mapping = new Mapping<Task, Resource>("mapping", task, res);
    mappingsC.addMapping(mapping);
    spec = new EnactmentSpecification(eGraph, rGraph, mappingsC, "");

    spec.setAttribute("attr", 42);
  }


  @Test
  void testReadWrite() {
    EnactmentSpecIo.writeESpecToFilePath(spec, tmpPath);
    EnactmentSpecification readIn = EnactmentSpecIo.readSpecFromFilePath(tmpPath);
    checkTwoSpecs(spec, readIn);
    // delete the tmp file
    File file = new File(tmpPath);
    file.delete();
  }

  protected void checkTwoSpecs(EnactmentSpecification original, EnactmentSpecification readIn) {
    for (Resource res : original.getResourceGraph()) {
      assertTrue(readIn.getResourceGraph().containsVertex(res.getId()));
    }
    for (Task task : original.getEnactmentGraph()) {
      assertTrue(readIn.getEnactmentGraph().containsVertex(task.getId()));
    }
    original.getMappings().mappingStream()
        .forEach(mapping -> assertTrue(readIn.getMappings().containsMapping(mapping)));

    original.getAttributeNames()
        .forEach(attrName -> assertEquals((int) original.getAttribute(attrName),
            (int) readIn.getAttribute(attrName)));
  }
}
