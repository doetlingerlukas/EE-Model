package at.uibk.dps.ee.model.graph;

import net.sf.opendse.model.Dependency;
import net.sf.opendse.model.Graph;
import net.sf.opendse.model.Task;

/**
 * The {@link EnactmentGraph} models the data and the control flow of the
 * workflow to enact.
 * 
 * @author Fedor Smirnov
 */
public class EnactmentGraph extends Graph<Task, Dependency> {

	private static final long serialVersionUID = 1L;

}
