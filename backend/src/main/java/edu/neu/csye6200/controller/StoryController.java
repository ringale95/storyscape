package edu.neu.csye6200.controller;

import edu.neu.csye6200.dto.StoryDTO;
import edu.neu.csye6200.dto.StoryResponseDTO;
import edu.neu.csye6200.entity.Story;
import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.repository.UserRepository;
import edu.neu.csye6200.service.StoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Story management
 */
@RestController
@RequestMapping("/api/stories")
public class StoryController {

    private final StoryService storyService;
    private final UserRepository userRepository;

    public StoryController(StoryService storyService, UserRepository userRepository) {
        this.storyService = storyService;
        this.userRepository = userRepository;
    }

    /**
     * Create a new story
     * POST /api/stories
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStory(
            @RequestBody StoryDTO dto,
            Authentication authentication) {

        try {
            // Check if user is authenticated
            if (authentication == null || !authentication.isAuthenticated()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "Authentication required. Please provide a valid JWT token.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // Extract email from JWT token
            String email = authentication.getName();

            // Look up user by email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found: " + email);
            }

            Story story = storyService.createStory(dto, user.getId());

            // Convert to response DTO with limited information
            StoryResponseDTO storyResponse = StoryResponseDTO.fromStory(story);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Story created successfully");
            response.put("story", storyResponse);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get all published stories
     * GET /api/stories
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStories() {
        List<Story> stories = storyService.getAllPublishedStories();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", stories.size());
        response.put("stories", stories);

        return ResponseEntity.ok(response);
    }

    /**
     * Get story by ID
     * GET /api/stories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStory(@PathVariable Long id) {
        try {
            Story story = storyService.getStoryByIdAndIncrementViews(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("story", story);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Update story
     * PUT /api/stories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStory(
            @PathVariable Long id,
            @RequestBody StoryDTO dto,
            Authentication authentication) {

        try {
            // Extract email from JWT token
            String email = authentication.getName();

            // Look up user by email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found: " + email);
            }

            Story story = storyService.updateStory(id, dto, user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Story updated successfully");
            response.put("story", story);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Publish story
     * PATCH /api/stories/{id}/publish
     */
    @PatchMapping("/{id}/publish")
    public ResponseEntity<Map<String, Object>> publishStory(
            @PathVariable Long id,
            Authentication authentication) {

        try {
            // Extract email from JWT token
            String email = authentication.getName();

            // Look up user by email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found: " + email);
            }

            Story story = storyService.publishStory(id, user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Story published successfully");
            response.put("story", story);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Archive story
     * PATCH /api/stories/{id}/archive
     */
    @PatchMapping("/{id}/archive")
    public ResponseEntity<Map<String, Object>> archiveStory(
            @PathVariable Long id,
            Authentication authentication) {

        try {
            // Extract email from JWT token
            String email = authentication.getName();

            // Look up user by email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found: " + email);
            }

            Story story = storyService.archiveStory(id, user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Story archived successfully");
            response.put("story", story);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Delete story
     * DELETE /api/stories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStory(
            @PathVariable Long id,
            Authentication authentication) {

        try {
            // Extract email from JWT token
            String email = authentication.getName();

            // Look up user by email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found: " + email);
            }

            storyService.deleteStory(id, user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Story deleted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get user stories
     * GET /api/stories/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStories(@PathVariable Long userId) {
        List<Story> stories = storyService.getUserPublishedStories(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", stories.size());
        response.put("stories", stories);

        return ResponseEntity.ok(response);
    }

    /**
     * Get my stories (authenticated user)
     * GET /api/stories/my
     */
    @GetMapping("/my")
    public ResponseEntity<Map<String, Object>> getMyStories(
            Authentication authentication) {

        // Extract email from JWT token
        String email = authentication.getName();

        // Look up user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found: " + email);
        }

        List<Story> stories = storyService.getUserStories(user.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", stories.size());
        response.put("stories", stories);

        return ResponseEntity.ok(response);
    }

    /**
     * Get featured stories
     * GET /api/stories/featured
     */
    @GetMapping("/featured")
    public ResponseEntity<Map<String, Object>> getFeaturedStories() {
        List<Story> stories = storyService.getFeaturedStories();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", stories.size());
        response.put("stories", stories);

        return ResponseEntity.ok(response);
    }

    /**
     * Get trending stories
     * GET /api/stories/trending?limit=10
     */
    @GetMapping("/trending")
    public ResponseEntity<Map<String, Object>> getTrendingStories(
            @RequestParam(defaultValue = "10") int limit) {

        List<Story> stories = storyService.getTrendingStories(limit);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("count", stories.size());
        response.put("stories", stories);

        return ResponseEntity.ok(response);
    }

    /**
     * Search stories
     * GET /api/stories/search?q=java
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchStories(@RequestParam("q") String query) {
        List<Story> stories = storyService.searchStories(query);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("query", query);
        response.put("count", stories.size());
        response.put("stories", stories);

        return ResponseEntity.ok(response);
    }

    /**
     * Get stories by tag
     * GET /api/stories/tag/java
     */
    @GetMapping("/tag/{tagName}")
    public ResponseEntity<Map<String, Object>> getStoriesByTag(@PathVariable String tagName) {
        List<Story> stories = storyService.getStoriesByTag(tagName);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("tag", tagName);
        response.put("count", stories.size());
        response.put("stories", stories);

        return ResponseEntity.ok(response);
    }

    /**
     * Like a story
     * POST /api/stories/{id}/like
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<Map<String, Object>> likeStory(@PathVariable Long id) {
        try {
            Story story = storyService.likeStory(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Story liked");
            response.put("likeCount", story.getLikeCount());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get user statistics
     * GET /api/stories/stats/user/{userId}
     */
    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable Long userId) {
        StoryService.StoryStatistics stats = storyService.getUserStatistics(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("statistics", stats);

        return ResponseEntity.ok(response);
    }
}