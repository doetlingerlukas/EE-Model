package at.uibk.dps.ee.model.objects;

import java.util.ArrayList;

/**
 * Models the class which is annotated onto the function nodes modeling the
 * element index operation. Just an array list with an overwritten toString.
 * 
 * @author Fedor Smirnov
 */
public class SubCollections extends ArrayList<SubCollection> {

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < this.size(); i++) {
			if (i != 0) {
				result.append(',');
			}
			result.append(this.get(i).toString());
		}
		return result.toString();
	}
}
