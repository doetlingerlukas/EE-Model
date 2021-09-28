package at.uibk.dps.ee.model.constants;

/**
 * Container class for the constants used by the classes building and
 * maintaining the enactment model.
 * 
 * @author Fedor Smirnov
 *
 */
public final class ConstantsEEModel {

  // General constants
  public static final String KeywordSeparator1 = "--";
  public static final String KeyWordSeparator2 = "__";
  public static final String KeyWordEdgeUniqueness = "'";

  // Spec constraints
  public static final String SpecIdDefault = "specification";

  // Resource graph constants
  public static final String idLocalResource = "Enactment Engine (Local Machine)";
  public static final int defaultFaaSTimeoutSeconds = 30;

  // Mapping edge constants
  public static final String implIdLocalNative = "native";

  public static final String ConstantNodeAffix = "Constant";


  // Enactment graph constants
  public static final String NegationPrefix = "!(";
  public static final String NegationSuffix = ")";
  public static final String CollectionIndexPrefix = "[";
  public static final String CollectionIndexSuffix = "]";
  public static final String EarliestArrivalFuncAffix = "|or|";

  public static final String DecisionVariableSuffix = "--decisionVariable";
  public static final String DecisionVariableJsonKey = "decisionVariable";
  public static final String EarliestArrivalJsonKey = "earliestArrival";
  public static final String JsonKeyThen = "Then";
  public static final String JsonKeyElse = "Else";
  // used for the edges from decision variable
  public static final String JsonKeyIfDecision = "IfDecision";
  // used for the edges to the nodes containing the compound result
  public static final String JsonKeyIfResult = "IfResult";

  public static final String JsonKeySequentiality = "SeqKey";

  public static final String JsonKeyWhileStart = "WhileKey";
  public static final String JsonKeyWhileCounter = "WhileCounter";
  public static final String JsonKeyWhileDecision = "WhileDecision";

  // Function node name components
  public static final String FuncNameUtilityDistribution = "Distribution";
  public static final String JsonKeyConstantIterator = "ConstantIterator";
  public static final String FuncNameUtilityAggregation = "Aggregation";
  public static final String JsonKeyAggregation = "aggregateEntry";
  public static final String JsonKeyDistribution = "distributedEntry";

  public static final String WhileConditionSuffix = "stopCondition";
  public static final String WhileConditionBoolSuffix = "stopDecision";
  public static final String WhileLoopCounterSuffix = "counter";
  public static final String WhileEndSuffix = "whileEnd";
  public static final String whileReplicaSuffix = "+";

  // Element index properties
  public static final String EIdxSeparatorInternal = ":";
  public static final String EIdxSeparatorExternal = ",";
  public static final String BlockSeparator = ",";

  /**
   * No constructor
   */
  private ConstantsEEModel() {}

  /**
   * Returns the key used to access the element of a collection on an index.
   * 
   * @param collName the name of the collection
   * @param idx the index
   * @return the element of a collection on an index
   */
  public static String getCollectionElementKey(final String collName, final int idx) {
    return collName + ConstantsEEModel.CollectionIndexPrefix + idx
        + ConstantsEEModel.CollectionIndexSuffix;
  }

  /**
   * Returns the collection name
   * 
   * @param elementKey the string used as the json key of a single element
   * @return the collection name
   */
  public static String getCollectionName(final String elementKey) {
    final String result = elementKey.split("\\" + CollectionIndexPrefix)[0];
    checkSplitResult(result, elementKey);
    return result;
  }

  /**
   * Returns the array index (position of the element within the array)
   * 
   * @param elementKey the string used as the json key of a single element
   * @return the index of the element within the array
   */
  public static int getArrayIndex(final String elementKey) {
    String intString = elementKey.split("\\" + CollectionIndexPrefix)[1];
    checkSplitResult(intString, elementKey);
    String splitIntString = intString.split("\\" + CollectionIndexSuffix)[0];
    checkSplitResult(splitIntString, intString);
    return Integer.parseInt(splitIntString);
  }

  /**
   * Checks whether split result is empty
   * 
   * @param splitResult the split result
   * @param originalString the original string
   */
  static void checkSplitResult(String splitResult, String originalString) {
    if (splitResult.equals("")) {
      throw new IllegalStateException(
          "Could not get the collection name from original string " + originalString);
    }
  }
}
