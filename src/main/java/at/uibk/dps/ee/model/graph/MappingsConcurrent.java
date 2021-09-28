package at.uibk.dps.ee.model.graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

  /**
   * Returns a {@link Stream} of the contained mappings.
   * 
   * @return a {@link Stream} of the contained mappings
   */
  public Stream<Mapping<Task, Resource>> mappingStream() {
    return mappings.values().stream();
  }

  /**
   * Returns true if the mapping set contains the given mapping
   * 
   * @param mapping the given mapping
   * @return true if the mapping set contains the given mapping
   */
  public boolean containsMapping(final Mapping<Task, Resource> mapping) {
    return mappings.containsKey(mapping.getId());
  }

  /**
   * Adds the given mapping to the mapping set. Returns true if the set was
   * altered as the result.
   * 
   * @param mapping the mapping to add
   * @return true if the set was altered as the result of the addition
   */
  public boolean addMapping(final Mapping<Task, Resource> mapping) {
    if (mappings.containsKey(mapping.getId())) {
      return false;
    } else {
      mappings.put(mapping.getId(), mapping);
      final Resource tar = mapping.getTarget();
      final Task src = mapping.getSource();
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

  /**
   * Removes the given mapping from the mapping set. Returns true iff the set was
   * altered as the result.
   * 
   * @param mapping the mapping to remove
   * @return true if the set was altered as the result of removing the mapping.
   */
  public boolean removeMapping(Mapping<Task, Resource> mapping) {
    if (!containsMapping(mapping)) {
      return false;
    } else {
      mappings.remove(mapping.getId());
      final Resource tar = mapping.getTarget();
      final Task src = mapping.getSource();
      taskMappings.get(src.getId()).remove(mapping.getId());
      resourceMappings.get(tar.getId()).remove(mapping.getId());
      return true;
    }
  }

  /**
   * Returns all source tasks for the given resource, i.e., the sources of all
   * mappings with the given resource as target.
   * 
   * @param resource the given resource
   * @return all source tasks for the given resource, i.e., the sources of all
   *         mappings with the given resource as target
   */
  public Set<Task> getSources(final Resource resource) {
    if (resourceMappings.containsKey(resource.getId())) {
      return resourceMappings.get(resource.getId()).values().stream()
          .map(mapping -> mapping.getSource()).collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }

  /**
   * Returns all target resources for the given task, i.e., the targets of all
   * mappings with the given task as source.
   * 
   * @param task the given task
   * @return all target resources for the given task, i.e., the targets of all
   *         mappings with the given task as source
   */
  public Set<Resource> getTargets(final Task task) {
    if (taskMappings.containsKey(task.getId())) {
      return taskMappings.get(task.getId()).values().stream().map(mapping -> mapping.getTarget())
          .collect(Collectors.toSet());
    } else {
      return new HashSet<>();
    }
  }

  /**
   * Returns all mappings mapping the given task.
   * 
   * @param task the given task
   * @return all mappings mapping the given task
   */
  public Set<Mapping<Task, Resource>> getMappings(final Task task) {
    return new HashSet<>(taskMappings.get(task.getId()).values());
  }

  /**
   * Returns all mappings mapping onto the given resource.
   * 
   * @param resource the given resource
   * @return all mappings mapping onto the given resource
   */
  public Set<Mapping<Task, Resource>> getMappings(final Resource resource) {
    return new HashSet<>(resourceMappings.get(resource.getId()).values());
  }
}
