package edu.neu.csye6200.dto;

import java.util.List;

/**
 * DTO for Story operations
 */
public class StoryDTO {

    private String title;
    private String body;
    private String contentType;
    private String visibility; // PUBLIC, SUBSCRIBER_ONLY, PREMIUM_ONLY
    private List<String> tags;

    // Constructors
    public StoryDTO() {
    }

    public StoryDTO(String title, String body) {
        this.title = title;
        this.body = body;
    }

    // Getters and Setters

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

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}