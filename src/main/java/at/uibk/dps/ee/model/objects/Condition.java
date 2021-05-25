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
    EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL, UNEQUAL, CONTAINS, STARTS_WITH, ENDS_WITH;
  }

  /**
   * The operator used to combine the result of the condition with the conditions
   * defined later.
   * 
   * @author Fedor Smirnov
   */
  public enum CombinedWith {
    And, Or
  }

  private final String firstInputId;
  private final String secondInputId;
  private final Operator operator;
  private final DataType type;
  private final CombinedWith combinedWith;
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
      final boolean negation, final DataType type, final CombinedWith combinedWith) {
    this.firstInputId = firstInputId;
    this.secondInputId = secondInputId;
    this.operator = operator;
    this.negation = negation;
    this.type = type;
    this.combinedWith = combinedWith;
  }

  /**
   * This constructor is needed to deserialize conditions.
   * 
   * @param deserialized the deserialized string
   */
  public Condition(final String deserialized) {
    final String[] subStrings = deserialized.split("\\s+");
    // check for negation
    int idx = 0;
    if (subStrings[idx].equals(ConstantsEEModel.NegationPrefix)) {
      this.negation = true;
      idx++;
    } else {
      this.negation = false;
    }
    this.firstInputId = subStrings[idx++];
    this.operator = Operator.valueOf(subStrings[idx++]);
    this.secondInputId = subStrings[idx++];
    this.combinedWith = CombinedWith.valueOf(subStrings[idx++]);
    this.type = DataType.valueOf(subStrings[idx]);
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

  public DataType getType() {
    return type;
  }

  public CombinedWith getCombinedWith() {
    return combinedWith;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(firstInputId).append(' ').append(operator.name()).append(' ')
        .append(secondInputId).append(' ').append(combinedWith.name()).append(' ')
        .append(type.name());
    if (negation) {
      builder.insert(0, ConstantsEEModel.NegationPrefix + ' ');
      builder.append(' ' + ConstantsEEModel.NegationSuffix);
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
        && operator.equals(otherCondi.getOperator()) && negation == otherCondi.isNegation()
        && type.equals(otherCondi.getType()) && combinedWith.equals(otherCondi.getCombinedWith());
  }
}
