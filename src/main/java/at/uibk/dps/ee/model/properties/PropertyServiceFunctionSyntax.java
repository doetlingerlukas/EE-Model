package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container to access properties of syntax function nodes.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionSyntax extends AbstractPropertyService {

	/**
	 * No constructor.
	 */
	private PropertyServiceFunctionSyntax() {
	}

	/**
	 * Properties defining attribute names.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	protected enum Property {
		/**
		 * The type of the syntax function
		 */
		SyntaxType
	}

	/**
	 * Types of syntax functions.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	public enum SyntaxType {
		/**
		 * Forwards the earliest available input to all outputs.
		 */
		EarliestInput
	}

	/**
	 * Creates a syntax function with the given ID and the given function type
	 * 
	 * @param taskId     the id of the task to create
	 * @param syntaxType the syntax type
	 * @return a syntax function with the given ID and the given function type
	 */
	public static Task createSyntaxFunction(final String taskId, final SyntaxType syntaxType) {
		final Task result = new Task(taskId);
		PropertyServiceFunction.setType(FunctionType.Syntax, result);
		PropertyServiceFunctionSyntax.setSyntaxType(result, syntaxType);
		return result;
	}

	/**
	 * Returns the syntax type for the given task.
	 * 
	 * @param task the given task
	 * @return the syntax type for the given task
	 */
	public static SyntaxType getSyntaxType(final Task task) {
		checkTask(task);
		final String attrName = Property.SyntaxType.name();
		return SyntaxType.valueOf((String) getAttribute(task, attrName));
	}

	/**
	 * Sets the syntax type for the given task.
	 * 
	 * @param task the given task
	 * @param type the type to set
	 */
	public static void setSyntaxType(final Task task, final SyntaxType type) {
		checkTask(task);
		final String attrName = Property.SyntaxType.name();
		task.setAttribute(attrName, type.name());
	}

	/**
	 * Checks that the given task is a syntax task throws an exception otherwise.
	 * 
	 * @param task the task to check
	 */
	protected static void checkTask(final Task task) {
		PropertyServiceFunction.checkTask(task);
		if (!PropertyServiceFunction.getType(task).equals(FunctionType.Syntax)) {
			throw new IllegalArgumentException("Task " + task.getId() + " is not a syntax task");
		}
	}
}
