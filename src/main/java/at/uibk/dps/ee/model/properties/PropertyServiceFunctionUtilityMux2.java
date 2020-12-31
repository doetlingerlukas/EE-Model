package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Container for the convenience methods used to access the attributes of the
 * util functions modeling 2:1 multiplexing.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionUtilityMux2 extends AbstractPropertyService {

	/**
	 * No constructor.
	 */
	private PropertyServiceFunctionUtilityMux2() {
	}

	/**
	 * Properties defining the attribute names
	 * 
	 * @author Fedor Smirnov
	 */
	protected enum Property {
		/**
		 * First input
		 */
		First,
		/**
		 * Second input
		 */
		Second,
		/**
		 * The control variable (True => First, False => Second)
		 */
		Control
	}

	/**
	 * Returns the id of the second input of the given mux node.
	 * 
	 * @param task the given mux node
	 * @return the id of the second input of the given mux node
	 */
	public static String getSecondInput(final Task task) {
		checkTask(task);
		final String attrName = Property.Second.name();
		return (String) getAttribute(task, attrName);
	}

	/**
	 * Sets the ID of the second input of the given mux node.
	 * 
	 * @param task          the given mux node
	 * @param secondInputId the id of the second input
	 */
	public static void setSecondInputId(final Task task, final String secondInputId) {
		checkTask(task);
		final String attrName = Property.Second.name();
		task.setAttribute(attrName, secondInputId);
	}

	/**
	 * Returns the id of the first input of the given mux node.
	 * 
	 * @param task the given mux node
	 * @return the id of the first input of the given mux node
	 */
	public static String getFirstInputId(final Task task) {
		checkTask(task);
		final String attrName = Property.First.name();
		return (String) getAttribute(task, attrName);
	}

	/**
	 * Sets the ID of the first input of the given mux node.
	 * 
	 * @param task         the given mux node
	 * @param firstInputId the id of the first input
	 */
	public static void setFirstInputId(final Task task, final String firstInputId) {
		checkTask(task);
		final String attrName = Property.First.name();
		task.setAttribute(attrName, firstInputId);
	}

	/**
	 * Returns the id of the data node containing the control data for the given mux
	 * function node.
	 * 
	 * @param task the mux function node
	 * @return id of the data node containing the control data for the given mux
	 *         function node
	 */
	public static String getControlId(final Task task) {
		checkTask(task);
		final String attrName = Property.Control.name();
		return (String) getAttribute(task, attrName);
	}

	/**
	 * Sets the id of the node containing the control data for the given mux node.
	 * 
	 * @param task          the mux node
	 * @param controlDataId the id of the node containing the control data
	 */
	public static void setControl(final Task task, final String controlDataId) {
		checkTask(task);
		final String attrName = Property.Control.name();
		task.setAttribute(attrName, controlDataId);
	}

	/**
	 * Checks that the given task models a 2:1 mux. Throws an exception otherwise.
	 * 
	 * @param task the task to check
	 */
	protected static void checkTask(final Task task) {
		if (!PropertyServiceFunctionUtility.getUtilityType(task).equals(UtilityType.MultiPlex2)) {
			throw new IllegalArgumentException("Task " + task.getId() + " does not model a 2:1 multiplexer.");
		}
	}
}
