package edu.neu.csye6200.service;

import edu.neu.csye6200.dto.StoryDTO;
import edu.neu.csye6200.entity.*;
import edu.neu.csye6200.factory.StoryFactory;
import edu.neu.csye6200.repository.StoryRepository;
import edu.neu.csye6200.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Story service implementing business logic
 * Demonstrates Abstraction, Factory Pattern, and Java Streams
 */
@Service
@Transactional
public class StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final TagService tagService;
    private final StoryFactory storyFactory;

    public StoryService(StoryRepository storyRepository,
                        UserRepository userRepository,
                        TagService tagService,
                        StoryFactory storyFactory) {
        this.storyRepository = storyRepository;
        this.userRepository = userRepository;
        this.tagService = tagService;
        this.storyFactory = storyFactory;
    }

    /**
     * Create a new story using StoryFactory
     * Demonstrates Factory Pattern usage
     */
    public Story createStory(StoryDTO dto, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use StoryFactory to create story with defaults
        Story story = storyFactory.createStory(author, dto.getTitle(), dto.getBody());

        // Set visibility if provided
        if (dto.getVisibility() != null) {
            StoryVisibility visibility = StoryVisibility.valueOf(dto.getVisibility());

            // Validate user can create this visibility level
            if (!storyFactory.canUserCreateStoryWithVisibility(author, visibility)) {
                throw new RuntimeException("Your tier cannot create " + visibility + " stories");
            }

            story.setVisibility(visibility);
        }

        // Set content type if provided
        if (dto.getContentType() != null) {
            storyFactory.applyContentTypeDefaults(story, dto.getContentType());
        }

        // Add tags if provided
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            Set<Tag> tags = tagService.getOrCreateTags(dto.getTags());
            story.setTags(tags);
        }

        return storyRepository.save(story);
    }

    /**
     * Create a draft article using factory
     */
    public Story createDraftArticle(String title, String body, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Story story = storyFactory.createDraftArticle(author, title, body);
        return storyRepository.save(story);
    }

    /**
     * Create a blog post using factory
     */
    public Story createBlogPost(String title, String body, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Story story = storyFactory.createBlogPost(author, title, body);
        return storyRepository.save(story);
    }

    /**
     * Create a tutorial using factory
     */
    public Story createTutorial(String title, String body, List<String> tagNames, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Tag> tags = tagService.getOrCreateTags(tagNames);
        Story story = storyFactory.createTutorial(author, title, body, tags);
        return storyRepository.save(story);
    }

    /**
     * Create premium story using factory
     */
    public Story createPremiumStory(String title, String body, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Story story = storyFactory.createPremiumStory(author, title, body);
            return storyRepository.save(story);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Create exclusive story using factory
     */
    public Story createExclusiveStory(String title, String body, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            Story story = storyFactory.createExclusiveStory(author, title, body);
            return storyRepository.save(story);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Create story with tier-based defaults using factory
     */
    public Story createStoryForUserTier(String title, String body, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Story story = storyFactory.createStoryForUserTier(author, title, body);
        return storyRepository.save(story);
    }

    /**
     * Update a story
     */
    public Story updateStory(Long storyId, StoryDTO dto, Long userId) {
        Story story = getStoryById(storyId);

        // Check if user is the author
        if (!story.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own stories");
        }

        // Update fields
        if (dto.getTitle() != null) {
            story.setTitle(dto.getTitle());
        }
        if (dto.getBody() != null) {
            story.setBody(dto.getBody());
        }
        if (dto.getVisibility() != null) {
            StoryVisibility visibility = StoryVisibility.valueOf(dto.getVisibility());

            // Validate user can create this visibility level
            if (!storyFactory.canUserCreateStoryWithVisibility(story.getAuthor(), visibility)) {
                throw new RuntimeException("Your tier cannot set " + visibility + " visibility");
            }

            story.setVisibility(visibility);
        }

        // Update tags if provided
        if (dto.getTags() != null) {
            story.clearTags();
            Set<Tag> tags = tagService.getOrCreateTags(dto.getTags());
            story.setTags(tags);
        }

        return storyRepository.save(story);
    }

    /**
     * Publish a story
     */
    public Story publishStory(Long storyId, Long userId) {
        Story story = getStoryById(storyId);

        // Check if user is the author
        if (!story.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You can only publish your own stories");
        }

        story.publish();
        return storyRepository.save(story);
    }

    /**
     * Archive a story
     */
    public Story archiveStory(Long storyId, Long userId) {
        Story story = getStoryById(storyId);

        // Check if user is the author
        if (!story.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You can only archive your own stories");
        }

        story.archive();
        return storyRepository.save(story);
    }

    /**
     * Delete a story
     */
    public void deleteStory(Long storyId, Long userId) {
        Story story = getStoryById(storyId);

        // Check if user is the author
        if (!story.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own stories");
        }

        storyRepository.delete(story);
    }

    /**
     * Get story by ID
     */
    @Transactional(readOnly = true)
    public Story getStoryById(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Story not found with id: " + id));
    }

    /**
     * Get story by ID and increment view count
     */
    public Story getStoryByIdAndIncrementViews(Long id) {
        Story story = getStoryById(id);
        story.incrementViewCount();
        return storyRepository.save(story);
    }

    /**
     * Get all stories by user
     */
    @Transactional(readOnly = true)
    public List<Story> getUserStories(Long userId) {
        return storyRepository.findByAuthorId(userId);
    }

    /**
     * Get published stories by user
     */
    @Transactional(readOnly = true)
    public List<Story> getUserPublishedStories(Long userId) {
        return storyRepository.findByAuthorIdAndStatus(userId, StoryStatus.PUBLISHED);
    }

    /**
     * Get all published stories
     */
    @Transactional(readOnly = true)
    public List<Story> getAllPublishedStories() {
        return storyRepository.findByStatusOrderByPublishedAtDesc(StoryStatus.PUBLISHED);
    }

    /**
     * Get featured stories
     */
    @Transactional(readOnly = true)
    public List<Story> getFeaturedStories() {
        return storyRepository.findByIsFeaturedTrueAndStatus(StoryStatus.PUBLISHED);
    }

    /**
     * Get trending stories (last 7 days, most viewed)
     * Demonstrates Java Streams
     */
    @Transactional(readOnly = true)
    public List<Story> getTrendingStories(int limit) {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        return storyRepository.findTrending(weekAgo).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get recent stories
     * Demonstrates Java Streams
     */
    @Transactional(readOnly = true)
    public List<Story> getRecentStories(int days, int limit) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return storyRepository.findRecentPublished(since).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Search stories
     */
    @Transactional(readOnly = true)
    public List<Story> searchStories(String query) {
        return storyRepository.searchStories(query);
    }

    /**
     * Get stories by tag
     */
    @Transactional(readOnly = true)
    public List<Story> getStoriesByTag(String tagName) {
        return storyRepository.findByTagName(tagName);
    }

    /**
     * Like a story
     */
    public Story likeStory(Long storyId) {
        Story story = getStoryById(storyId);
        story.incrementLikeCount();
        return storyRepository.save(story);
    }

    /**
     * Make story featured (Admin only)
     */
    public Story makeStoryFeatured(Long storyId, boolean featured) {
        Story story = getStoryById(storyId);
        story.setIsFeatured(featured);
        return storyRepository.save(story);
    }

    /**
     * Make story boosted (Admin or product purchase)
     */
    public Story makeStoryBoosted(Long storyId, boolean boosted) {
        Story story = getStoryById(storyId);
        story.setIsBoosted(boosted);
        return storyRepository.save(story);
    }

    /**
     * Clone a story from template using factory
     */
    public Story cloneStory(Long templateId, String newTitle, Long userId) {
        User newAuthor = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Story template = getStoryById(templateId);
        Story clonedStory = storyFactory.createFromTemplate(template, newAuthor, newTitle);

        return storyRepository.save(clonedStory);
    }

    /**
     * Get user story statistics using Java Streams
     * Demonstrates Java Streams for data aggregation
     */
    @Transactional(readOnly = true)
    public StoryStatistics getUserStatistics(Long userId) {
        List<Story> userStories = getUserStories(userId);

        long totalStories = userStories.size();

        long publishedStories = userStories.stream()
                .filter(s -> s.getStatus() == StoryStatus.PUBLISHED)
                .count();

        int totalViews = userStories.stream()
                .mapToInt(Story::getViewCount)
                .sum();

        int totalLikes = userStories.stream()
                .mapToInt(Story::getLikeCount)
                .sum();

        return new StoryStatistics(totalStories, publishedStories, totalViews, totalLikes);
    }

    /**
     * Get stories by multiple tags using Java Streams
     * Demonstrates complex stream operations
     * NO SORTING - Removed to fix compilation errors
     */
    @Transactional(readOnly = true)
    public List<Story> getStoriesByMultipleTags(List<String> tagNames, int limit) {
        return storyRepository.findAll().stream()
                .filter(story -> story.getStatus() == StoryStatus.PUBLISHED)
                .filter(story -> {
                    Set<String> storyTagNames = story.getTags().stream()
                            .map(Tag::getName)
                            .collect(Collectors.toSet());
                    return tagNames.stream().anyMatch(storyTagNames::contains);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get most liked stories using Java Streams
     */
    @Transactional(readOnly = true)
    public List<Story> getMostLikedStories(int limit) {
        return storyRepository.findByStatus(StoryStatus.PUBLISHED).stream()
                .sorted((s1, s2) -> Integer.compare(s2.getLikeCount(), s1.getLikeCount()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get stories by visibility level
     * Demonstrates filtering with enums
     * NO SORTING - Removed to fix compilation errors
     */
    @Transactional(readOnly = true)
    public List<Story> getStoriesByVisibility(StoryVisibility visibility) {
        return storyRepository.findAll().stream()
                .filter(story -> story.getStatus() == StoryStatus.PUBLISHED)
                .filter(story -> story.getVisibility() == visibility)
                .collect(Collectors.toList());
    }

    /**
     * Get stories accessible by user tier
     * Demonstrates business logic with streams
     * NO SORTING - Removed to fix compilation errors
     */
    @Transactional(readOnly = true)
    public List<Story> getStoriesAccessibleByTier(Tier userTier, int limit) {
        return storyRepository.findByStatus(StoryStatus.PUBLISHED).stream()
                .filter(story -> story.isAccessibleBy(userTier))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get aggregate statistics for all stories
     * Demonstrates complex aggregation with streams
     */
    @Transactional(readOnly = true)
    public PlatformStatistics getPlatformStatistics() {
        List<Story> allStories = storyRepository.findAll();

        long totalStories = allStories.size();

        long publishedStories = allStories.stream()
                .filter(s -> s.getStatus() == StoryStatus.PUBLISHED)
                .count();

        int totalViews = allStories.stream()
                .mapToInt(Story::getViewCount)
                .sum();

        int totalLikes = allStories.stream()
                .mapToInt(Story::getLikeCount)
                .sum();

        // Group by content type
        var storiesByType = allStories.stream()
                .filter(s -> s.getStatus() == StoryStatus.PUBLISHED)
                .collect(Collectors.groupingBy(
                        Story::getContentType,
                        Collectors.counting()
                ));

        // Group by visibility
        var storiesByVisibility = allStories.stream()
                .filter(s -> s.getStatus() == StoryStatus.PUBLISHED)
                .collect(Collectors.groupingBy(
                        Story::getVisibility,
                        Collectors.counting()
                ));

        return new PlatformStatistics(
                totalStories,
                publishedStories,
                totalViews,
                totalLikes,
                storiesByType,
                storiesByVisibility
        );
    }

    /**
     * Inner class for user statistics
     */
    public static class StoryStatistics {
        private long totalStories;
        private long publishedStories;
        private int totalViews;
        private int totalLikes;

        public StoryStatistics(long totalStories, long publishedStories,
                               int totalViews, int totalLikes) {
            this.totalStories = totalStories;
            this.publishedStories = publishedStories;
            this.totalViews = totalViews;
            this.totalLikes = totalLikes;
        }

        // Getters
        public long getTotalStories() { return totalStories; }
        public long getPublishedStories() { return publishedStories; }
        public int getTotalViews() { return totalViews; }
        public int getTotalLikes() { return totalLikes; }
    }

    /**
     * Inner class for platform-wide statistics
     */
    public static class PlatformStatistics {
        private long totalStories;
        private long publishedStories;
        private int totalViews;
        private int totalLikes;
        private java.util.Map<String, Long> storiesByType;
        private java.util.Map<StoryVisibility, Long> storiesByVisibility;

        public PlatformStatistics(long totalStories, long publishedStories,
                                  int totalViews, int totalLikes,
                                  java.util.Map<String, Long> storiesByType,
                                  java.util.Map<StoryVisibility, Long> storiesByVisibility) {
            this.totalStories = totalStories;
            this.publishedStories = publishedStories;
            this.totalViews = totalViews;
            this.totalLikes = totalLikes;
            this.storiesByType = storiesByType;
            this.storiesByVisibility = storiesByVisibility;
        }

        // Getters
        public long getTotalStories() { return totalStories; }
        public long getPublishedStories() { return publishedStories; }
        public int getTotalViews() { return totalViews; }
        public int getTotalLikes() { return totalLikes; }
        public java.util.Map<String, Long> getStoriesByType() { return storiesByType; }
        public java.util.Map<StoryVisibility, Long> getStoriesByVisibility() { return storiesByVisibility; }
    }
}