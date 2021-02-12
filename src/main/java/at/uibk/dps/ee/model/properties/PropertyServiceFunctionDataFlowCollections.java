package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionDataFlow.DataFlowType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container for methods used to control the properties of the
 * function nodes modeling the distribution and aggregation of collection data.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionDataFlowCollections extends AbstractPropertyService {

  /**
   * No constructor.
   */
  private PropertyServiceFunctionDataFlowCollections() {}

  /**
   * Properties defining attribute names.
   * 
   * @author Fedor Smirnov
   */
  protected enum Property {
    /**
     * Type of the operation.
     */
    OperationType,
    /**
     * The compound defining the scope between the distribution and the aggregation.
     */
    Scope,
    /**
     * The number of iterations.
     */
    IterationNumber
  }

  /**
   * Type of the data flow operation
   * 
   * @author Fedor Smirnov
   *
   */
  public enum OperationType {
    Distribution, Aggregation
  }

  /**
   * Creates a task with the given ID, modeling the requested collection data flow
   * operation.
   * 
   * @param taskId the given task id
   * @param operationType the requested operation
   * @return a task with the given ID, modeling the requested collection data flow
   *         operation
   */
  public static Task createCollectionDataFlowTask(final String taskId,
      final OperationType operationType, final String scope) {
    final Task result =
        PropertyServiceFunctionDataFlow.createDataFlowFunction(taskId, DataFlowType.Collections);
    setOperationType(result, operationType);
    setScope(result, scope);
    return result;
  }

  /**
   * Sets the iteration number for the provided task.
   * 
   * @param task the provided task
   * @param iterationNumber the iteration number
   */
  public static void setIterationNumber(final Task task, final int iterationNumber) {
    if (!isDistributionNode(task)) {
      throw new IllegalArgumentException("Task " + task.getId() + " is not a distribution node.");
    }
    final String attrName = Property.IterationNumber.name();
    task.setAttribute(attrName, iterationNumber);
  }

  /**
   * Returns the iteration number annotated at the given task.
   * 
   * @param task the given task
   * @return the iteration number annotated at the given task
   */
  public static int getIterationNumber(final Task task) {
    if (!isDistributionNode(task)) {
      throw new IllegalArgumentException("Task " + task.getId() + " is not a distribution node.");
    }
    final String attrName = Property.IterationNumber.name();
    return (int) getAttribute(task, attrName);
  }

  /**
   * Returns true iff the given node is an aggregation node.
   * 
   * @param task the given task
   * @return true iff the given node is an aggregation node
   */
  public static boolean isAggregationNode(final Task task) {
    return TaskPropertyService.isProcess(task)
        && PropertyServiceFunction.getUsageType(task).equals(UsageType.DataFlow)
        && PropertyServiceFunctionDataFlow.getDataFlowType(task).equals(DataFlowType.Collections)
        && PropertyServiceFunctionDataFlowCollections.getOperationType(task)
            .equals(OperationType.Aggregation);
  }

  /**
   * Returns true iff the given node is an distribution node.
   * 
   * @param task the given task
   * @return true iff the given node is an distribution node
   */
  public static boolean isDistributionNode(final Task task) {
    return TaskPropertyService.isProcess(task)
        && PropertyServiceFunction.getUsageType(task).equals(UsageType.DataFlow)
        && PropertyServiceFunctionDataFlow.getDataFlowType(task).equals(DataFlowType.Collections)
        && PropertyServiceFunctionDataFlowCollections.getOperationType(task)
            .equals(OperationType.Distribution);
  }

  /**
   * Sets the given operation type for the givent task.
   * 
   * @param task the given task
   * @param operationType the given operation type
   */
  protected static void setOperationType(final Task task, final OperationType operationType) {
    checkTask(task);
    final String attrName = Property.OperationType.name();
    task.setAttribute(attrName, operationType.name());
  }

  /**
   * Returns the operation type for the given task.
   * 
   * @param task the given task
   * @return the operation type of the given task
   */
  public static OperationType getOperationType(final Task task) {
    checkTask(task);
    final String attrName = Property.OperationType.name();
    return OperationType.valueOf((String) getAttribute(task, attrName));
  }

  /**
   * Returns the scope for the given node.
   * 
   * @param task the given task
   * @return the scope string
   */
  public static String getScope(final Task task) {
    checkTask(task);
    final String attrName = Property.Scope.name();
    return (String) getAttribute(task, attrName);
  }

  /**
   * Sets the scope for the given task.
   * 
   * @param task the given task
   * @param scope the scope
   */
  protected static void setScope(final Task task, final String scope) {
    checkTask(task);
    final String attrName = Property.Scope.name();
    task.setAttribute(attrName, scope);
  }

  /**
   * Checks that the given task is a collection operation.
   * 
   * @param task the given task
   */
  protected static void checkTask(final Task task) {
    PropertyServiceFunctionDataFlow.checkTask(task);
    if (!PropertyServiceFunctionDataFlow.getDataFlowType(task).equals(DataFlowType.Collections)) {
      throw new IllegalArgumentException(
          "Task " + task.getId() + " does not model a collection data flow operation.");
    }
  }
}
