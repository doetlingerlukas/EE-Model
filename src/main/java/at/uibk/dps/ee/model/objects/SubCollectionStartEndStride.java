package at.uibk.dps.ee.model.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;

/**
 * The {@link SubCollectionStartEndStride} reads a sub collection from the
 * original collection.
 * 
 * @author Fedor Smirnov
 *
 */
public class SubCollectionStartEndStride implements SubCollection {

	protected final int start;
	protected final int end;
	protected final int stride;

	/**
	 * Sets the start, the end and the stride. Called with -1 to assign the default
	 * value.
	 * 
	 * @param start  start (defaults to 0)
	 * @param end    end (defaults to the size of the collection)
	 * @param stride (defaults to 1)
	 */
	public SubCollectionStartEndStride(int start, int end, int stride) {
		this.start = start;
		this.end = end;
		this.stride = stride;
	}

	@Override
	public JsonElement getSubCollection(JsonArray originalCollection) {
		JsonArray result = new JsonArray();
		int actualStart = start == -1 ? 0 : start;
		int actualEnd = end == -1 ? originalCollection.size() - 1 : end;
		int actualStride = stride == -1 ? 1 : stride;

		for (int i = actualStart; i <= actualEnd; i += actualStride) {
			JsonElement entry = originalCollection.get(i);
			result.add(entry);
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		String startString = start == -1 ? "" : String.valueOf(start);
		String endString = end == -1 ? "" : String.valueOf(end);
		String strideString = stride == -1 ? "" : String.valueOf(stride);
		buffer.append(startString).append(ConstantsEEModel.ElementIndexValueSeparatorInternal).append(endString)
				.append(ConstantsEEModel.ElementIndexValueSeparatorInternal).append(strideString);
		return buffer.toString();
	}
}
