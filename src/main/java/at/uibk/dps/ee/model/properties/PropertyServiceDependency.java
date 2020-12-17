package at.uibk.dps.ee.model.properties;

import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container to access the attributes of the enactment
 * {@link Dependency}s.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceDependency extends AbstractPropertyService {

	private PropertyServiceDependency() {
	}

	/**
	 * The properties annotated on dependency edges.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	public enum Property {
		/**
		 * General type of dependency
		 */
		Type
	}

	/**
	 * Different types of dependencies.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
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
	public static TypeDependency getType(final Dependency dep) {
		final String attrName = Property.Type.name();
		return TypeDependency.valueOf((String) getAttribute(dep, attrName));
	}

	/**
	 * Sets the type of the given dependency.
	 * 
	 * @param dep the given dependency
	 * @param type the type to set
	 */
	public static void setType(final Dependency dep, final TypeDependency type) {
		final String attrName = Property.Type.name();
		dep.setAttribute(attrName, type.name());
	}
}
