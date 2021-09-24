package at.uibk.dps.ee.model.persistance;

import at.uibk.dps.ee.model.graph.EnactmentSpecification;
import net.sf.opendse.io.SpecificationReader;
import net.sf.opendse.io.SpecificationWriter;
import net.sf.opendse.model.Specification;

/**
 * The {@link EnactmentSpecIo} writes the spec by first transforming it to
 * an odse specification.
 * 
 * @author Fedor Smirnov
 *
 */
public class EnactmentSpecIo {

  private EnactmentSpecIo() {}

  public static void writeESpecToFilePath(EnactmentSpecification eSpec, String filePath) {
    Specification spec = EnactmentSpecTransformer.toOdse(eSpec);
    SpecificationWriter writer = new SpecificationWriter();
    writer.write(spec, filePath);
  }
  
  public static EnactmentSpecification readSpecFromFilePath(String filePath) {
    SpecificationReader reader = new SpecificationReader();
    Specification spec = reader.read(filePath);
    return EnactmentSpecTransformer.toApollo(spec);
  }
}
