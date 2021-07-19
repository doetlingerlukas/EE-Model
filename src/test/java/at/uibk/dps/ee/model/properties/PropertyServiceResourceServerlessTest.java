package at.uibk.dps.ee.model.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonPrimitive;
import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import net.sf.opendse.model.Resource;

public class PropertyServiceResourceServerlessTest {

  @Test
  public void testCreateServerless() {
    String id = "id";
    String uri = "www.bla.com";
    Resource result = PropertyServiceResourceServerless.createServerlessResource(id, uri);
    assertEquals(id, result.getId());
    assertEquals(uri, PropertyServiceResourceServerless.getUri(result));
    assertEquals(ConstantsEEModel.defaultFaaSTimeoutSeconds,
        PropertyServiceResourceServerless.getTimeoutInSeconds(result));
    PropertyServiceResourceServerless.setTimeoutInSeconds(result, 5);
    assertEquals(5, PropertyServiceResourceServerless.getTimeoutInSeconds(result));
    result.setAttribute(PropertyServiceResourceServerless.propNameTimeout, new JsonPrimitive(10));
    assertEquals(10, PropertyServiceResourceServerless.getTimeoutInSeconds(result));
  }
}
