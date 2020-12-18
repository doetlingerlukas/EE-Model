package at.uibk.dps.ee.model.properties;

import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container offering convenient access to the attributes of the
 * nodes modeling functions.
 * 
 * @author Fedor Smirnov
 *
 */
public final class PropertyServiceFunction extends AbstractPropertyService {

	/**
	 * No constructor
	 */
	private PropertyServiceFunction() {
	}

	/**
	 * Properties of the function nodes.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	public enum Property {
		/**
		 * A description of the resource
		 */
		Resource
	}

	/**
	 * Returns the resource string annotated with the given task.
	 * 
	 * @param task the given task
	 * @return the resource string
	 */
	public static String getResource(final Task task) {
		checkTask(task);
		final String attrName = Property.Resource.name();
		return (String) getAttribute(task, attrName);
	}

	/**
	 * Sets the resource for the given task.
	 * 
	 * @param task     the given task
	 * @param resource the resource to set
	 */
	public static void setResource(final Task task, final String resource) {
		checkTask(task);
		final String attrName = Property.Resource.name();
		task.setAttribute(attrName, resource);
	}

	/**
	 * Returns true iff the resource has been set for the given task.
	 * 
	 * @param task the given task
	 * @return true iff the resource has been set for the given task
	 */
	public static boolean isResourceSet(final Task task) {
		checkTask(task);
		return isAttributeSet(task, Property.Resource.name());
	}

	/**
	 * Checks that we are given a function task; Throws an exception if not.
	 * 
	 * @param task the task to check
	 */
	protected static void checkTask(final Task task) {
		if (!TaskPropertyService.isProcess(task)) {
			throw new IllegalArgumentException("Node " + task.getId() + " is not a function node.");
		}
	}
}
