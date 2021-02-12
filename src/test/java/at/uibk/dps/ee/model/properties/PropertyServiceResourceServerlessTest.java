package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;
import org.junit.Test;
import at.uibk.dps.ee.model.properties.PropertyServiceResource.ResourceType;
import net.sf.opendse.model.Resource;

public class PropertyServiceResourceServerlessTest {

  @Test(expected = IllegalArgumentException.class)
  public void testCheckResource() {
    Resource res = PropertyServiceResource.createResource("bla", ResourceType.Local);
    PropertyServiceResourceServerless.checkResource(res);
  }

  @Test
  public void testCreateServerless() {
    String id = "id";
    String uri = "www.bla.com";
    Resource result = PropertyServiceResourceServerless.createServerlessResource(id, uri);
    assertEquals(id, result.getId());
    assertEquals(uri, PropertyServiceResourceServerless.getUri(result));
  }
}
