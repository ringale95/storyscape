package edu.neu.csye6200.entity;

import jakarta.persistence.*;

/**
 * Subscription payment type.
 * Stored in the payments table with discriminator value 'SUBSCRIPTION'.
 */
@Entity
@DiscriminatorValue("SUBSCRIPTION")
public class Subscription extends Payment {

    @Column(name = "subscription_cost")
    private Long subscriptionCost;

    // Default constructor for JPA
    public Subscription() {
        super();
    }

    public Subscription(Long subscriptionCost, String description, long priceCents) {
        super(priceCents, description);
        this.subscriptionCost = subscriptionCost;
    }

    // Getters and Setters
    public Long getSubscriptionCost() {
        return subscriptionCost;
    }

    public void setSubscriptionCost(Long subscriptionCost) {
        this.subscriptionCost = subscriptionCost;
    }
}
