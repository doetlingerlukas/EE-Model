package at.uibk.dps.ee.model.graph;

import net.sf.opendse.model.Architecture;
import net.sf.opendse.model.Link;
import net.sf.opendse.model.Resource;

/**
 * The {@link ResourceGraph} models the resources available for the execution,
 * as well as their connections to each other.
 * 
 * @author Fedor Smirnov
 *
 */
public class ResourceGraph extends Architecture<Resource, Link> {

  private static final long serialVersionUID = 1L;
}
