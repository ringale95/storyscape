package edu.neu.csye6200.entity;

/**
 * Story visibility levels
 */
public enum StoryVisibility {
    PUBLIC("Public", "Accessible to all users"),
    SUBSCRIBER_ONLY("Subscriber Only", "Premium users only"),
    PREMIUM_ONLY("Premium Only", "Core tier users only");

    private final String displayName;
    private final String description;

    StoryVisibility(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}