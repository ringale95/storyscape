package edu.neu.csye6200.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Story entity representing blog posts and articles
 * Demonstrates Encapsulation and business logic
 */
@Entity
@Table(name = "stories")
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User author;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(length = 50)
    private String contentType = "ARTICLE";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StoryVisibility visibility = StoryVisibility.PUBLIC;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoryStatus status = StoryStatus.DRAFT;

    @Column(nullable = false)
    private Integer viewCount = 0;

    @Column(nullable = false)
    private Integer likeCount = 0;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "story_tags",
            joinColumns = @JoinColumn(name = "story_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable = false)
    private Boolean isFeatured = false;

    @Column(nullable = false)
    private Boolean isBoosted = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime publishedAt;

    // Constructors
    public Story() {
    }

    public Story(User author, String title, String body) {
        this.author = author;
        this.title = title;
        this.body = body;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business methods

    /**
     * Publish the story
     */
    public void publish() {
        this.status = StoryStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    /**
     * Archive the story
     */
    public void archive() {
        this.status = StoryStatus.ARCHIVED;
    }

    /**
     * Increment view count
     */
    public void incrementViewCount() {
        this.viewCount++;
    }

    /**
     * Increment like count
     */
    public void incrementLikeCount() {
        this.likeCount++;
    }

    /**
     * Check if story is accessible by a given user tier
     */
    public boolean isAccessibleBy(Tier userTier) {
        switch (visibility) {
            case PUBLIC:
                return true;
            case SUBSCRIBER_ONLY:
                return userTier.isPremium();
            case PREMIUM_ONLY:
                return userTier == Tier.CORE;
            default:
                return false;
        }
    }

    /**
     * Calculate feed score for personalization
     * Used by FeedService
     */
    public double calculateFeedScore(User user) {
        double score = 0.0;

        // Recency score
        LocalDateTime referenceDate = publishedAt != null ? publishedAt : createdAt;
        long daysSincePublished = java.time.Duration.between(
                referenceDate,
                LocalDateTime.now()
        ).toDays();
        score += Math.max(0, 100 - daysSincePublished);

        // Engagement score
        score += (viewCount * 0.1) + (likeCount * 2);

        // Featured/boosted content
        if (isFeatured) score += 500;
        if (isBoosted) score += 200;

        return score;
    }

    /**
     * Add tag to story
     */
    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new HashSet<>();
        }
        tags.add(tag);
    }

    /**
     * Remove tag from story
     */
    public void removeTag(Tag tag) {
        if (tags != null) {
            tags.remove(tag);
        }
    }

    /**
     * Clear all tags
     */
    public void clearTags() {
        if (tags != null) {
            tags.clear();
        }
    }

    /**
     * Check if story has a specific tag
     */
    public boolean hasTag(String tagName) {
        if (tags == null) return false;
        return tags.stream()
                .anyMatch(tag -> tag.getName().equalsIgnoreCase(tagName));
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public StoryVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(StoryVisibility visibility) {
        this.visibility = visibility;
    }

    public StoryStatus getStatus() {
        return status;
    }

    public void setStatus(StoryStatus status) {
        this.status = status;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Boolean getIsBoosted() {
        return isBoosted;
    }

    public void setIsBoosted(Boolean isBoosted) {
        this.isBoosted = isBoosted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + (author != null ? author.getUsername() : "null") +
                ", status=" + status +
                ", visibility=" + visibility +
                '}';
    }
}