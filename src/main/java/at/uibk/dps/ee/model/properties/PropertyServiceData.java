package at.uibk.dps.ee.model.properties;

import com.google.gson.JsonElement;

import net.sf.opendse.model.Communication;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container for the properties of the {@link Communication} nodes
 * modeling the data packages consumed and produced by functions as inputs and
 * outputs, respectively.
 * 
 * @author Fedor Smirnov
 *
 */
public final class PropertyServiceData extends AbstractPropertyService {

  private PropertyServiceData() {}

  /**
   * Properties annotated for data nodes.
   * 
   * @author Fedor Smirnov
   *
   */
  public enum Property {
    /**
     * Was the data already produced?
     */
    DataAvailable,
    /**
     * The data
     */
    Content,
    /**
     * Is workflow input?
     */
    Root,
    /**
     * Is workflow output?
     */
    Leaf,
    /**
     * The key used in the input/output Json Object (only for leaf/root nodes)
     */
    JsonKey,
    /**
     * The data type
     */
    DataType,
    /**
     * The type of the data node
     */
    NodeType,
  }

  public enum DataType {
    Number, String, Object, Collection, Boolean, Array
  }

  /**
   * The type of data node.
   * 
   * @author Fedor Smirnov
   *
   */
  public enum NodeType {
    /**
     * The default type for data nodes: model data which is produced throughout the
     * enactment or provided as wf input.
     */
    Default,
    /**
     * Data nodes modeling constant data.
     */
    Constant,
    /**
     * Data nodes whose content is used for control flow decisions.
     */
    Decision,
    /**
     * Data nodes used to model sequentiality without data exchange
     */
    Sequentiality
  }

  /**
   * Returns the node type of the provided task
   * 
   * @param task the provided task
   * @return
   */
  public static NodeType getNodeType(final Task task) {
    checkTask(task);
    final String attrName = Property.NodeType.name();
    if (isAttributeSet(task, attrName)) {
      return NodeType.valueOf((String) getAttribute(task, attrName));
    } else {
      return NodeType.Default;
    }
  }

  /**
   * Sets the provided node type for the provided task.
   * 
   * @param task the provided task.
   * @param nodeType the node type to set
   */
  public static void setNodeType(final Task task, final NodeType nodeType) {
    checkTask(task);
    final String attrName = Property.NodeType.name();
    task.setAttribute(attrName, nodeType.name());
  }

  /**
   * Creates a constant data node with the given id, data type, and content.
   * 
   * @param nodeId the id of the created node
   * @param dataType the data type of the created node
   * @param content the content of the created node
   * @return a constant data node with the given id, data type, and content
   */
  public static Task createConstantNode(final String nodeId, final DataType dataType,
      final JsonElement content) {
    final Task result = new Communication(nodeId);
    setDataType(result, dataType);
    setNodeType(result, NodeType.Constant);
    final String attrNameContent = Property.Content.name();
    result.setAttribute(attrNameContent, content);
    final String dataAvalAttrName = Property.DataAvailable.name();
    result.setAttribute(dataAvalAttrName, true);
    return result;
  }

  /**
   * Creates a sequentiality node with the requested ID.
   * 
   * @param nodeId the requested ID
   * @return Creates a sequentiality node with the requested ID
   */
  public static Task createSequentialityNode(final String nodeId) {
    final Task result = new Communication(nodeId);
    setDataType(result, DataType.Boolean);
    setNodeType(result, NodeType.Sequentiality);
    return result;
  }

  /**
   * Returns the data type of the given task.
   * 
   * @param task the given task
   * @return the data type of the given task
   */
  public static DataType getDataType(final Task task) {
    checkTask(task);
    final String attrName = Property.DataType.name();
    return DataType.valueOf((String) getAttribute(task, attrName));
  }

  /**
   * Annotates the given node with the given data type
   * 
   * @param task the given task node
   * @param type the type to annotate
   */
  public static void setDataType(final Task task, final DataType type) {
    checkTask(task);
    final String attrName = Property.DataType.name();
    task.setAttribute(attrName, type.name());
  }

  /**
   * Sets the json key used to store/retrieve the data in/from the Json
   * output/input of the workflow.
   * 
   * @param task the task to annotate
   * @param the key to use in the Json object
   */
  public static void setJsonKey(final Task task, final String jsonKey) {
    checkTask(task);
    if (!isRoot(task) && !isLeaf(task)) {
      throw new IllegalArgumentException("Only leaf/root nodes can be annotated with a Json key.");
    }
    final String attrName = Property.JsonKey.name();
    task.setAttribute(attrName, jsonKey);
  }

  /**
   * Returns the json key used to store/retrieve the data in/from the Json
   * output/input of the workflow.
   * 
   * @param task the leaf/root task
   * @return the json key used to store/retrieve the data in/from the Json
   *         output/input of the workflow
   */
  public static String getJsonKey(final Task task) {
    checkTask(task);
    if (!isRoot(task) && !isLeaf(task)) {
      throw new IllegalArgumentException("Only leaf/root nodes can be annotated with a Json key.");
    }
    final String attrName = Property.JsonKey.name();
    return (String) getAttribute(task, attrName);
  }

  /**
   * Annotates the given task as leaf.
   * 
   * @param task the given task
   */
  public static void makeLeaf(final Task task) {
    checkTask(task);
    final String attrName = Property.Leaf.name();
    task.setAttribute(attrName, true);
  }

  /**
   * Checks whether the given task is a leaf (wf output).
   * 
   * @param task the task to check.
   * @return <code>true</code> if the task is a leaf.
   */
  public static boolean isLeaf(final Task task) {
    checkTask(task);
    final String attrName = Property.Leaf.name();
    if (!isAttributeSet(task, attrName)) {
      return false;
    }
    return (boolean) getAttribute(task, attrName);
  }

  /**
   * Annotates the given task as a root.
   * 
   * @param task the given task
   */
  public static void makeRoot(final Task task) {
    checkTask(task);
    final String attrName = Property.Root.name();
    task.setAttribute(attrName, true);
  }

  /**
   * Checks whether the given task is a root (wf input).
   * 
   * @param task the given task
   * @return <code>true</code> iff the given task is a root.
   */
  public static boolean isRoot(final Task task) {
    checkTask(task);
    final String attrName = Property.Root.name();
    if (!isAttributeSet(task, attrName)) {
      return false;
    }
    return (boolean) getAttribute(task, attrName);
  }

  /**
   * Annotates the given content to the given data node.
   * 
   * @param task the given data node
   * @param content the content to annotate
   */
  public static void setContent(final Task task, final JsonElement content) {
    checkTask(task);
    if (getNodeType(task).equals(NodeType.Constant)) {
      throw new IllegalArgumentException("The content of a constant data node must not be set.");
    }
    final String attrNameContent = Property.Content.name();
    task.setAttribute(attrNameContent, content);
    final String attrNameAval = Property.DataAvailable.name();
    task.setAttribute(attrNameAval, true);
  }

  /**
   * Returns the data content of the given data node. Throws an exception if no
   * data annotated.
   * 
   * @param task the given task
   * @return the data content of the given data node
   */
  public static JsonElement getContent(final Task task) {
    checkTask(task);
    final String attrName = Property.Content.name();
    checkAttribute(task, attrName);
    return (JsonElement) getAttribute(task, attrName);
  }

  /**
   * Returns <code>true</code> if the data has been annotated as available.
   * 
   * @param task the data task to check
   * @return <code>true</code> if the data has been annotated as available
   */
  public static boolean isDataAvailable(final Task task) {
    checkTask(task);
    final String attrName = Property.DataAvailable.name();
    if (!isAttributeSet(task, attrName)) {
      return false;
    }
    return (boolean) getAttribute(task, attrName);
  }

  /**
   * Empties the data node by resetting its content.
   * 
   * @param task the data node
   */
  public static void resetContent(final Task task) {
    checkTask(task);
    if (getNodeType(task).equals(NodeType.Constant)) {
      throw new IllegalArgumentException("The content of a constant data node must not be set.");
    }
    final String attrName = Property.DataAvailable.name();
    task.setAttribute(attrName, false);
    final String attrNameContent = Property.Content.name();
    task.setAttribute(attrNameContent, null);
  }

  /**
   * Checks whether a given task is processable by this service. Throws an
   * exception in case it is not.
   * 
   * @param task the task to check
   */
  protected static void checkTask(final Task task) {
    if (!TaskPropertyService.isCommunication(task)) {
      throw new IllegalArgumentException("Task " + task.getId() + " does not model data.");
    }
  }
}
