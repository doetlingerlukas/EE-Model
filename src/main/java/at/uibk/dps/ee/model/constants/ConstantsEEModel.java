package at.uibk.dps.ee.model.constants;

/**
 * Container class for the constants used by the classes building and
 * maintaining the enactment model.
 * 
 * @author Fedor Smirnov
 *
 */
public final class ConstantsEEModel {

	public static final String KeywordSeparator1 = "--";
	public static final String KeyWordSeparator2 = "__";
	public static final String NegationPrefix = "!(";
	public static final String NegationSuffix = ")";
	public static final String EarliestArrivalFuncAffix = "|or|";

	public static final String DecisionVariableSuffix = "--decisionVariable";
	public static final String DecisionVariableJsonKey = "decisionVariable";
	public static final String EarliestArrivalJsonKey = "earliestArrival";

	// Function node name components
	public static final String FuncNameUtilityDistribution = "Distribution";
	public static final String JsonKeyConstantIterator = "ConstantIterator";
	public static final String FuncNameUtilityAggregation = "Aggregation";
	public static final String JsonKeyAggregation = "aggregateEntry";
	public static final String JsonKeyDistribution = "distributedEntry";

	// Element index properties
	public static final String EIdxSeparatorInternal = ":";
	public static final String EIdxSeparatorExternal = ",";
	public static final String BlockSeparator = ",";

	/**
	 * No constructor
	 */
	private ConstantsEEModel() {
	}
}
