package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceResource.ResourceType;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container offering access to the attributes of the resource
 * nodes modeling serverless resources.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceResourceServerless extends AbstractPropertyService {

  /**
   * No constructor
   */
  private PropertyServiceResourceServerless() {}

  /**
   * Properties defining the attributes of serverless resources.
   * 
   * @author Fedor Smirnov
   */
  protected enum Property {
    /**
     * The URI to the serverless function.
     */
    Uri
  }

  public static final String propNameUri = Property.Uri.name();


  /**
   * Creates a serverless resource with the provided resource link and ID.
   * 
   * @param id the provided ID
   * @param resourceLink the resource link
   * @return a serverless resource with the provided resource link and ID
   */
  public static Resource createServerlessResource(final String id, final String resourceLink) {
    final Resource result = new Resource(id);
    PropertyServiceResource.setResourceType(result, ResourceType.Serverless);
    setUri(result, resourceLink);
    return result;
  }

  /**
   * Returns the resource link of the given resource.
   * 
   * @param res the given resource
   * @return the resource link of the given resource
   */
  public static String getUri(final Resource res) {
    checkResource(res);
    return (String) getAttribute(res, propNameUri);
  }

  /**
   * Sets the resource link for the provided resource.
   * 
   * @param res the provided resource
   * @param resourceLink the link to set
   */
  protected static void setUri(final Resource res, final String resourceLink) {
    checkResource(res);
    res.setAttribute(propNameUri, resourceLink);
  }

  /**
   * Checks whether provided resource is serverless. Throws exception otherwise.
   * 
   * @param res the provided resource.
   */
  protected static void checkResource(final Resource res) {
    if (!PropertyServiceResource.getResourceType(res).equals(ResourceType.Serverless)) {
      throw new IllegalArgumentException("Resource " + res + " is not a serverless resource.");
    }
  }
}
