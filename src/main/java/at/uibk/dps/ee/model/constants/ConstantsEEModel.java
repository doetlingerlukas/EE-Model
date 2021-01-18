package at.uibk.dps.ee.model.constants;

/**
 * Container class for the constants used by the classes building and
 * maintaining the enactment model.
 * 
 * @author Fedor Smirnov
 *
 */
public final class ConstantsEEModel {

	public static final String DependencyAffix = "--";
	public static final String KeyWordSeparator2 = "__";
	public static final String NegationPrefix = "!(";
	public static final String NegationSuffix = ")";
	public static final String EarliestArrivalFuncAffix = "|or|";

	public static final String DecisionVariableSuffix = "--decisionVariable";
	public static final String DecisionVariableJsonKey = "decisionVariable";
	public static final String EarliestArrivalJsonKey = "earliestArrival";

	// Element index properties
	public static final String EIdxSeparatorInternal = ":";
	public static final String EIdxSeparatorExternal = ",";
	public static final String EIdxEdgeIdxSeparator = "!";
	public static final String EIdxDataKeyWord = "data";

	/**
	 * Enum for the parameters of an EIdx operation which can be determined by a
	 * src.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	public enum EIdxParameters {
		Index, Start, End, Stride;
	}

	/**
	 * No constructor
	 */
	private ConstantsEEModel() {
	}
}
