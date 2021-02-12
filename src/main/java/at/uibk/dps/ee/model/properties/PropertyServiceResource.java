package at.uibk.dps.ee.model.properties;

import net.sf.opendse.model.Resource;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static container with methods offering convenient access to the attributes of
 * resource nodes.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceResource extends AbstractPropertyService {

  private static final String propNameType = Property.Type.name();

  /**
   * No constructor.
   */
  private PropertyServiceResource() {}

  /**
   * Defines the properties shared by all resource nodes.
   * 
   * @author Fedor Smirnov
   */
  protected enum Property {
    /**
     * The type of the resource.
     */
    Type
  }

  /**
   * The general types of resources.
   * 
   * @author Fedor Smirnov
   */
  public enum ResourceType {
    /**
     * Models an execution on the local resource (the resource running the EE).
     */
    Local,
    /**
     * Models an execution via a serverless function.
     */
    Serverless
  }

  /**
   * Creates a resource node with the provided ID and the provided type.
   * 
   * @param resId the resource id
   * @param type the type of the resource
   * @return a resource node with the provided ID and the provided type
   */
  public static Resource createResource(final String resId, final ResourceType type) {
    final Resource result = new Resource(resId);
    setResourceType(result, type);
    return result;
  }

  /**
   * Sets the type for the provided resource.
   * 
   * @param res the provided resource
   * @param type the type to set
   */
  protected static void setResourceType(final Resource res, final ResourceType type) {
    res.setAttribute(propNameType, type.name());
  }

  /**
   * Returns the resource type of the provided resource.
   * 
   * @param res the provided resource
   * @return the type of the resource
   */
  public static ResourceType getResourceType(final Resource res) {
    return ResourceType.valueOf((String) getAttribute(res, propNameType));
  }

}
