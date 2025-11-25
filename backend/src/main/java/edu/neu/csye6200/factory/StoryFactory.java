package edu.neu.csye6200.factory;

import edu.neu.csye6200.entity.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Story Factory - Factory Pattern
 * Similar to UserFactory and ProductFactory
 * Creates Story instances with proper defaults
 * Demonstrates Factory Pattern
 */
@Component
public class StoryFactory {

    private static StoryFactory instance;

    // Private constructor for singleton
    private StoryFactory() {
    }

    /**
     * Get singleton instance
     */
    public static StoryFactory getInstance() {
        if (instance == null) {
            synchronized (StoryFactory.class) {
                if (instance == null) {
                    instance = new StoryFactory();
                }
            }
        }
        return instance;
    }

    /**
     * Create a basic story with defaults
     * Main factory method
     */
    public Story createStory(User author, String title, String body) {
        validateInputs(author, title, body);

        Story story = new Story();
        story.setAuthor(author);
        story.setTitle(title);
        story.setBody(body);

        // Apply defaults
        story.setStatus(StoryStatus.DRAFT);
        story.setVisibility(StoryVisibility.PUBLIC);
        story.setContentType("ARTICLE");
        story.setViewCount(0);
        story.setLikeCount(0);
        story.setIsFeatured(false);
        story.setIsBoosted(false);
        story.setTags(new HashSet<>());

        return story;
    }

    /**
     * Create a story with specific visibility
     */
    public Story createStory(User author, String title, String body, StoryVisibility visibility) {
        Story story = createStory(author, title, body);
        story.setVisibility(visibility);
        return story;
    }

    /**
     * Create a story with tags
     */
    public Story createStory(User author, String title, String body, Set<Tag> tags) {
        Story story = createStory(author, title, body);
        story.setTags(tags);
        return story;
    }

    /**
     * Create a story with all options
     */
    public Story createStory(User author, String title, String body,
                             StoryVisibility visibility, String contentType, Set<Tag> tags) {
        Story story = createStory(author, title, body);
        story.setVisibility(visibility);
        story.setContentType(contentType);
        if (tags != null) {
            story.setTags(tags);
        }
        return story;
    }

    /**
     * Create a draft article
     */
    public Story createDraftArticle(User author, String title, String body) {
        Story story = createStory(author, title, body);
        story.setStatus(StoryStatus.DRAFT);
        story.setContentType("ARTICLE");
        story.setVisibility(StoryVisibility.PUBLIC);
        return story;
    }

    /**
     * Create a blog post
     */
    public Story createBlogPost(User author, String title, String body) {
        Story story = createStory(author, title, body);
        story.setContentType("BLOG");
        story.setVisibility(StoryVisibility.PUBLIC);
        return story;
    }

    /**
     * Create a tutorial
     */
    public Story createTutorial(User author, String title, String body, Set<Tag> tags) {
        Story story = createStory(author, title, body, tags);
        story.setContentType("TUTORIAL");
        story.setVisibility(StoryVisibility.PUBLIC);
        return story;
    }

    /**
     * Create a premium story (subscriber only)
     */
    public Story createPremiumStory(User author, String title, String body) {
        // Only CORE or GOLD tier users can create premium stories
        if (!author.getTier().isPremium()) {
            throw new IllegalArgumentException("Only premium users can create premium stories");
        }

        Story story = createStory(author, title, body);
        story.setVisibility(StoryVisibility.SUBSCRIBER_ONLY);
        story.setContentType("PREMIUM");
        return story;
    }

    /**
     * Create an exclusive story (core tier only)
     */
    public Story createExclusiveStory(User author, String title, String body) {
        // Only CORE tier users can create exclusive stories
        if (author.getTier() != Tier.CORE) {
            throw new IllegalArgumentException("Only CORE tier users can create exclusive stories");
        }

        Story story = createStory(author, title, body);
        story.setVisibility(StoryVisibility.PREMIUM_ONLY);
        story.setContentType("EXCLUSIVE");
        return story;
    }

    /**
     * Create a featured story (admin only)
     */
    public Story createFeaturedStory(User author, String title, String body, boolean featured) {
        Story story = createStory(author, title, body);
        story.setIsFeatured(featured);
        story.setStatus(StoryStatus.PUBLISHED);
        story.setPublishedAt(java.time.LocalDateTime.now());
        return story;
    }

    /**
     * Create a boosted story (purchased product)
     */
    public Story createBoostedStory(User author, String title, String body, boolean boosted) {
        Story story = createStory(author, title, body);
        story.setIsBoosted(boosted);
        return story;
    }

    /**
     * Create story from template
     * Useful for cloning or creating similar stories
     */
    public Story createFromTemplate(Story template, User newAuthor, String newTitle) {
        if (template == null) {
            throw new IllegalArgumentException("Template story cannot be null");
        }

        Story story = new Story();
        story.setAuthor(newAuthor);
        story.setTitle(newTitle);
        story.setBody(template.getBody());
        story.setContentType(template.getContentType());
        story.setVisibility(template.getVisibility());
        story.setStatus(StoryStatus.DRAFT);
        story.setViewCount(0);
        story.setLikeCount(0);
        story.setIsFeatured(false);
        story.setIsBoosted(false);

        // Copy tags if present
        if (template.getTags() != null && !template.getTags().isEmpty()) {
            story.setTags(new HashSet<>(template.getTags()));
        }

        return story;
    }

    /**
     * Apply content type specific defaults
     * Demonstrates Polymorphism through type-based behavior
     */
    public void applyContentTypeDefaults(Story story, String contentType) {
        switch (contentType.toUpperCase()) {
            case "ARTICLE":
                story.setContentType("ARTICLE");
                // Articles are typically longer form content
                break;

            case "BLOG":
                story.setContentType("BLOG");
                // Blog posts are more casual
                break;

            case "TUTORIAL":
                story.setContentType("TUTORIAL");
                // Tutorials are educational
                break;

            case "NEWS":
                story.setContentType("NEWS");
                // News articles are time-sensitive
                break;

            case "REVIEW":
                story.setContentType("REVIEW");
                // Reviews evaluate something
                break;

            case "OPINION":
                story.setContentType("OPINION");
                // Opinion pieces are subjective
                break;

            default:
                story.setContentType("ARTICLE");
        }
    }

    /**
     * Create published story ready to go live
     */
    public Story createPublishedStory(User author, String title, String body, Set<Tag> tags) {
        Story story = createStory(author, title, body, tags);
        story.setStatus(StoryStatus.PUBLISHED);
        story.setPublishedAt(java.time.LocalDateTime.now());
        return story;
    }

    /**
     * Validate story creation inputs
     */
    private void validateInputs(User author, String title, String body) {
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Body cannot be empty");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("Title must not exceed 200 characters");
        }
    }

    /**
     * Validate story visibility based on user tier
     */
    public boolean canUserCreateStoryWithVisibility(User user, StoryVisibility visibility) {
        switch (visibility) {
            case PUBLIC:
                return true; // Everyone can create public stories

            case SUBSCRIBER_ONLY:
                return user.getTier().isPremium(); // CORE or GOLD

            case PREMIUM_ONLY:
                return user.getTier() == Tier.CORE; // Only CORE

            default:
                return false;
        }
    }

    /**
     * Get recommended visibility for user tier
     */
    public StoryVisibility getRecommendedVisibility(User user) {
        switch (user.getTier()) {
            case CORE:
                return StoryVisibility.PREMIUM_ONLY;
            case GOLD:
            case SILVER:
                return StoryVisibility.SUBSCRIBER_ONLY;
            case NORMAL:
            case OTHER:
            default:
                return StoryVisibility.PUBLIC;
        }
    }

    /**
     * Create story with tier-based defaults
     * Automatically applies best settings for user's tier
     */
    public Story createStoryForUserTier(User author, String title, String body) {
        Story story = createStory(author, title, body);

        // Apply tier-based defaults
        story.setVisibility(getRecommendedVisibility(author));

        // Premium users get additional benefits
        if (author.getTier().isPremium()) {
            story.setContentType("PREMIUM");
        }

        return story;
    }

    /**
     * Bulk create stories for testing/seeding
     */
    public java.util.List<Story> createMultipleStories(User author,
                                                       java.util.List<StoryData> storyDataList) {
        return storyDataList.stream()
                .map(data -> createStory(author, data.title, data.body))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Helper class for bulk story creation
     */
    public static class StoryData {
        public String title;
        public String body;

        public StoryData(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }
}