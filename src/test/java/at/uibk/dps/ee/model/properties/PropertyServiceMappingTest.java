package at.uibk.dps.ee.model.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceMapping.EnactmentMode;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

public class PropertyServiceMappingTest {

  @Test
  public void testIsCapacityRelevant() {
    Task negl = new Task("negligible");
    Task nonNegl = new Task("nonNegligible");
    PropertyServiceFunction.annotateNonNegligibleWorkload(nonNegl);
    Resource limited = new Resource("limited");
    Resource unlimited = new Resource("unlimited");
    PropertyServiceResource.annotateUnlimitedCapacity(unlimited);

    assertTrue(PropertyServiceMapping
        .isCapacityRelevant(new Mapping<Task, Resource>("m", nonNegl, limited)));
    assertFalse(
        PropertyServiceMapping.isCapacityRelevant(new Mapping<Task, Resource>("m", negl, limited)));
    assertFalse(PropertyServiceMapping
        .isCapacityRelevant(new Mapping<Task, Resource>("m", nonNegl, unlimited)));
    assertFalse(PropertyServiceMapping
        .isCapacityRelevant(new Mapping<Task, Resource>("m", negl, unlimited)));
  }

  @Test
  public void testCreateMapping() {
    String resId = "res";
    String taskId = "task";
    Resource res = new Resource(resId);
    Task task = new Task(taskId);
    EnactmentMode mode = EnactmentMode.Local;
    String implId = "impl1";

    String expectedId = taskId + ConstantsEEModel.KeywordSeparator1 + resId
        + ConstantsEEModel.KeywordSeparator1 + implId;
    Mapping<Task, Resource> result = PropertyServiceMapping.createMapping(task, res, mode, implId);
    assertEquals(expectedId, result.getId());
    assertEquals(task, result.getSource());
    assertEquals(res, result.getTarget());
    assertEquals(mode, PropertyServiceMapping.getEnactmentMode(result));
    assertEquals(implId, PropertyServiceMapping.getImplementationId(result));
  }
}
