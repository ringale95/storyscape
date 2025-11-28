package edu.neu.csye6200.service;

import edu.neu.csye6200.entity.Tag;
import edu.neu.csye6200.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Get or create tag by name
     */
    public Tag getOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> {
                    Tag tag = new Tag(name);
                    return tagRepository.save(tag);
                });
    }

    /**
     * Get or create multiple tags
     */
    public Set<Tag> getOrCreateTags(List<String> tagNames) {
        return tagNames.stream()
                .map(this::getOrCreateTag)
                .collect(Collectors.toSet());
    }

    /**
     * Create a new tag
     */
    public Tag createTag(String name, String description) {
        if (tagRepository.existsByName(name)) {
            throw new IllegalArgumentException("Tag already exists: " + name);
        }

        Tag tag = new Tag(name, description);
        return tagRepository.save(tag);
    }

    /**
     * Get all tags
     */
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    /**
     * Get tag by ID
     */
    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));
    }

    /**
     * Search tags by name
     */
    public List<Tag> searchTags(String query) {
        return tagRepository.findAll().stream()
                .filter(tag -> tag.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Delete tag
     */
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}