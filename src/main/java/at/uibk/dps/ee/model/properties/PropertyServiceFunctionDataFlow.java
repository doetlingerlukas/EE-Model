package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container to access properties of data flow function nodes.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionDataFlow extends AbstractPropertyService {

  /**
   * No constructor.
   */
  private PropertyServiceFunctionDataFlow() {}

  /**
   * Properties defining attribute names.
   * 
   * @author Fedor Smirnov
   *
   */
  protected enum Property {
    /**
     * The type of the data flow function
     */
    DataFlowType
  }

  /**
   * Types of data flow functions.
   * 
   * @author Fedor Smirnov
   *
   */
  public enum DataFlowType {
    /**
     * Forwards the earliest available input to all outputs.
     */
    EarliestInput,
    /**
     * A 2:1 multiplexer operating based on a boolean variable.
     */
    Multiplexer,
    /**
     * Operations for collection distribution and aggregation
     */
    Collections,
  }

  /**
   * Returns true if the given task is a multiplexer node.
   * 
   * @param task the given task
   * @return true if the given task is a multiplexer node
   */
  public static boolean isMultiplexerNode(final Task task) {
    return TaskPropertyService.isProcess(task)
        && PropertyServiceFunction.getUsageType(task).equals(UsageType.DataFlow)
        && getDataFlowType(task).equals(DataFlowType.Multiplexer);
  }

  /**
   * Creates a data flow function with the given ID and the given function type
   * 
   * @param taskId the id of the task to create
   * @param dataFlowType the data flow type
   * @return a data flow function with the given ID and the given function type
   */
  public static Task createDataFlowFunction(final String taskId, final DataFlowType dataFlowType) {
    final Task result = new Task(taskId);
    PropertyServiceFunction.setUsageType(UsageType.DataFlow, result);
    PropertyServiceFunctionDataFlow.setDataFlowType(result, dataFlowType);
    return result;
  }

  /**
   * Returns the data flow type for the given task.
   * 
   * @param task the given task
   * @return the data flow type for the given task
   */
  public static DataFlowType getDataFlowType(final Task task) {
    checkTask(task);
    final String attrName = Property.DataFlowType.name();
    return DataFlowType.valueOf((String) getAttribute(task, attrName));
  }

  /**
   * Sets the data flow type for the given task.
   * 
   * @param task the given task
   * @param type the type to set
   */
  public static void setDataFlowType(final Task task, final DataFlowType type) {
    checkTask(task);
    final String attrName = Property.DataFlowType.name();
    task.setAttribute(attrName, type.name());
  }

  /**
   * Checks that the given task is a data flow task throws an exception otherwise.
   * 
   * @param task the task to check
   */
  protected static void checkTask(final Task task) {
    PropertyServiceFunction.checkTask(task);
    if (!PropertyServiceFunction.getUsageType(task).equals(UsageType.DataFlow)) {
      throw new IllegalArgumentException("Task " + task.getId() + " is not a data flow task");
    }
  }
}
