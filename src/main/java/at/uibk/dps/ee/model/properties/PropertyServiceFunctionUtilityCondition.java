package at.uibk.dps.ee.model.properties;

import java.util.Set;

import at.uibk.dps.ee.model.objects.Condition;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Contains convenience function to access properties of utility functions
 * evaluating conditions.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionUtilityCondition extends AbstractPropertyService {

	/**
	 * No constructor
	 */
	private PropertyServiceFunctionUtilityCondition() {
	}

	protected enum Property {
		/**
		 * The checked conditions
		 */
		Conditions,
		/**
		 * Summary operator
		 */
		Summary
	}

	public enum Summary {
		AND, OR
	}

	/**
	 * Returns the summary type annotated at the given task.
	 * 
	 * @param task the given task
	 * @return the summary type annotated at the given task
	 */
	public static Summary getSummary(Task task) {
		checkTask(task);
		String attrName = Property.Summary.name();
		return Summary.valueOf((String) getAttribute(task, attrName));
	}

	/**
	 * Sets the summary type for the provided task
	 * 
	 * @param task    the provided task
	 * @param summary the summary type to set
	 */
	public static void setSummary(Task task, Summary summary) {
		checkTask(task);
		String attrName = Property.Summary.name();
		task.setAttribute(attrName, summary.name());
	}

	/**
	 * Returns the set of conditions annotated on the provided task.
	 * 
	 * @param task the provided task
	 * @return the set of conditions annotated on the provided task
	 */
	@SuppressWarnings("unchecked")
	public static Set<Condition> getConditions(Task task) {
		checkTask(task);
		String attrName = Property.Conditions.name();
		return (Set<Condition>) getAttribute(task, attrName);
	}

	/**
	 * Annotates the provided condition task with the given set of conditions.
	 * 
	 * @param task       the condition task
	 * @param conditions the set of conditions to annotate
	 */
	public static void setConditions(Task task, Set<Condition> conditions) {
		checkTask(task);
		String attrName = Property.Conditions.name();
		task.setAttribute(attrName, conditions);
	}

	/**
	 * Checks a given task. Throws an exception if it is not a condition node.
	 * 
	 * @param task the task to check.
	 */
	protected static void checkTask(Task task) {
		if (!PropertyServiceFunctionUtility.getUtilityType(task).equals(UtilityType.Condition)) {
			throw new IllegalArgumentException("Task " + task.getId() + " is not a condition node.");
		}
	}

}
