package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Contains convenience methods to access the properties of the nodes modeling
 * utility functions.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionUtility extends AbstractPropertyService {

  /**
   * No constructor.
   */
  private PropertyServiceFunctionUtility() {}

  /**
   * Properties defining attribute names
   * 
   * @author Fedor Smirnov
   *
   */
  protected enum Property {
    /**
     * The type of utility function
     */
    UtilityType
  }

  /**
   * Types of utility functions
   * 
   * @author Fedor Smirnov
   *
   */
  public enum UtilityType {
    /**
     * Evaluating boolean conditions.
     */
    Condition, CollectionOperation, While
  }

  /**
   * Annotates the given task with the given utility type.
   * 
   * @param task the task to annotate
   * @param type the type of the task
   */
  protected static void setUtilityType(final Task task, final UtilityType type) {
    checkTask(task);
    final String attrName = Property.UtilityType.name();
    task.setAttribute(attrName, type.name());
  }

  /**
   * Returns the utility type of the given task.
   * 
   * @param task the given task
   * @return the utility type of the given task
   */
  public static UtilityType getUtilityType(final Task task) {
    checkTask(task);
    final String attrName = Property.UtilityType.name();
    return UtilityType.valueOf((String) getAttribute(task, attrName));
  }

  /**
   * Checks that the given task is a utility function. Throws an exception
   * otherwise.
   * 
   * @param task the task to check
   */
  protected static void checkTask(final Task task) {
    PropertyServiceFunction.checkTask(task);
    if (!PropertyServiceFunction.getUsageType(task).equals(UsageType.Utility)) {
      throw new IllegalArgumentException("Task " + task.getId() + " is not a utility function.");
    }
  }
}
