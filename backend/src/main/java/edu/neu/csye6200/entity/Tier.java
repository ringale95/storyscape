package edu.neu.csye6200.entity;

public enum Tier {
    NORMAL,
    SILVER,
    GOLD,
    CORE,
    OTHER;

    /**
     * Check if this tier is premium (GOLD or CORE)
     */
    public boolean isPremium() {
        return this == CORE || this == GOLD || this == SILVER;
    }
}