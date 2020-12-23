package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container for accessing the attributes of serverless function nodes.
 * 
 * @author Fedor Smirnov
 *
 */
public final class PropertyServiceFunctionServerless extends AbstractPropertyService{

	/**
	 * No constructor
	 */
	private PropertyServiceFunctionServerless() {
	}
	
	/**
	 * Properties of serverless function nodes.
	 * 
	 * @author Fedor Smirnov
	 */
	protected enum Property{
		/**
		 * A description of the resource (only for serverless functions)
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
	 * Checks that the provided task is a serverless function.
	 *  
	 * @param task the provided task
	 */
	protected static void checkTask(Task task) {
		if (!PropertyServiceFunction.getType(task).equals(FunctionType.Serverless)) {
			throw new IllegalArgumentException("The task " + task.getId() + " is not a serverless function.");
		}
	}
}
