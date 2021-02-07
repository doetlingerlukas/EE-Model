package at.uibk.dps.ee.model.graph;

import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Specification;
import net.sf.opendse.model.Task;

/**
 * The {@link EnactmentSpecification} models the whole enactment problem.
 * 
 * @author Fedor Smirnov
 */
public class EnactmentSpecification extends Specification {

	public EnactmentSpecification(EnactmentGraph enactmentGraph, ResourceGraph resourceGraph,
			Mappings<Task, Resource> mappings) {
		super(enactmentGraph, resourceGraph, mappings, null);
	}

	/**
	 * Returns the enactment graph.
	 * 
	 * @return the enactment graph
	 */
	public EnactmentGraph getEnactmentGraph() {
		return (EnactmentGraph) getApplication();
	}

	/**
	 * Returns the resource graph
	 * 
	 * @return the resource graph
	 */
	public ResourceGraph getResourceGraph() {
		return (ResourceGraph) getArchitecture();
	}
}
