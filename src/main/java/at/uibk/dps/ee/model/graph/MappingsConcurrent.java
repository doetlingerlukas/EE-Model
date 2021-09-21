package at.uibk.dps.ee.model.graph;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.opendse.model.Mapping;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;

/**
 * The {@link MappingsConcurrent} object offers thread-safe access to the
 * mappings during the orchestration.
 * 
 * @author Fedor Smirnov
 *
 */
public class MappingsConcurrent<T extends Task, R extends Resource>
    implements Iterable<Mapping<T, R>> {

  protected final ConcurrentHashMap<String, Mapping<T, R>> mappings = new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, Mapping<T, R>>> taskMappings =
      new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, Mapping<T, R>>> resourceMappings =
      new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, R>> targets =
      new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, T>> sources =
      new ConcurrentHashMap<>();


  @Override
  public Iterator<Mapping<T, R>> iterator() {
    return mappings.values().iterator();
  }

  public boolean containsMapping(Mapping<T, R> mapping) {
    if (mappings.containsKey(mapping.getId())) {
      if (mapping.equals(mappings.get(mapping.getId()))) {
        return true;
      } else {
        throw new IllegalStateException("Two different mappings with same id: " + mapping.getId());
      }
    } else {
      return false;
    }
  }

  public boolean addMapping(Mapping<T, R> mapping) {
    if (mappings.containsKey(mapping.getId())) {
      // check whether same
      Mapping<T, R> current = mappings.get(mapping.getId());
      if (current.equals(mapping)) {
        return false;
      } else {
        throw new IllegalStateException("Two different mappings with same id: " + mapping.getId());
      }
    } else {
      mappings.put(mapping.getId(), mapping);
      return true;
    }
  }

  public boolean removeMapping(Mapping<T, R> mapping) {
    if (!containsMapping(mapping)) {
      return false;
    } else {
      mappings.remove(mapping.getId());
      return true;
    }
  }

  public Set<T> getSources(R resource) {
    return null;
  }

  public Set<R> getTargets(T task) {
    return null;
  }

  public Set<Mapping<T, R>> getMappings(T task) {
    return null;
  }

  public Set<Mapping<T, R>> getMappings(R resource) {
    return null;
  }
}
