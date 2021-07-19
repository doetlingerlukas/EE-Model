package at.uibk.dps.ee.model.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ConstantsEEModelTest {

  @Test
  public void testGetCollElementKey() {
    String collName = "collection";
    int idx = 3;
    String expected = "collection[3]";
    assertEquals(expected, ConstantsEEModel.getCollectionElementKey(collName, idx));
  }

  @Test
  public void testGetCollectionName() {
    String input = "collection[3]";
    String expected = "collection";
    assertEquals(expected, ConstantsEEModel.getCollectionName(input));
  }

  @Test
  public void testGetArrayIndex() {
    String input = "collection[3]";
    int expected = 3;
    assertEquals(expected, ConstantsEEModel.getArrayIndex(input));
  }

}
