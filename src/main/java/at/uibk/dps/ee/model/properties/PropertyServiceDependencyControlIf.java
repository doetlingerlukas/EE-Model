package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceDependency.TypeDependency;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container for accessing the properties of the control
 * depedencies modeling the implications of if compounds.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceDependencyControlIf extends AbstractPropertyService {

	/**
	 * No constructor
	 */
	private PropertyServiceDependencyControlIf() {
	}

	/**
	 * Properties defining the attribute names.
	 * 
	 * @author Fedor Smirnov
	 */
	protected enum Property {
		/**
		 * Boolean value: True => edge is active when condition variable is true; False
		 * => edge is active when condition variable is false.
		 */
		Activation
	}

	/**
	 * Returns the activation of the provided dependency.
	 * 
	 * @param dependency the provided dependency.
	 * @return the activation of the provided dependency
	 */ 
	public static boolean getActivation(Dependency dependency) {
		checkDependency(dependency);
		String attrName = Property.Activation.name();
		return (boolean) getAttribute(dependency, attrName);
	}
	
	/**
	 * Sets the activation for the provided dependency.
	 * 
	 * @param dependency the provided dependency
	 * @param activation the activation to set
	 */
	public static void setActivation(Dependency dependency, boolean activation) {
		checkDependency(dependency);
		String attrName = Property.Activation.name();
		dependency.setAttribute(attrName, activation);
	}
	
	/**
	 * Checks the provided {@link Dependency}. Throws an exception if it does not
	 * model an if control flow.
	 * 
	 * @param dependency the dependency to check.
	 */
	protected static void checkDependency(Dependency dependency) {
		if (!PropertyServiceDependency.getType(dependency).equals(TypeDependency.ControlIf)) {
			throw new IllegalArgumentException(
					"The dependency " + dependency.getId() + " does not model a control if implication.");
		}
	}
}
