package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.JsonPrimitive;
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
    assertEquals(3, PropertyServiceResourceServerless.getTimeoutInSeconds(result));
    PropertyServiceResourceServerless.setTimeoutInSeconds(result, 5);
    assertEquals(5, PropertyServiceResourceServerless.getTimeoutInSeconds(result));
    result.setAttribute(PropertyServiceResourceServerless.propNameTimeout, new JsonPrimitive(10));
    assertEquals(10, PropertyServiceResourceServerless.getTimeoutInSeconds(result));
  }
}
