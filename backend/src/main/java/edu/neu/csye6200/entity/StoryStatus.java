package edu.neu.csye6200.entity;

/**
 * Story status enumeration
 */
public enum StoryStatus {
    DRAFT("Draft", "Story is being edited"),
    UNDER_REVIEW("Under Review", "Story is awaiting approval"),
    PUBLISHED("Published", "Story is live on platform"),
    ARCHIVED("Archived", "Story is no longer active");

    private final String displayName;
    private final String description;

    StoryStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublic() {
        return this == PUBLISHED;
    }
}