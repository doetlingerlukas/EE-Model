package at.uibk.dps.ee.model.objects;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class SubCollectionStartEndStrideTest {

	@Test
	public void test() {
		JsonArray input = new JsonArray();
		JsonElement el0 = mock(JsonElement.class);
		JsonElement el1 = mock(JsonElement.class);
		JsonElement el2 = mock(JsonElement.class);
		JsonElement el3 = mock(JsonElement.class);
		JsonElement el4 = mock(JsonElement.class);

		input.add(el0);
		input.add(el1);
		input.add(el2);
		input.add(el3);
		input.add(el4);
		
		SubCollectionStartEndStride tested1 = new SubCollectionStartEndStride(1, 2, -1);
		JsonElement result1 = tested1.getSubCollection(input);
		assertEquals("1:2:", tested1.toString());
		assertTrue(result1.isJsonArray());
		JsonArray arrayResult1 = result1.getAsJsonArray();
		assertEquals(2, arrayResult1.size());
		assertEquals(el1, arrayResult1.get(0));
		assertEquals(el2, arrayResult1.get(1));
		
		SubCollectionStartEndStride tested2 = new SubCollectionStartEndStride(-1, 1, -1);
		JsonElement result2 = tested2.getSubCollection(input);
		assertEquals(":1:", tested2.toString());
		assertTrue(result2.isJsonArray());
		JsonArray arrayResult2 = result2.getAsJsonArray();
		assertEquals(2, arrayResult2.size());
		assertEquals(el0, arrayResult2.get(0));
		assertEquals(el1, arrayResult2.get(1));
		
		SubCollectionStartEndStride tested3 = new SubCollectionStartEndStride(1, -1, 2);
		JsonElement result3 = tested3.getSubCollection(input);
		assertEquals("1::2", tested3.toString());
		assertTrue(result3.isJsonArray());
		JsonArray arrayResult3 = result3.getAsJsonArray();
		assertEquals(2, arrayResult3.size());
		assertEquals(el1, arrayResult3.get(0));
		assertEquals(el3, arrayResult3.get(1));
		
		SubCollectionStartEndStride tested4 = new SubCollectionStartEndStride(1, -1, -1);
		JsonElement result4 = tested4.getSubCollection(input);
		assertEquals("1::", tested4.toString());
		assertTrue(result4.isJsonArray());
		JsonArray arrayResult4 = result4.getAsJsonArray();
		assertEquals(4, arrayResult4.size());
		assertEquals(el1, arrayResult4.get(0));
		assertEquals(el2, arrayResult4.get(1));
		assertEquals(el3, arrayResult4.get(2));
		assertEquals(el4, arrayResult4.get(3));
	}
}
