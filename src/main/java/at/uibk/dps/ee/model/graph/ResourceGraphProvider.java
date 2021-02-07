package at.uibk.dps.ee.model.graph;

/**
 * Interface for the classes offering access to the resource graph.
 * 
 * @author Fedor Smirnov
 *
 */
public interface ResourceGraphProvider {

	/**
	 * Returns the resource graph
	 * 
	 * @return the resource graph
	 */
	ResourceGraph getResourceGraph();
	
}
