package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceDependency.TypeDependency;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;

public class PropertyServiceDependencyControlIfTest {

	@Test
	public void testCreateDependency() {
		Task src = new Task("src");
		Task dest = new Task("dest");
		String jsonKey = "jsonkey";
		boolean activation = false;

		Dependency result = PropertyServiceDependencyControlIf.createControlIfDependency(src, dest, jsonKey,
				activation);
		assertEquals(jsonKey, PropertyServiceDependency.getJsonKey(result));
		assertEquals(TypeDependency.ControlIf, PropertyServiceDependency.getType(result));
		assertFalse(PropertyServiceDependencyControlIf.getActivation(result));
	}

	@Test
	public void testGetSetActivation() {
		Dependency dep = new Dependency("dep");
		PropertyServiceDependency.setType(dep, TypeDependency.ControlIf);
		PropertyServiceDependencyControlIf.setActivation(dep, false);
		assertFalse(PropertyServiceDependencyControlIf.getActivation(dep));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckDep() {
		Dependency dep = new Dependency("dep");
		PropertyServiceDependency.setType(dep, TypeDependency.Data);
		PropertyServiceDependencyControlIf.checkDependency(dep);
	}
}
