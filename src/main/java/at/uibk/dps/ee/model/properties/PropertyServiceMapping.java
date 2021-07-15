package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static method container providing access to the attributes of the mapping
 * edges.
 * 
 * @author Fedor Smirnov
 *
 */
public final class PropertyServiceMapping extends AbstractPropertyService {

  private static final String propNameEnactmentMode = Property.EnactmentMode.name();
  private static final String propNameImplId = Property.ImplementationId.name();

  /**
   * No constructor.
   */
  private PropertyServiceMapping() {}

  /**
   * Defines the properties shared by all mapping edges
   * 
   * @author Fedor Smirnov
   *
   */
  protected enum Property {
    /**
     * The enactment mode of the source task on the target resource (e.g., local or
     * serverless)
     */
    EnactmentMode,
    /**
     * The id of the function implementation (e.g., to distinguish a function
     * written in Python from one written in Java)
     */
    ImplementationId
  }

  /**
   * The general types of resources.
   * 
   * @author Fedor Smirnov
   */
  public enum EnactmentMode {
    /**
     * Models an execution on the local resource (the resource running the EE).
     */
    Local,
    /**
     * Models an execution via a serverless function.
     */
    Serverless,
    /**
     * Natively implemented functions, used for demo purposes
     */
    Demo
  }

  /**
   * Returns the implementation ID of the requested mapping.
   * 
   * @param mapping the requested mapping.
   * @return the implementation ID of the requested mapping.
   */
  public static String getImplementationId(final Mapping<Task, Resource> mapping) {
    return (String) getAttribute(mapping, propNameImplId);
  }

  /**
   * Returns the enactment mode of the requested mapping.
   * 
   * @param mapping the requested mapping.
   * @return the enactment mode of the requested mapping.
   */
  public static EnactmentMode getEnactmentMode(final Mapping<Task, Resource> mapping) {
    return EnactmentMode.valueOf((String) getAttribute(mapping, propNameEnactmentMode));
  }

  /**
   * Sets the implementation ID for the given mapping
   * 
   * @param mapping the given mapping
   * @param implId the impl id to set
   */
  protected static void setImplementationId(final Mapping<Task, Resource> mapping,
      final String implId) {
    mapping.setAttribute(propNameImplId, implId);
  }

  /**
   * Sets the enactment mode for the given mapping
   * 
   * @param mapping the given mapping
   * @param enactmentMode the enactment mode to set
   */
  protected static void setEnactmentMode(final Mapping<Task, Resource> mapping,
      final EnactmentMode enactmentMode) {
    mapping.setAttribute(propNameEnactmentMode, enactmentMode.name());
  }

  /**
   * Returns a mapping edge from the given src task to the given dst resource.
   * 
   * @param src the src task
   * @param dst the dst resource
   * @param enactmentMode the enactment mode
   * @param implId the implementation ID
   * @return a mapping edge from the given src task to the given dst resource
   */
  public static Mapping<Task, Resource> createMapping(final Task src, final Resource dst,
      final EnactmentMode enactmentMode, final String implId) {
    return createMapping(src, dst, enactmentMode, implId, getMappingId(src, dst, implId));
  }

  /**
   * Returns a mapping edge from the given src task to the given dst resource.
   * 
   * @param src the src task
   * @param dst the dst resource
   * @param enactmentMode the enactment mode
   * @param implId the implementation ID
   * @param mappingId the id that the created mapping will have
   * @return a mapping edge from the given src task to the given dst resource
   */
  protected static Mapping<Task, Resource> createMapping(final Task src, final Resource dst,
      final EnactmentMode enactmentMode, final String implId, final String mappingId) {
    final Mapping<Task, Resource> result = new Mapping<>(mappingId, src, dst);
    setEnactmentMode(result, enactmentMode);
    setImplementationId(result, implId);
    return result;
  }

  /**
   * Returns the id string of a mapping from the given src task to the given
   * destination.
   * 
   * @param src the src task
   * @param dst the dst resource
   * @return the id string of a mapping from the given src task to the given
   *         destination
   */
  protected static String getMappingId(final Task src, final Resource dst, final String implId) {
    return src.getId() + ConstantsEEModel.KeywordSeparator1 + dst.getId()
        + ConstantsEEModel.KeywordSeparator1 + implId;
  }
}
