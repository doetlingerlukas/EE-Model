package at.uibk.dps.ee.model.objects;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.objects.Condition.Operator;

public class ConditionTest {

	@Test
	public void test() {
		String first = "first";
		String second = "second";
		Operator operator = Operator.LESS;
		String toString = ConstantsEEModel.NegationPrefix + first + " " + operator.name() + " " + second
				+ ConstantsEEModel.NegationSuffix;
		String toString2 = first + " " + operator.name() + " " + second;

		Condition tested1 = new Condition(first, second, operator, true);
		Condition tested2 = new Condition(first, second, operator, false);

		assertEquals(first, tested1.getFirstInputId());
		assertEquals(second, tested1.getSecondInputId());
		assertEquals(operator, tested1.getOperator());
		assertTrue(tested1.isNegation());
		assertEquals(toString, tested1.toString());

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

		Condition tested1 = new Condition(first, second, operator, true);
		Condition tested2 = new Condition(first, second, operator, true);
		
		assertEquals(tested1, tested2);
	}

}
