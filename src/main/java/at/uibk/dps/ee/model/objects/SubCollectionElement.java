package at.uibk.dps.ee.model.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * The {@link SubCollectionElement} reads the entry from a single index of the
 * original collection.
 * 
 * @author Fedor Smirnov
 *
 */
public class SubCollectionElement implements SubCollection {

	protected final int index;

	/**
	 * Constructor provided with the index of the element which is to be extracted.
	 * 
	 * @param index the index
	 */
	public SubCollectionElement(final int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return String.valueOf(index);
	}

	@Override
	public JsonElement getSubCollection(final JsonArray originalCollection) {
		return originalCollection.get(index);
	}
}
