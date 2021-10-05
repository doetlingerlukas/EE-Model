package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container for characterizing the function nodes used to model
 * the end of a while compound.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionUtilityWhile extends AbstractPropertyService {

  private final static String propNameWhileStart = Properties.WhileStartRef.name();
  private final static String propNameWhileCounter = Properties.WhileCounterRef.name();

  /**
   * No constructor.
   */
  private PropertyServiceFunctionUtilityWhile() {}

  /**
   * Properties annotated on nodes implementing the while functionality.
   * 
   * @author Fedor Smirnov
   */
  public enum Properties {
    /**
     * Reference to the data node modeling the start of the while body
     */
    WhileStartRef,
    /**
     * Reference to the data node containing the loop count
     */
    WhileCounterRef
  }



  /**
   * Returns true iff the provided task models a while end operation.
   * 
   * @param task the provided task
   * @return true iff the provided task models a while end operation
   */
  public static boolean isWhileEndTask(final Task task) {
    return TaskPropertyService.isProcess(task)
        && PropertyServiceFunction.getUsageType(task).equals(UsageType.Utility)
        && PropertyServiceFunctionUtility.getUtilityType(task).equals(UtilityType.While);
  }

  /**
   * Creates a node modeling the end of the while loop started by the given node.
   * 
   * @param whileStart the node at the start of the while loop
   * @return node modeling the end of the while loop started by the given node
   */
  public static Task createWhileEndTask(final Task whileStart, final Task whileCounter) {
    final String nodeId =
        whileStart.getId() + ConstantsEEModel.KeywordSeparator1 + ConstantsEEModel.WhileEndSuffix;
    final Task result = new Task(nodeId);
    PropertyServiceFunction.setUsageType(UsageType.Utility, result);
    PropertyServiceFunctionUtility.setUtilityType(result, UtilityType.While);
    setWhileCounterReference(result, whileCounter);
    setWhileStart(result, whileStart.getId());
    return result;
  }

  /**
   * Sets the reference to the while counter.
   * 
   * @param whileEndTask the while end task
   * @param whileCounter the while counter
   */
  static void setWhileCounterReference(final Task whileEndTask, final Task whileCounter) {
    checkTask(whileEndTask);
    whileEndTask.setAttribute(propNameWhileCounter, whileCounter.getId());
  }

  /**
   * Returns the reference to the while counter.
   * 
   * @param whileEndTask the while end node
   * @return the reference to the while counter
   */
  public static String getWhileCounterReference(final Task whileEndTask) {
    checkTask(whileEndTask);
    return (String) getAttribute(whileEndTask, propNameWhileCounter);
  }

  /**
   * Returns the ID of the while start annotated at the given while end node.
   * 
   * @param whileEnd the given while end node
   * @return the ID of the while start annotated at the given while end node
   */
  public static String getWhileStart(final Task whileEnd) {
    checkTask(whileEnd);
    return (String) getAttribute(whileEnd, propNameWhileStart);
  }


  /**
   * Annotates the given whileEnd task with a reference to the given whileStart
   * task.
   * 
   * @param whileEnd the while end task
   * @param whileStartID the id of the while start task
   */
  public static void setWhileStart(final Task whileEnd, final String whileStartID) {
    checkTask(whileEnd);
    whileEnd.setAttribute(propNameWhileStart, whileStartID);
  }

  /**
   * Checks whether the given task models the end of a while compound.
   * 
   * @param task the given task
   */
  static void checkTask(final Task task) {
    PropertyServiceFunctionUtility.checkTask(task);
    if (!PropertyServiceFunctionUtility.getUtilityType(task).equals(UtilityType.While)) {
      throw new IllegalArgumentException(
          "The task " + task.getId() + " does not model the end of a while compound");
    }
  }
}
