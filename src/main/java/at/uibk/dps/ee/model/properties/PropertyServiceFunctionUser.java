package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container offering convenience methods to access the properties
 * of task nodes modeling user functions.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionUser extends AbstractPropertyService {

	/**
	 * No constructor.
	 */
	private PropertyServiceFunctionUser() {
	}

	/**
	 * Defines the relevant attributes of user functions.
	 * 
	 * @author Fedor Smirnov
	 */
	protected enum Property {
		/**
		 * The type of the function.
		 */
		FunctionType
	}

	protected static final String propNameFunctionType = Property.FunctionType.name();

	/**
	 * Creates a task node with the given ID, modeling a user function of the
	 * provided type.
	 * 
	 * @param taskId
	 * @param functionType
	 * @return
	 */
	public static Task createUserTask(String taskId, String functionType) {
		Task result = new Task(taskId);
		PropertyServiceFunction.setUsageType(UsageType.User, result);
		setFunctionType(result, functionType);
		return result;
	}

	/**
	 * Returns the function type for the given task.
	 * 
	 * @param task the given task
	 * @return the function type for the given task
	 */
	public static String getFunctionType(Task task) {
		return (String) getAttribute(task, propNameFunctionType);
	}

	/**
	 * Sets the function type for the provided task.
	 * 
	 * @param task         the provided task
	 * @param functionType the function type to set
	 */
	protected static void setFunctionType(Task task, String functionType) {
		checkTask(task);
		task.setAttribute(propNameFunctionType, functionType);
	}

	/**
	 * Checks that the provided task is a user task. Throws an exception otherwise.
	 * 
	 * @param task the provided task
	 */
	protected static void checkTask(Task task) {
		PropertyServiceFunction.checkTask(task);
		if (!PropertyServiceFunction.getUsageType(task).equals(UsageType.User)) {
			throw new IllegalArgumentException("The task " + task.getId() + " is not a user task.");
		}
	}
}
