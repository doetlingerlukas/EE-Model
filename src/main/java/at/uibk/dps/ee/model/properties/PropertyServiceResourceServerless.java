package at.uibk.dps.ee.model.properties;

import com.google.gson.JsonElement;
import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container offering access to the attributes of the resource
 * nodes modeling serverless resources.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceResourceServerless extends AbstractPropertyService {

  public static final String propNameUri = Property.Uri.name();
  public static final String propNameTimeout = Property.TimeoutInSeconds.name();

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
    Uri,
    /**
     * The timeout configured for the function
     */
    TimeoutInSeconds
  }

  /**
   * Creates a serverless resource with the provided resource link and ID.
   * 
   * @param resId the provided ID
   * @param resourceLink the resource link
   * @return a serverless resource with the provided resource link and ID
   */
  public static Resource createServerlessResource(final String resId, final String resourceLink) {
    final Resource result = PropertyServiceResource.createResource(resId);
    setUri(result, resourceLink);
    return result;
  }

  /**
   * Returns the timeout set for the serverless resource or the default value if
   * no timeout is set.
   * 
   * @param res the given resource.
   * @return the timeout set for the serverless resource or the default value if
   *         no timeout is set
   */
  public static int getTimeoutInSeconds(final Resource res) {
    checkResource(res);
    if (isAttributeSet(res, propNameTimeout)) {
      final Object attr = getAttribute(res, propNameTimeout);
      if (attr instanceof JsonElement) {
        return ((JsonElement) attr).getAsInt();
      } else {
        return (int) attr;
      }
    } else {
      return ConstantsEEModel.defaultFaaSTimeoutSeconds;
    }
  }

  /**
   * Sets the timeout in seconds for the given resource.
   * 
   * @param res the given resource
   * @param timeoutInSeconds the timeout in seconds to set
   */
  static void setTimeoutInSeconds(final Resource res, final int timeoutInSeconds) {
    checkResource(res);
    res.setAttribute(propNameTimeout, timeoutInSeconds);
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
  static void setUri(final Resource res, final String resourceLink) {
    checkResource(res);
    res.setAttribute(propNameUri, resourceLink);
  }

  /**
   * Checks whether provided resource is serverless. Throws exception otherwise.
   * 
   * @param res the provided resource.
   */
  static void checkResource(final Resource res) {
    // this could be probably dropped
  }
}
