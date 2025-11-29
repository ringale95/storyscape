package edu.neu.csye6200.dto;

/**
 * DTO for Product Action requests
 * Contains metadata needed to process product actions like featuring a story
 */
public class ProductActionDTO {

    private Long userId;
    private Long storyId; // Optional: story ID for actions like featuring a story

    // Constructors
    public ProductActionDTO() {
    }

    public ProductActionDTO(Long userId, Long storyId) {
        this.userId = userId;
        this.storyId = storyId;
    }

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }
}
