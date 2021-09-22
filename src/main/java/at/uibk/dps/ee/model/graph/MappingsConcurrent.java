package at.uibk.dps.ee.model.graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
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

  @Override
  public Iterator<Mapping<T, R>> iterator() {
    return mappings.values().iterator();
  }

  public boolean containsMapping(Mapping<T, R> mapping) {
    return mappings.containsKey(mapping.getId());
  }

  public boolean addMapping(Mapping<T, R> mapping) {
    if (mappings.containsKey(mapping.getId())) {
      return false;
    } else {
      mappings.put(mapping.getId(), mapping);
      R tar = mapping.getTarget();
      T src = mapping.getSource();
      // housekeeping ...
      // ... the task mappings,
      taskMappings.putIfAbsent(src.getId(), new ConcurrentHashMap<>());
      taskMappings.get(src.getId()).put(mapping.getId(), mapping);
      // ... and the resource mappings
      resourceMappings.putIfAbsent(tar.getId(), new ConcurrentHashMap<>());
      resourceMappings.get(tar.getId()).put(mapping.getId(), mapping);
      return true;
    }
  }

  public boolean removeMapping(Mapping<T, R> mapping) {
    if (!containsMapping(mapping)) {
      return false;
    } else {
      mappings.remove(mapping.getId());
      R tar = mapping.getTarget();
      T src = mapping.getSource();
      taskMappings.get(src.getId()).remove(mapping.getId());
      resourceMappings.get(tar.getId()).remove(mapping.getId());
      return true;
    }
  }

  public Set<T> getSources(R resource) {
    if (resourceMappings.containsKey(resource.getId())) {
      return resourceMappings.get(resource.getId()).values().stream()
          .map(mapping -> mapping.getSource()).collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }

  public Set<R> getTargets(T task) {
    if (taskMappings.containsKey(task.getId())) {
      return taskMappings.get(task.getId()).values().stream().map(mapping -> mapping.getTarget())
          .collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }

  public Set<Mapping<T, R>> getMappings(T task) {
    return new HashSet<>(taskMappings.get(task.getId()).values());
  }

  public Set<Mapping<T, R>> getMappings(R resource) {
    return new HashSet<>(resourceMappings.get(resource.getId()).values());
  }
}
