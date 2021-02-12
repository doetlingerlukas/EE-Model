package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/**
 * Static method container providing access to the attributes of the mapping
 * edges.
 * 
 * @author Fedor Smirnov
 *
 */
public final class PropertyServiceMapping {

  /**
   * No constructor.
   */
  private PropertyServiceMapping() {}

  /**
   * Returns a mapping edge from the given src task to the given dst resource.
   * 
   * @param src the src task
   * @param dst the dst resource
   * @return a mapping edge from the given src task to the given dst resource
   */
  public static Mapping<Task, Resource> createMapping(final Task src, final Resource dst) {
    return new Mapping<Task, Resource>(getMappingId(src, dst), src, dst);
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
  protected static String getMappingId(final Task src, final Resource dst) {
    return src.getId() + ConstantsEEModel.KeywordSeparator1 + dst.getId();
  }
}
