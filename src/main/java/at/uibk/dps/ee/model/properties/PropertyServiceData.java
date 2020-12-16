package at.uibk.dps.ee.model.properties;

import com.google.gson.JsonObject;

import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container for the properties of the {@link Communication} nodes
 * modeling the data packages consumed and produced by functions as inputs and
 * outputs, respectively.
 * 
 * @author Fedor Smirnov
 *
 */
public class PropertyServiceData extends AbstractPropertyService {

	private PropertyServiceData() {
	}

	public enum Property {
		/**
		 * Was the data already produced?
		 */
		DataAvailable,
		/**
		 * The data
		 */
		Content
	}

	/**
	 * Annotates the given content to the given data node.
	 * 
	 * @param task    the given data node
	 * @param content the content to annotate
	 */
	public static void setContent(Task task, JsonObject content) {
		checkTask(task);
		String attrNameContent = Property.Content.name();
		task.setAttribute(attrNameContent, content);
		String attrNameAval = Property.DataAvailable.name();
		task.setAttribute(attrNameAval, true);
	}

	/**
	 * Returns the data content of the given data node. Throws an exception if no
	 * data annotated.
	 * 
	 * @param task the given task
	 * @return the data content of the given data node
	 */
	public static JsonObject getContent(Task task) {
		checkTask(task);
		String attrName = Property.Content.name();
		checkAttribute(task, attrName);
		return (JsonObject) getAttribute(task, attrName);
	}

	/**
	 * Returns <code>true</code> if the data has been annotated as available.
	 * 
	 * @param task the data task to check
	 * @return <code>true</code> if the data has been annotated as available
	 */
	public static boolean isDataAvailable(Task task) {
		checkTask(task);
		String attrName = Property.DataAvailable.name();
		if (!isAttributeSet(task, attrName)) {
			return false;
		}
		return (boolean) getAttribute(task, attrName);
	}

	/**
	 * Checks whether a given task is processable by this service. Throws an
	 * exception in case it is not.
	 * 
	 * @param task the task to check
	 */
	protected static void checkTask(Task task) {
		if (!TaskPropertyService.isCommunication(task)) {
			throw new IllegalArgumentException("Task " + task.getId() + " does not model data.");
		}
	}
}
