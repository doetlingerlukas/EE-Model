package at.uibk.dps.ee.model.properties;

import java.net.NetworkInterface;
import java.net.SocketException;
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
  protected static final String macAddress = getMacAddress();

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
  public static Mapping<Task, Resource> createMappingLocal(Task src, Resource dst,
      String imageName) {
    String implId = macAddress + "--" + imageName;
    String mappingId = PropertyServiceMapping.getMappingId(src, dst) + "-" + imageName;
    Mapping<Task, Resource> result =
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
  protected static void setImageName(Mapping<Task, Resource> mapping, String imageName) {
    checkMapping(mapping);
    mapping.setAttribute(propNameImage, imageName);
  }

  /**
   * Returns the image name for the provided mapping.
   * 
   * @param mapping the provided mapping.
   */
  public static String getImageName(Mapping<Task, Resource> mapping) {
    checkMapping(mapping);
    return (String) getAttribute(mapping, propNameImage);
  }

  /**
   * Checks whether the provided mapping maps onto a local resource.
   * 
   * @param mapping the provided mapping
   */
  protected static void checkMapping(Mapping<Task, Resource> mapping) {
    if (!PropertyServiceMapping.getEnactmentMode(mapping).equals(EnactmentMode.Local)) {
      throw new IllegalArgumentException("The mapping " + mapping + " is not a local mapping.");
    }
  }

  /**
   * Returns a string representation of Apollo's host machine.
   * 
   * @return a string representation of Apollo's host machine
   */
  protected static String getMacAddress() {
    try {
      NetworkInterface ni = NetworkInterface.getNetworkInterfaces().nextElement();
      byte[] hardwareAddress = ni.getHardwareAddress();
      String[] hexadecimal = new String[hardwareAddress.length];
      for (int i = 0; i < hardwareAddress.length; i++) {
        hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
      }
      return String.join("-", hexadecimal);
    } catch (SocketException e) {
      throw new IllegalStateException("Mac address of the host could not be determined.");
    }
  }

  /**
   * No constructor.
   */
  private PropertyServiceMappingLocal() {}
}
