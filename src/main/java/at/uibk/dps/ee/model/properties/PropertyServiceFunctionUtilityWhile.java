package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container for characterizing the function nodes used to model
 * the end of a while compound.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceFunctionUtilityWhile extends AbstractPropertyService {

  protected final static String propNameWhileStart = Properties.WhileStart.name();

  /**
   * No constructor.
   */
  private PropertyServiceFunctionUtilityWhile() {}


  protected enum Properties {
    WhileStart
  }

  /**
   * Creates a node modeling the end of the while loop started by the given node.
   * 
   * @param whileStart the node at the start of the while loop
   * @return node modeling the end of the while loop started by the given node
   */
  public static Task createWhileEndTask(Task whileStart) {
    String nodeId =
        whileStart.getId() + ConstantsEEModel.KeywordSeparator1 + ConstantsEEModel.WhileEndSuffix;
    Task result = new Task(nodeId);
    PropertyServiceFunction.setUsageType(UsageType.Utility, result);
    PropertyServiceFunctionUtility.setUtilityType(result, UtilityType.While);
    setWhileStart(result, whileStart);
    return result;
  }

  /**
   * Returns the ID of the while start annotated at the given while end node.
   * 
   * @param whileEnd the given while end node
   * @return the ID of the while start annotated at the given while end node
   */
  protected static String getWhileStart(Task whileEnd) {
    checkTask(whileEnd);
    return (String) getAttribute(whileEnd, propNameWhileStart);
  }

  /**
   * Annotates the given whileEnd task with a reference to the given whileStart
   * task.
   * 
   * @param whileEnd the while end task
   * @param whileStart the while start task
   */
  protected static void setWhileStart(Task whileEnd, Task whileStart) {
    checkTask(whileEnd);
    whileEnd.setAttribute(propNameWhileStart, whileStart.getId());
  }

  /**
   * Checks whether the given task models the end of a while compound.
   * 
   * @param task the given task
   */
  protected static void checkTask(Task task) {
    PropertyServiceFunctionUtility.checkTask(task);
    if (!PropertyServiceFunctionUtility.getUtilityType(task).equals(UtilityType.While)) {
      throw new IllegalArgumentException(
          "The task " + task.getId() + " does not model the end of a while compound");
    }
  }
}
