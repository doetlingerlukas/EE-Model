package at.uibk.dps.ee.model.constants;

/**
 * Container class for the constants used by the classes building and
 * maintaining the enactment model.
 * 
 * @author Fedor Smirnov
 *
 */
public final class ConstantsEEModel {

	public static final String DependencyAffix = "|--|";
	public static final String NegationPrefix = "!(";
	public static final String NegationSuffix = ")";
	public static final String EarliestArrivalFuncAffix = "|or|";
	
	public static final String DecisionVariableSuffix = "--decisionVariable";
	public static final String DecisionVariableJsonKey = "decisionVariable";
	public static final String EarliestArrivalJsonKey = "earliestArrival";
	
	public static final String ElementIndexName = "element-index";
	public static final String ElementIndexValueSeparatorInternal = ":";
	public static final String ElementIndexValueSeparatorExternal = ",";
	
	/**
	 * No constructor
	 */
	private ConstantsEEModel() {
	}
}
