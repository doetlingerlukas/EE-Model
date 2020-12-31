package at.uibk.dps.ee.model.objects;

import java.io.Serializable;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;

/**
 * Models a boolean function which generates a boolean out of 2 inputs.
 * 
 * @author Fedor Smirnov
 *
 */
public final class Condition implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The operator used to process the 2 inputs.
	 * 
	 * @author Fedor Smirnov
	 */
	public enum Operator {
		EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL, UNEQUAL, STARTS_WITH, ENDS_WITH
	}

	protected final String firstInputId;
	protected final String secondInputId;
	protected final Operator operator;
	protected final boolean negation;

	/**
	 * Standard constructor
	 * 
	 * @param firstInputId  the id of the data node with the first input
	 * @param secondInputId the id of the data node with the second input
	 * @param operator      the condition operator
	 * @param negation      true iff the result is negated
	 */
	public Condition(final String firstInputId, final String secondInputId, final Operator operator,
			final boolean negation) {
		this.firstInputId = firstInputId;
		this.secondInputId = secondInputId;
		this.operator = operator;
		this.negation = negation;
	}

	public String getFirstInputId() {
		return firstInputId;
	}

	public String getSecondInputId() {
		return secondInputId;
	}

	public Operator getOperator() {
		return operator;
	}

	public boolean isNegation() {
		return negation;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(firstInputId);
		builder.append(" ");
		builder.append(operator.name());
		builder.append(" ");
		builder.append(secondInputId);
		if (negation) {
			builder.insert(0, ConstantsEEModel.NegationPrefix);
			builder.append(ConstantsEEModel.NegationSuffix);
		}
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstInputId == null) ? 0 : firstInputId.hashCode());
		result = prime * result + (negation ? 1231 : 1237);
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((secondInputId == null) ? 0 : secondInputId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Condition other = (Condition) obj;
		if (firstInputId == null) {
			if (other.firstInputId != null)
				return false;
		} else if (!firstInputId.equals(other.firstInputId))
			return false;
		if (negation != other.negation)
			return false;
		if (operator != other.operator)
			return false;
		if (secondInputId == null) {
			if (other.secondInputId != null)
				return false;
		} else if (!secondInputId.equals(other.secondInputId))
			return false;
		return true;
	}
}
