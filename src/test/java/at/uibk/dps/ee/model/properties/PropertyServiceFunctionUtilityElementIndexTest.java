package at.uibk.dps.ee.model.properties;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceFunction.UsageType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtility.UtilityType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionUtilityCollections.CollectionOperation;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionUtilityElementIndexTest {

  @Test
  public void testCreateTask() {
    String dataId = "data";
    String collId = "subcollection";

    CollectionOperation operation = CollectionOperation.ElementIndex;

    String expectedId = dataId + ConstantsEEModel.KeywordSeparator1 + operation
        + ConstantsEEModel.KeywordSeparator1 + collId;

    Task result = PropertyServiceFunctionUtilityCollections.createCollectionOperation(dataId,
        collId, operation);
    assertEquals(UtilityType.CollectionOperation,
        PropertyServiceFunctionUtility.getUtilityType(result));
    assertEquals(collId, PropertyServiceFunctionUtilityCollections.getSubCollectionsString(result));
    assertEquals(expectedId, result.getId());
    assertEquals(operation,
        PropertyServiceFunctionUtilityCollections.getCollectionOperation(result));
  }

  @Test
  public void testGetSetSubCollections() {
    Task task = new Task("task");
    PropertyServiceFunction.setUsageType(UsageType.Utility, task);
    PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.CollectionOperation);
    String string = "bla";
    PropertyServiceFunctionUtilityCollections.setSubCollectionsString(task, string);
    assertEquals(string, PropertyServiceFunctionUtilityCollections.getSubCollectionsString(task));
  }

  @Test
  public void testCheckTask() {
    assertThrows(IllegalArgumentException.class, () -> {
      Task task = new Task("task");
      PropertyServiceFunction.setUsageType(UsageType.Utility, task);
      PropertyServiceFunctionUtility.setUtilityType(task, UtilityType.Condition);
      PropertyServiceFunctionUtilityCollections.checkTask(task);
    });
  }
}
