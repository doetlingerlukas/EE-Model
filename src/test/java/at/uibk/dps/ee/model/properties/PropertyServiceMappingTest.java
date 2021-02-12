package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;
import org.junit.Test;
import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

public class PropertyServiceMappingTest {

  @Test
  public void testCreateMapping() {
    String resId = "res";
    String taskId = "task";
    Resource res = new Resource(resId);
    Task task = new Task(taskId);
    String expectedId = taskId + ConstantsEEModel.KeywordSeparator1 + resId;
    Mapping<Task, Resource> result = PropertyServiceMapping.createMapping(task, res);
    assertEquals(expectedId, result.getId());
    assertEquals(task, result.getSource());
    assertEquals(res, result.getTarget());
  }
}
