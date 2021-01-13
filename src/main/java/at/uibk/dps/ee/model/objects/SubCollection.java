package at.uibk.dps.ee.model.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Interface for the operands which extracts subcollections from a given
 * collection (json array).
 * 
 * @author Fedor Smirnov
 *
 */
public interface SubCollection {

	/**
	 * Returns a json array creates as subcollection of the given array.
	 * 
	 * @param originalCollection the original Json array
	 * @return a sub collection (which can be a single element) from the given array
	 */
	JsonElement getSubCollection(JsonArray originalCollection);
}
