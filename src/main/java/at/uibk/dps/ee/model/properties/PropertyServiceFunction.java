package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.core.enactable.Enactable.State;
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
	protected enum Property {
		/**
		 * The type of the modeled function
		 */
		FunctionType,
		/**
		 * The state of the corresponding enactable
		 */
		EnactableState
	}

	/**
	 * The types of the modeled function.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	public enum FunctionType {
		/**
		 * Serverless functions, e.g., AWS Lambda
		 */
		Serverless,
		/**
		 * Functions executed on the same machine where the EE is running.
		 */
		Local
	}
	
	/**
	 * Annotates the provided task with the provided enactable state.
	 * 
	 * @param task the provided task
	 * @param enactableState the state to annotate
	 */
	public static void setEnactableState(final Task task, final State enactableState) {
		checkTask(task);
		final String attrName = Property.EnactableState.name();
		task.setAttribute(attrName, enactableState.name());
	}
	
	/**
	 * Returns the enactable state annotated at the provided function node.
	 * 
	 * @param task the provided function node
	 * @return the enactable state annotated at the provided function node
	 */
	public static State getEnactableState(final Task task) {
		checkTask(task);
		final String attrName = Property.EnactableState.name();
		if (!isAttributeSet(task, attrName)) {
			return State.WAITING;
		}
		return State.valueOf((String) getAttribute(task, attrName));
	}

	/**
	 * Sets the function type for the provided function.
	 * 
	 * @param functionType the type to set
	 * @param the          task to annotate
	 */
	public static void setType(final FunctionType functionType, final Task task) {
		checkTask(task);
		final String attrName = Property.FunctionType.name();
		task.setAttribute(attrName, functionType.name());
	}

	/**
	 * Returns the function type of the given task.
	 * 
	 * @param task the given task
	 * @return the function type of the given task
	 */
	public static FunctionType getType(final Task task) {
		checkTask(task);
		final String attrName = Property.FunctionType.name();
		return FunctionType.valueOf((String) getAttribute(task, attrName));
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
