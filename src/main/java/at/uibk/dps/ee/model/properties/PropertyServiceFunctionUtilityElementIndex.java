package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container managing the properties of the function nodes
 * modeling element index operations.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionUtilityElementIndex extends AbstractPropertyService {

	/**
	 * No constructor.
	 */
	private PropertyServiceFunctionUtilityElementIndex() {
	}

	/**
	 * Properties of the element index function.
	 * 
	 * @author Fedor Smirnov
	 */
	protected enum Property {
		/**
		 * String describing the subcollection operation done by this node
		 */
		SubCollectionString
	}

	/**
	 * Creates a function node modeling the element index operation (described by
	 * the given subcollection) applied to the data node with the given id.
	 * 
	 * @param dataId         the given data id
	 * @param subCollections the subcollections to annotate
	 * @return a function node modeling the element index operation (described by
	 *         the given subcollection) applied to the data node with the given id.
	 */
	public static Task createElementIndexTask(final String dataId, final String subCollectionsString) {
		final String taskId = dataId + ConstantsEEModel.DependencyAffix + ConstantsEEModel.EIdxName
				+ ConstantsEEModel.DependencyAffix + subCollectionsString;
		final Task result = new Task(taskId);
		PropertyServiceFunction.setType(FunctionType.Utility, result);
		PropertyServiceFunctionUtility.setUtilityType(result, UtilityType.ElementIndex);
		setSubCollectionsString(result, subCollectionsString);
		return result;
	}

	/**
	 * Annotates the given subcollections onto the given node.
	 * 
	 * @param task           the given task
	 * @param subCollections the subcollections to annotate
	 */
	protected static void setSubCollectionsString(final Task task, final String subCollectionsString) {
		checkTask(task);
		final String attrName = Property.SubCollectionString.name();
		task.setAttribute(attrName, subCollectionsString);
	}

	/**
	 * Returns the subCollections annotated on the given node
	 * 
	 * @param task the given node
	 * @return the subCollections annotated on the given node
	 */
	public static String getSubCollectionsString(final Task task) {
		checkTask(task);
		final String attrName = Property.SubCollectionString.name();
		return (String) getAttribute(task, attrName);
	}

	/**
	 * Checks whether the task is an element index task. Throws exception otherwise.
	 * 
	 * @param task the given task
	 */
	protected static void checkTask(final Task task) {
		PropertyServiceFunctionUtility.checkTask(task);
		if (!PropertyServiceFunctionUtility.getUtilityType(task).equals(UtilityType.ElementIndex)) {
			throw new IllegalArgumentException("The task " + task.getId() + " is not an element index task.");
		}
	}
}
