package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import at.uibk.dps.ee.model.properties.PropertyServiceDependency.TypeDependency;
import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

public class PropertyServiceDependencyTest {

	@Test
	public void testAddDataDependency() {
		Task proc = new Task("process");
		Communication data = new Communication("data");
		String jsonKey = "jsonKey";
		EnactmentGraph graph = new EnactmentGraph();
		PropertyServiceDependency.addDataDependency(proc, data, jsonKey, graph);
		Dependency result = graph.getOutEdges(proc).iterator().next();
		assertEquals(jsonKey, PropertyServiceDependency.getJsonKey(result));
		assertEquals(TypeDependency.Data, PropertyServiceDependency.getType(result));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChecDataDepEndpointsTwoProc() {
		Task proc1 = new Task("process1");
		Task proc2 = new Task("process2");
		PropertyServiceDependency.checkDataDependencyEndPoints(proc1, proc2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testChecDataDepEndpointsTwoData() {
		Communication data1 = new Communication("data1");
		Communication data2 = new Communication("data2");
		PropertyServiceDependency.checkDataDependencyEndPoints(data2, data1);
	}

	@Test
	public void testChecDataDepEndpoints() {
		Task proc = new Task("process");
		Communication data = new Communication("data");
		PropertyServiceDependency.checkDataDependencyEndPoints(proc, data);
		PropertyServiceDependency.checkDataDependencyEndPoints(data, proc);
	}

	@Test
	public void testCreateDependency() {
		String srcString = "src";
		String dstString = "dest";
		Task src = new Task(srcString);
		Task dst = new Task(dstString);
		String expected = srcString + ConstantsEEModel.DependencyAffix + dstString;
		Dependency result = PropertyServiceDependency.createDependency(src, dst);
		assertEquals(expected, result.getId());
	}

	@Test
	public void testGetSetJsonKey() {
		Dependency dep = new Dependency("dep");
		String key = "myKey";
		PropertyServiceDependency.setJsonKey(dep, key);
		assertEquals(key, PropertyServiceDependency.getJsonKey(dep));
	}

	@Test
	public void testGetSetType() {
		Dependency dep = new Dependency("dep");
		PropertyServiceDependency.setType(dep, TypeDependency.Data);
		assertEquals(TypeDependency.Data, PropertyServiceDependency.getType(dep));
	}
}
