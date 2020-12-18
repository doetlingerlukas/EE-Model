package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceDependency.TypeDependency;
import net.sf.opendse.model.Dependency;

public class PropertyServiceDependencyTest {

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
