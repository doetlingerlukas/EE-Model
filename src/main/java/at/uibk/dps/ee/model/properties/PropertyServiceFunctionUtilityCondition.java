package at.uibk.dps.ee.model.properties;

import java.util.List;

import at.uibk.dps.ee.model.objects.Condition;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
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
  private PropertyServiceFunctionUtilityCondition() {}

  /**
   * Properties defining attribute names
   * 
   * @author Fedor Smirnov
   *
   */
  protected enum Property {
    /**
     * The checked conditions
     */
    Conditions,    
  }

  /**
   * Creates a function task representing the evaluation of a condition.
   * 
   * @param taskId the task id
   * @param conditions the set of conditions
   * @return a function task representing the evaluation of a condition
   */
  public static Task createConditionEvaluation(final String taskId, final List<Condition> conditions) {
    final Task result = new Task(taskId);
    PropertyServiceFunction.setUsageType(UsageType.Utility, result);
    PropertyServiceFunctionUtility.setUtilityType(result, UtilityType.Condition);
    setConditions(result, conditions);
    return result;
  }

  /**
   * Returns the set of conditions annotated on the provided task.
   * 
   * @param task the provided task
   * @return the set of conditions annotated on the provided task
   */
  @SuppressWarnings("unchecked")
  public static List<Condition> getConditions(final Task task) {
    checkTask(task);
    final String attrName = Property.Conditions.name();
    return (List<Condition>) getAttribute(task, attrName);
  }

  /**
   * Annotates the provided condition task with the given set of conditions.
   * 
   * @param task the condition task
   * @param conditions the set of conditions to annotate
   */
  public static void setConditions(final Task task, final List<Condition> conditions) {
    checkTask(task);
    final String attrName = Property.Conditions.name();
    task.setAttribute(attrName, conditions);
  }

  /**
   * Checks a given task. Throws an exception if it is not a condition node.
   * 
   * @param task the task to check.
   */
  static void checkTask(final Task task) {
    if (!PropertyServiceFunctionUtility.getUtilityType(task).equals(UtilityType.Condition)) {
      throw new IllegalArgumentException("Task " + task.getId() + " is not a condition node.");
    }
  }
}
