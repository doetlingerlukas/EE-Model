package at.uibk.dps.ee.model.properties;

import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container to access the attributes of the enactment
 * {@link Dependency}s.
 * 
 * @author Fedor Smirnov
 */
public class PropertyServiceDependency extends AbstractPropertyService {

	private PropertyServiceDependency() {
	}

	public enum Property {
		/**
		 * General type of dependency
		 */
		Type
	}

	public enum TypeDependency {
		/**
		 * Data flow
		 */
		Data
	}

	/**
	 * Returns the type of the given dependency.
	 * 
	 * @param dep the given dependency
	 */
	public static TypeDependency getType(Dependency dep) {
		String attrName = Property.Type.name();
		return TypeDependency.valueOf((String) getAttribute(dep, attrName));
	}

	/**
	 * Sets the type of the given dependency.
	 * 
	 * @param dep the given dependency
	 * @param type the type to set
	 */
	public static void setType(Dependency dep, TypeDependency type) {
		String attrName = Property.Type.name();
		dep.setAttribute(attrName, type.name());
	}
}
