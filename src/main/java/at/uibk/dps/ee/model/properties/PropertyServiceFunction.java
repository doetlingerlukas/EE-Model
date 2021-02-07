package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.core.enactable.Enactable;
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
		UsageType,
		/**
		 * The enactable associated with the task
		 */
		Enactable
	}
	
	protected static final String propNameUsageType = Property.UsageType.name();

	/**
	 * The types of the modeled function.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	public enum UsageType {
		/**
		 * Functions performing the actual calculation/processing defined by the user
		 * (the practical purpose of the WF execution).
		 */
		User,
		/**
		 * Local functions which realize the data and control flow defined by the WF
		 * designer with some degree of processing.
		 */
		Utility,
		/**
		 * Functions which do not result in computation, but only detail the data flow
		 */
		DataFlow
	}

	/**
	 * Returns the enactable of the given task.
	 * 
	 * @param task the given task
	 * @return the enactable of the given task
	 */
	public static Enactable getEnactable(final Task task) {
		checkTask(task);
		final String attrName = Property.Enactable.name();
		return (Enactable) getAttribute(task, attrName);
	}

	/**
	 * Sets the enactable for the given task-
	 * 
	 * @param task      the given task
	 * @param enactable the enactable to set
	 */
	public static void setEnactable(final Task task, final Enactable enactable) {
		checkTask(task);
		final String attrName = Property.Enactable.name();
		task.setAttribute(attrName, enactable);
	}

	/**
	 * Sets the function type for the provided function.
	 * 
	 * @param functionType the type to set
	 * @param the          task to annotate
	 */
	public static void setUsageType(final UsageType functionType, final Task task) {
		checkTask(task);
		task.setAttribute(propNameUsageType, functionType.name());
	}

	/**
	 * Returns the function type of the given task.
	 * 
	 * @param task the given task
	 * @return the function type of the given task
	 */
	public static UsageType getUsageType(final Task task) {
		checkTask(task);
		return UsageType.valueOf((String) getAttribute(task, propNameUsageType));
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
