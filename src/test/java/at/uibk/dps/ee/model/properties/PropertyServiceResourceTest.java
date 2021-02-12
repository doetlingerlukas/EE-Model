package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;
import org.junit.Test;
import at.uibk.dps.ee.model.properties.PropertyServiceResource.ResourceType;
import net.sf.opendse.model.Resource;

public class PropertyServiceResourceTest {

  @Test
  public void testCreateResource() {
    String id = "resId";
    ResourceType type = ResourceType.Local;
    Resource result = PropertyServiceResource.createResource(id, type);
    assertEquals(id, result.getId());
    assertEquals(type, PropertyServiceResource.getResourceType(result));
  }

}
