package at.uibk.dps.ee.model.objects;

import java.io.Serializable;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.properties.PropertyServiceData.DataType;

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
    EQUAL(DataType.Number), LESS(DataType.Number), GREATER(DataType.Number), LESS_EQUAL(
        DataType.Number), GREATER_EQUAL(DataType.Number), UNEQUAL(DataType.Number), CONTAINS(
            DataType.String), STARTS_WITH(DataType.String), ENDS_WITH(
                DataType.Number), AND(DataType.Boolean), OR(DataType.Boolean);

    private final DataType dataType;

    Operator(final DataType dataType) {
      this.dataType = dataType;
    }

    public DataType getDataType() {
      return this.dataType;
    }
  }

  private final String firstInputId;
  private final String secondInputId;
  private final Operator operator;
  private final boolean negation;

  /**
   * Standard constructor
   * 
   * @param firstInputId the id of the data node with the first input
   * @param secondInputId the id of the data node with the second input
   * @param operator the condition operator
   * @param negation true iff the result is negated
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
    builder.append(firstInputId).append(' ').append(operator.name()).append(' ')
        .append(secondInputId);
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
  public boolean equals(final Object obj) {
    if (!(obj instanceof Condition)) {
      return false;
    }
    final Condition otherCondi = (Condition) obj;
    return firstInputId.equals(otherCondi.getFirstInputId())
        && secondInputId.equals(otherCondi.getSecondInputId())
        && operator.equals(otherCondi.getOperator()) && negation == otherCondi.isNegation();
  }
}
