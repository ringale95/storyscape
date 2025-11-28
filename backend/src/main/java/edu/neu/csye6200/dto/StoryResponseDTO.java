package edu.neu.csye6200.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import edu.neu.csye6200.entity.Story;
import edu.neu.csye6200.entity.Tag;

/**
 * DTO for Story response - contains only essential information
 */
public class StoryResponseDTO {

    private Long id;
    private String title;
    private String status;
    private String visibility;
    private String contentType;
    private Long authorId;
    private String authorName;
    private List<String> tagNames;
    private LocalDateTime createdAt;

    // Constructors
    public StoryResponseDTO() {
    }

    public StoryResponseDTO(Long id, String title, String status, String visibility,
            String contentType, Long authorId, String authorName,
            List<String> tagNames, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.visibility = visibility;
        this.contentType = contentType;
        this.authorId = authorId;
        this.authorName = authorName;
        this.tagNames = tagNames;
        this.createdAt = createdAt;
    }

    /**
     * Convert Story entity to StoryResponseDTO
     */
    public static StoryResponseDTO fromStory(Story story) {
        List<String> tagNames = story.getTags() != null
                ? story.getTags().stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList())
                : null;

        return new StoryResponseDTO(
                story.getId(),
                story.getTitle(),
                story.getStatus() != null ? story.getStatus().name() : null,
                story.getVisibility() != null ? story.getVisibility().name() : null,
                story.getContentType(),
                story.getAuthor() != null ? story.getAuthor().getId() : null,
                story.getAuthor() != null ? story.getAuthor().getUsername() : null,
                tagNames,
                story.getCreatedAt());
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(List<String> tagNames) {
        this.tagNames = tagNames;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
