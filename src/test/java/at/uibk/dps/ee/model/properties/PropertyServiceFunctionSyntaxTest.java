package at.uibk.dps.ee.model.properties;

import static org.junit.Assert.*;

import org.junit.Test;

import at.uibk.dps.ee.model.properties.PropertyServiceFunction.FunctionType;
import at.uibk.dps.ee.model.properties.PropertyServiceFunctionSyntax.SyntaxType;
import net.sf.opendse.model.Task;

public class PropertyServiceFunctionSyntaxTest {

	@Test(expected = IllegalArgumentException.class)
	public void testCheckTask() {
		Task task = new Task("task");
		PropertyServiceFunctionSyntax.checkTask(task);
	}

	@Test
	public void checkSetGetSyntaxType() {
		Task task = new Task("task");
		PropertyServiceFunction.setType(FunctionType.Syntax, task);
		PropertyServiceFunctionSyntax.setSyntaxType(task, SyntaxType.EarliestInput);
		assertEquals(SyntaxType.EarliestInput, PropertyServiceFunctionSyntax.getSyntaxType(task));
	}
}
