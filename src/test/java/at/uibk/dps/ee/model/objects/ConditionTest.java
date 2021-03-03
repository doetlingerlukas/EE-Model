package at.uibk.dps.ee.model.objects;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.objects.Condition.CombinedWith;
import at.uibk.dps.ee.model.objects.Condition.Operator;
import at.uibk.dps.ee.model.properties.PropertyServiceData.DataType;

public class ConditionTest {

  @Test
  public void test() {
    String first = "first";
    String second = "second";
    Operator operator = Operator.LESS;
    String toString = ConstantsEEModel.NegationPrefix + first + " " + operator.name() + " " + second
        + ConstantsEEModel.NegationSuffix;
    String toString2 = first + " " + operator.name() + " " + second;

    Condition tested1 =
        new Condition(first, second, operator, true, DataType.Number, CombinedWith.And);
    Condition tested2 =
        new Condition(first, second, operator, false, DataType.Number, CombinedWith.And);

    assertEquals(first, tested1.getFirstInputId());
    assertEquals(second, tested1.getSecondInputId());
    assertEquals(operator, tested1.getOperator());
    assertTrue(tested1.isNegation());
    assertEquals(toString, tested1.toString());
    assertEquals(DataType.Number, tested1.getType());
    assertEquals(CombinedWith.And, tested1.getCombinedWith());

    assertEquals(first, tested2.getFirstInputId());
    assertEquals(second, tested2.getSecondInputId());
    assertEquals(operator, tested2.getOperator());
    assertFalse(tested2.isNegation());
    assertEquals(toString2, tested2.toString());
  }

  @Test
  public void testEquals() {
    String first = "first";
    String second = "second";
    Operator operator = Operator.LESS;

    Condition tested1 =
        new Condition(first, second, operator, true, DataType.Number, CombinedWith.And);
    Condition tested2 =
        new Condition(first, second, operator, true, DataType.Number, CombinedWith.And);

    assertEquals(tested1, tested2);
  }
}
