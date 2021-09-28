package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.properties.PropertyServiceMapping.EnactmentMode;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Method container for conveniently handling the properties of local type
 * mappings.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceMappingLocal extends AbstractPropertyService {

  public static final String propNameImage = Property.Image.name();
  private static final String macAddress = getMacAddress();

  /**
   * Properties of the local mappings.
   * 
   * @author Fedor Smirnov
   *
   */
  protected enum Property {
    /**
     * The name of the Docker image used to implement the function.
     */
    Image
  }

  /**
   * Creates a local mapping for a container with the provided image name.
   * 
   * @param src the source task (function type)
   * @param dst the target resource (the ee resource)
   * @param imageName the name of the image used in the container
   * @return a local mapping for a container with the provided image name
   */
  public static Mapping<Task, Resource> createMappingLocal(final Task src, final Resource dst,
      final String imageName) {
    final String implId = macAddress + "--" + imageName;
    final String mappingId =
        PropertyServiceMapping.getMappingId(src, dst, implId) + "-" + imageName;
    final Mapping<Task, Resource> result =
        PropertyServiceMapping.createMapping(src, dst, EnactmentMode.Local, implId, mappingId);
    setImageName(result, imageName);
    return result;
  }

  /**
   * Sets the image name for the given mapping.
   * 
   * @param mapping the given mapping
   * @param imageName the image name
   */
  static void setImageName(final Mapping<Task, Resource> mapping,
      final String imageName) {
    checkMapping(mapping);
    mapping.setAttribute(propNameImage, imageName);
  }

  /**
   * Returns the image name for the provided mapping.
   * 
   * @param mapping the provided mapping.
   */
  public static String getImageName(final Mapping<Task, Resource> mapping) {
    checkMapping(mapping);
    return (String) getAttribute(mapping, propNameImage);
  }

  /**
   * Checks whether the provided mapping maps onto a local resource.
   * 
   * @param mapping the provided mapping
   */
  static void checkMapping(final Mapping<Task, Resource> mapping) {
    if (!PropertyServiceMapping.getEnactmentMode(mapping).equals(EnactmentMode.Local)) {
      throw new IllegalArgumentException("The mapping " + mapping + " is not a local mapping.");
    }
  }

  /**
   * Returns a string representation of Apollo's host machine.
   * 
   * @return a string representation of Apollo's host machine
   */
  static String getMacAddress() {
    System.err.println("Need to implement a proper way to get the MAC address");
    // just for now
    return "myMachine";
  }

  /**
   * No constructor.
   */
  private PropertyServiceMappingLocal() {}
}
