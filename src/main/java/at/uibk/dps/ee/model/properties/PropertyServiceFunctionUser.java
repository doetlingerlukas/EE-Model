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

  private static final String propNameFunctionType = Property.TypeID.name();
  private static final String propNameWhileRef = Property.WhileRef.name();

  /**
   * No constructor.
   */
  private PropertyServiceFunctionUser() {}

  /**
   * Defines the relevant attributes of user functions.
   * 
   * @author Fedor Smirnov
   */
  protected enum Property {
    /**
     * The type of the function.
     */
    TypeID,
    /**
     * User to distinguis seqReplicas from the original task (so that they can have
     * the same scheduling options)
     */
    WhileRef
  }

  /**
   * Sets the reference to the while task for the given while replica.
   * 
   * @param task the given task
   * @param originalRef the reference to the while task
   */
  public static void setWhileRef(Task task, String originalRef) {
    checkTask(task);
    task.setAttribute(propNameWhileRef, originalRef);
  }

  /**
   * Returns the original ref of the given task.
   * 
   * @param task the given task
   * @return the reference to the original task
   */
  public static String getOriginalRef(Task task) {
    checkTask(task);
    if (!isSeqReplica(task)) {
      throw new IllegalArgumentException("task " + task + " is not a sequential replica.");
    }
    return (String) getAttribute(task, propNameWhileRef);
  }

  /**
   * Returs true if the given task is a sequential replica.
   * 
   * @param task the given task
   * @return true iff the given task is a sequential replica
   */
  public static boolean isSeqReplica(Task task) {
    checkTask(task);
    return isAttributeSet(task, propNameWhileRef);
  }

  /**
   * Creates a task node with the given ID, modeling a user function of the
   * provided type.
   * 
   * @param taskId the id of the task to create
   * @param functionTypeString the function type string
   * @return a task node with the given ID, modeling a user function of the
   *         provided type
   */
  public static Task createUserTask(final String taskId, final String functionTypeString) {
    final Task result = new Task(taskId);
    PropertyServiceFunction.setUsageType(UsageType.User, result);
    setTypeId(result, functionTypeString);
    return result;
  }

  /**
   * Returns the function type string for the given task.
   * 
   * @param task the given task
   * @return the function type string for the given task
   */
  public static String getTypeId(final Task task) {
    return (String) getAttribute(task, propNameFunctionType);
  }

  /**
   * Sets the function type for the provided task.
   * 
   * @param task the provided task
   * @param functionTypeString the function type string to set
   */
  protected static void setTypeId(final Task task, final String functionTypeString) {
    checkTask(task);
    task.setAttribute(propNameFunctionType, functionTypeString);
  }

  /**
   * Checks that the provided task is a user task. Throws an exception otherwise.
   * 
   * @param task the provided task
   */
  protected static void checkTask(final Task task) {
    PropertyServiceFunction.checkTask(task);
    if (!PropertyServiceFunction.getUsageType(task).equals(UsageType.User)) {
      throw new IllegalArgumentException("The task " + task.getId() + " is not a user task.");
    }
  }
}
