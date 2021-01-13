package at.uibk.dps.ee.model.objects;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubCollectionsTest {

	@Test
	public void test() {
		SubCollections tested = new SubCollections();
		SubCollection mock1 = mock(SubCollection.class);
		SubCollection mock2 = mock(SubCollection.class);
		SubCollection mock3 = mock(SubCollection.class);
		String string1 = "bla";
		String string2 = "abc";
		String string3 = "fun";
		String expected = string1 + ',' + string2 + ',' + string3;

		when(mock1.toString()).thenReturn(string1);
		when(mock2.toString()).thenReturn(string2);
		when(mock3.toString()).thenReturn(string3);

		tested.add(mock1);
		tested.add(mock2);
		tested.add(mock3);

		assertEquals(expected, tested.toString());
	}
}
