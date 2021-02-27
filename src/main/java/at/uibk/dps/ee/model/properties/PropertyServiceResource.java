package at.uibk.dps.ee.model.properties;

import java.util.HashSet;
import java.util.Set;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;

/**
 * Static container with methods offering convenient access to the attributes of
 * resource nodes.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceResource extends AbstractPropertyService {

  private static final String propNameType = Property.Type.name();
  private static final String propNameState = Property.State.name();
  private static final String propNameUsedBy = Property.UsedBy.name();

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
    Type,
    /**
     * The state of the resource
     */
    State,
    /**
     * The set of tasks currently using the resource
     */
    UsedBy
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
   * Models the different states of the resources.
   * 
   * @author Fedor Smirnov
   *
   */
  public enum ResourceState {
    /**
     * The resource is currently being used
     */
    Used,
    /**
     * The resource is idle
     */
    Idle
  }

  /**
   * Removes a task from the list of users of the given resource.
   * 
   * @param task the given task
   * @param res the given resource
   */
  public static void removeUsingTask(final Task task, final Resource res) {
    final Set<String> users = getUsingTaskIds(res);
    if (!users.contains(task.getId())) {
      throw new IllegalStateException("The task " + task.getId()
          + " cannote be removed from resource " + res.getId() + " since it is not using it.");
    }
    users.remove(task.getId());
    res.setAttribute(propNameUsedBy, users);
  }

  /**
   * Adds a task to the list of users of the given resource
   * 
   * @param task the task to add
   * @param res the resource
   */
  public static void addUsingTask(final Task task, final Resource res) {
    final Set<String> users = getUsingTaskIds(res);
    users.add(task.getId());
    res.setAttribute(propNameUsedBy, users);
  }

  /**
   * Returns the IDs of the tasks currently using the resource.
   * 
   * @param res the resource
   * @return the IDs of the tasks currently using the resource
   */
  @SuppressWarnings("unchecked")
  public static Set<String> getUsingTaskIds(final Resource res) {
    if (isAttributeSet(res, propNameUsedBy)) {
      return (Set<String>) res.getAttribute(propNameUsedBy);
    } else {
      return new HashSet<>();
    }
  }

  /**
   * Returns the current state of the given resource
   * 
   * @param res the given resource
   * @return the current state of the given resource
   */
  public static ResourceState getState(final Resource res) {
    if (!isAttributeSet(res, propNameState)) {
      return ResourceState.Idle;
    }
    return ResourceState.valueOf((String) res.getAttribute(propNameState));
  }

  /**
   * Sets the state of the given resource
   * 
   * @param res the given resource
   * @param state the state to set
   */
  public static void setState(final Resource res, final ResourceState state) {
    res.setAttribute(propNameState, state.name());
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
