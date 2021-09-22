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
public class MappingsConcurrent implements Iterable<Mapping<Task, Resource>> {

  protected final ConcurrentHashMap<String, Mapping<Task, Resource>> mappings =
      new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, Mapping<Task, Resource>>> taskMappings =
      new ConcurrentHashMap<>();
  protected final ConcurrentHashMap<String, ConcurrentHashMap<String, Mapping<Task, Resource>>> resourceMappings =
      new ConcurrentHashMap<>();

  @Override
  public Iterator<Mapping<Task, Resource>> iterator() {
    return mappings.values().iterator();
  }

  public boolean containsMapping(Mapping<Task, Resource> mapping) {
    return mappings.containsKey(mapping.getId());
  }

  public boolean addMapping(Mapping<Task, Resource> mapping) {
    if (mappings.containsKey(mapping.getId())) {
      return false;
    } else {
      mappings.put(mapping.getId(), mapping);
      Resource tar = mapping.getTarget();
      Task src = mapping.getSource();
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

  public boolean removeMapping(Mapping<Task, Resource> mapping) {
    if (!containsMapping(mapping)) {
      return false;
    } else {
      mappings.remove(mapping.getId());
      Resource tar = mapping.getTarget();
      Task src = mapping.getSource();
      taskMappings.get(src.getId()).remove(mapping.getId());
      resourceMappings.get(tar.getId()).remove(mapping.getId());
      return true;
    }
  }

  public Set<Task> getSources(Resource resource) {
    if (resourceMappings.containsKey(resource.getId())) {
      return resourceMappings.get(resource.getId()).values().stream()
          .map(mapping -> mapping.getSource()).collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }

  public Set<Resource> getTargets(Task task) {
    if (taskMappings.containsKey(task.getId())) {
      return taskMappings.get(task.getId()).values().stream().map(mapping -> mapping.getTarget())
          .collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }

  public Set<Mapping<Task, Resource>> getMappings(Task task) {
    return new HashSet<>(taskMappings.get(task.getId()).values());
  }

  public Set<Mapping<Task, Resource>> getMappings(Resource resource) {
    return new HashSet<>(resourceMappings.get(resource.getId()).values());
  }
}
