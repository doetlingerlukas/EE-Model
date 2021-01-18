package at.uibk.dps.ee.model.properties;

import at.uibk.dps.ee.model.constants.ConstantsEEModel;
import at.uibk.dps.ee.model.graph.EnactmentGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.AbstractPropertyService;
import net.sf.opendse.model.properties.TaskPropertyService;

/**
 * Static method container to access the attributes of the enactment
 * {@link Dependency}s.
 * 
 * @author Fedor Smirnov
 */
public final class PropertyServiceDependency extends AbstractPropertyService {

	private PropertyServiceDependency() {
	}

	/**
	 * The properties annotated on dependency edges.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	public enum Property {
		/**
		 * General type of dependency
		 */
		Type,
		/**
		 * The json key used by the Function endpoint of the edge
		 */
		JsonKey
	}

	/**
	 * Different types of dependencies.
	 * 
	 * @author Fedor Smirnov
	 *
	 */
	public enum TypeDependency {
		/**
		 * Data flow
		 */
		Data,
		/**
		 * Control flow following from if compounds
		 */
		ControlIf
	}

	/**
	 * Returns a dependency with a unique ID made from the IDs of its endpoints.
	 * 
	 * @param src  the edge source
	 * @param dest the edge destination
	 * @return a dependency with a unique ID made from the IDs of its endpoints
	 */
	protected static Dependency createDependency(final Task src, final Task dest) {
		final String dependencyId = src.getId() + ConstantsEEModel.KeywordSeparator1 + dest.getId();
		return new Dependency(dependencyId);
	}

	/**
	 * Creates and adds a data dependency to connect the provided nodes.
	 * 
	 * @param src     the source node
	 * @param dest    the destination node
	 * @param jsonKey the key used (by the function node end point) to refer to the
	 *                data content (of the data node end point) by the function node
	 *                end point.
	 * @param graph   the enactment graph
	 * @return
	 */
	public static void addDataDependency(final Task src, final Task dest, final String jsonKey, EnactmentGraph graph) {
		checkDataDependencyEndPoints(src, dest);
		final Dependency dependency = createDependency(src, dest);
		setType(dependency, TypeDependency.Data);
		setJsonKey(dependency, jsonKey);
		graph.addEdge(dependency, src, dest, EdgeType.DIRECTED);
	}

	/**
	 * Checks that we have a data AND a function node as end points, throws an
	 * exception otherwise.
	 * 
	 * @param src  the src node
	 * @param dest the dest node
	 */
	protected static void checkDataDependencyEndPoints(final Task src, final Task dest) {
		final boolean functionPresent = TaskPropertyService.isProcess(src) || TaskPropertyService.isProcess(dest);
		final boolean dataPresent = TaskPropertyService.isCommunication(src)
				|| TaskPropertyService.isCommunication(dest);
		if (!(functionPresent && dataPresent)) {
			throw new IllegalArgumentException("Dependency end points are not a function AND a data node.");
		}
	}

	/**
	 * Returns the json key annotated at the given dependency.
	 * 
	 * @param dep the given dependency
	 * @return the json key annotated at the given dependency
	 */
	public static String getJsonKey(final Dependency dep) {
		final String attrName = Property.JsonKey.name();
		return (String) getAttribute(dep, attrName);
	}

	/**
	 * Sets the json key of the provided dependency
	 * 
	 * @param dep     the provided dependency
	 * @param jsonKey the json key to set
	 */
	public static void setJsonKey(final Dependency dep, final String jsonKey) {
		final String attrName = Property.JsonKey.name();
		dep.setAttribute(attrName, jsonKey);
	}

	/**
	 * Returns the type of the given dependency.
	 * 
	 * @param dep the given dependency
	 */
	public static TypeDependency getType(final Dependency dep) {
		final String attrName = Property.Type.name();
		return TypeDependency.valueOf((String) getAttribute(dep, attrName));
	}

	/**
	 * Sets the type of the given dependency.
	 * 
	 * @param dep  the given dependency
	 * @param type the type to set
	 */
	public static void setType(final Dependency dep, final TypeDependency type) {
		final String attrName = Property.Type.name();
		dep.setAttribute(attrName, type.name());
	}
}
