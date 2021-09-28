package at.uibk.dps.ee.model.persistance;

import at.uibk.dps.ee.model.graph.EnactmentSpecification;
import net.sf.opendse.io.SpecificationReader;
import net.sf.opendse.io.SpecificationWriter;
import net.sf.opendse.model.Specification;

/**
 * The {@link EnactmentSpecIo} writes the spec by first transforming it to an
 * odse specification.
 * 
 * @author Fedor Smirnov
 *
 */
public class EnactmentSpecIo {

  /**
   * No constructor
   */
  private EnactmentSpecIo() {}

  /**
   * Writes the given {@link EnactmentSpecification} to a file at the given path.
   * 
   * @param eSpec the {@link EnactmentSpecification}
   * @param filePath the file path to write the spec to
   */
  public static void writeESpecToFilePath(final EnactmentSpecification eSpec,
      final String filePath) {
    final Specification spec = EnactmentSpecTransformer.toOdse(eSpec);
    final SpecificationWriter writer = new SpecificationWriter();
    writer.write(spec, filePath);
  }

  /**
   * Reads an {@link EnactmentSpecification} from the given file.
   * 
   * @param filePath the file path to read from
   * @return the {@link EnactmentSpecification}
   */
  public static EnactmentSpecification readSpecFromFilePath(final String filePath) {
    final SpecificationReader reader = new SpecificationReader();
    final Specification spec = reader.read(filePath);
    return EnactmentSpecTransformer.toApollo(spec);
  }
}
