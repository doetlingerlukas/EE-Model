package at.uibk.dps.ee.model.utils;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.graph.EnactmentSpecification;
import at.uibk.dps.ee.model.graph.ResourceGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Element;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

class UtilsDeepCopyTest {

  EnactmentGraph eGraphOriginal;

  ResourceGraph rGraphOriginal;

  Mappings<Task, Resource> mappingsOriginal;

  EnactmentSpecification specOriginal;

  @Test
  void testCopyAttributesWrongClass() {
    Task task = new Task("task");
    Communication comm = new Communication("comm");
    assertThrows(IllegalArgumentException.class, () -> {
      UtilsDeepCopy.copyAttributes(task, comm);
    });
  }

  @Test
  void testRestoreSpec() {
    String attrName = "attr";
    EnactmentSpecification specCopy = UtilsDeepCopy.deepCopySpec(specOriginal);
    ResourceGraph graphCopy = specCopy.getResourceGraph();
    Resource res = graphCopy.getVertices().iterator().next();
    Link link = graphCopy.getEdges().iterator().next();
    res.setAttribute(attrName, 3);
    link.setAttribute(attrName, 3);
    EnactmentGraph eCopy = specCopy.getEnactmentGraph();
    Task task = eCopy.getVertices().iterator().next();
    Dependency dep = eCopy.getEdges().iterator().next();
    task.setAttribute(attrName, 3);
    dep.setAttribute(attrName, 3);
    UtilsDeepCopy.restoreSpecAttributes(specOriginal, specCopy);
    assertNull(res.getAttribute(attrName));
    assertNull(link.getAttribute(attrName));
    assertNull(task.getAttribute(attrName));
    assertNull(dep.getAttribute(attrName));
  }

  @Test
  void testRestoreMappingAttributes() {
    EnactmentGraph eGraphCopy = UtilsDeepCopy.deepCopyEGraph(eGraphOriginal);
    ResourceGraph rGraphCopy = UtilsDeepCopy.deepCopyRGraph(rGraphOriginal);
    Mappings<Task, Resource> mappingCopy =
        UtilsDeepCopy.deepCopyMappings(mappingsOriginal, eGraphCopy, rGraphCopy);
    Mapping<Task, Resource> copyMapping = mappingCopy.getAll().iterator().next();
    copyMapping.setAttribute("attr", 3);
    UtilsDeepCopy.restoreMappingsAttributes(mappingsOriginal, mappingCopy);
    assertNull(copyMapping.getAttribute("attr"));
  }

  @Test
  void testRestoreElementAttributes() {
    Task original = new Task("original");
    String attrName1 = "attr1";
    int value1 = 1;
    String attrName2 = "attr2";
    int value2 = 2;
    original.setAttribute(attrName1, value1);
    Task copy = UtilsDeepCopy.deepCopyTask(original);
    copy.setAttribute(attrName2, value2);
    assertEquals(value1, (int) copy.getAttribute(attrName1));
    assertEquals(value2, (int) copy.getAttribute(attrName2));
    UtilsDeepCopy.restoreElementAttributes(original, copy);
    assertEquals(value1, (int) copy.getAttribute(attrName1));
    assertNull(copy.getAttribute(attrName2));
  }

  @Test
  void testCopySpec() {
    EnactmentSpecification specCopy = UtilsDeepCopy.deepCopySpec(specOriginal);
    assertTrue(isCorrectEGraphCopy(eGraphOriginal, specCopy.getEnactmentGraph()));
    assertTrue(isCorrectRGraphCopy(rGraphOriginal, specCopy.getResourceGraph()));
    assertTrue(isCorrectMappingsCopy(mappingsOriginal, specCopy.getMappings(), eGraphOriginal,
        specCopy.getEnactmentGraph(), rGraphOriginal, specCopy.getResourceGraph()));
  }

  @Test
  void testCopyMappings() {
    EnactmentGraph copyE = UtilsDeepCopy.deepCopyEGraph(eGraphOriginal);
    ResourceGraph copyR = UtilsDeepCopy.deepCopyRGraph(rGraphOriginal);
    Mappings<Task, Resource> mappingsCopy =
        UtilsDeepCopy.deepCopyMappings(mappingsOriginal, copyE, copyR);
    assertTrue(isCorrectMappingsCopy(mappingsOriginal, mappingsCopy, eGraphOriginal, copyE,
        rGraphOriginal, copyR));
  }

  @Test
  void testCopyRGraph() {
    ResourceGraph rCopy = UtilsDeepCopy.deepCopyRGraph(rGraphOriginal);
    assertTrue(isCorrectRGraphCopy(rGraphOriginal, rCopy));
  }

  @Test
  void testCopyEGraph() {
    EnactmentGraph eCopy = UtilsDeepCopy.deepCopyEGraph(eGraphOriginal);
    assertTrue(isCorrectEGraphCopy(eGraphOriginal, eCopy));
  }

  static boolean isCorrectMappingsCopy(Mappings<Task, Resource> original,
      Mappings<Task, Resource> copy, EnactmentGraph eGraph, EnactmentGraph eCopy,
      ResourceGraph rGraph, ResourceGraph rCopy) {
    Map<String, Mapping<Task, Resource>> originalMap = original.getAll().stream()
        .collect(Collectors.toMap(mapping -> mapping.getId(), mapping -> mapping));
    Map<String, Mapping<Task, Resource>> copyMap = copy.getAll().stream()
        .collect(Collectors.toMap(mapping -> mapping.getId(), mapping -> mapping));
    return originalMap.keySet().stream()
        .allMatch(key -> isMappingCorrectlyCopied(originalMap.get(key), copyMap.get(key), eGraph,
            eCopy, rGraph, rCopy));
  }

  static boolean isMappingCorrectlyCopied(Mapping<Task, Resource> mapping,
      Mapping<Task, Resource> copy, EnactmentGraph eGraph, EnactmentGraph eCopy,
      ResourceGraph rGraph, ResourceGraph rCopy) {
    boolean result = true;
    result &= areAttributesCopied(mapping, copy);
    result &= eGraph.getVertex(mapping.getSource()) != null;
    result &= eCopy.getVertex(copy.getSource()) != null;
    result &= rGraph.getVertex(mapping.getTarget()) != null;
    result &= rCopy.getVertex(copy.getTarget()) != null;
    return result;
  }


  static boolean isCorrectRGraphCopy(ResourceGraph originalR, ResourceGraph copyR) {
    boolean result = true;
    // check the vertices
    for (Resource res : originalR.getVertices()) {
      Resource copyRes = copyR.getVertex(res.getId());
      result &= (copyRes != null);
      result &= copyRes != res;
      result &= areAttributesCopied(res, copyRes);
    }
    // check the edges
    for (Link link : originalR.getEdges()) {
      Link copyLink = copyR.getEdge(link.getId());
      result &= (copyLink != null);
      result &= copyLink != link;
      result &= areAttributesCopied(link, copyLink);
      result &= (originalR.getEndpoints(link).getFirst().getId()
          .equals(copyR.getEndpoints(copyLink).getFirst().getId()));
      result &= (originalR.getEndpoints(link).getSecond().getId()
          .equals(copyR.getEndpoints(copyLink).getSecond().getId()));
    }
    return result;
  }


  static boolean isCorrectEGraphCopy(EnactmentGraph originalE, EnactmentGraph copyE) {
    boolean result = true;
    // check the vertices
    for (Task task : originalE.getVertices()) {
      Task copyTask = copyE.getVertex(task.getId());
      result &= (copyTask != null);
      result &= copyTask != task;
      result &= areAttributesCopied(task, copyTask);
    }
    // check the edges
    for (Dependency dep : originalE.getEdges()) {
      Dependency copyDep = copyE.getEdge(dep.getId());
      result &= (copyDep != null);
      result &= copyDep != dep;
      result &= areAttributesCopied(dep, copyDep);
      result &= (originalE.getSource(dep).getId().equals(copyE.getSource(copyDep).getId()));
      result &= (originalE.getDest(dep).getId().equals(copyE.getDest(copyDep).getId()));
    }
    return result;
  }

  static boolean areAttributesCopied(Element original, Element copy) {
    return original.getAttributeNames().stream()
        .allMatch(attrName -> original.getAttribute(attrName).equals(copy.getAttribute(attrName)));
  }


  @BeforeEach
  void setup() {
    // the e graph
    Task root = new Task("root");
    root.setAttribute("attr", 42);
    Communication comm = new Communication("comm");
    Task task = new Task("task");
    Dependency d1 = new Dependency("d1");
    d1.setAttribute("attrDep", "abc");
    Dependency d2 = new Dependency("d2");
    eGraphOriginal = new EnactmentGraph();
    eGraphOriginal.addEdge(d1, root, comm, EdgeType.DIRECTED);
    eGraphOriginal.addEdge(d2, comm, task, EdgeType.DIRECTED);

    // the r graph
    Resource res1 = new Resource("res1");
    res1.setAttribute("resAttr", true);
    Resource res2 = new Resource("res2");
    Link link = new Link("link");
    link.setAttribute("linkAttr", 14.1);
    rGraphOriginal = new ResourceGraph();
    rGraphOriginal.addEdge(link, res1, res2, EdgeType.UNDIRECTED);

    // the mappings
    Mapping<Task, Resource> map1 = new Mapping<Task, Resource>("m1", root, res1);
    map1.setAttribute("mapAttr", "attr");
    Mapping<Task, Resource> map2 = new Mapping<Task, Resource>("m2", root, res2);
    Mapping<Task, Resource> map3 = new Mapping<Task, Resource>("m3", task, res2);
    mappingsOriginal = new Mappings<>();
    mappingsOriginal.add(map1);
    mappingsOriginal.add(map2);
    mappingsOriginal.add(map3);

    // the spec
    specOriginal = new EnactmentSpecification(eGraphOriginal, rGraphOriginal, mappingsOriginal);
  }

}
