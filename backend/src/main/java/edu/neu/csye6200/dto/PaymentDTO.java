package edu.neu.csye6200.dto;

/**
 * Data Transfer Object for Payment creation and update requests.
 *
 * This DTO handles both PayAsYouGo and Subscription payment types.
 * - For PayAsYouGo: set paymentType="payg", subscriptionCost=null or 0
 * - For Subscription: set paymentType="subscription", include subscriptionCost
 *
 * For PATCH requests, only include fields you want to update.
 * Using Long (wrapper) instead of long (primitive) allows null to indicate "not
 * provided".
 */
public class PaymentDTO {

    private String paymentType; // "payg" or "subscription"
    private Long priceCents; // Using Long to support null in PATCH requests
    private String description;
    private Long subscriptionCost; // Only used for Subscription type

    // Default constructor for Jackson
    public PaymentDTO() {
    }

    public PaymentDTO(String paymentType, Long priceCents, String description, Long subscriptionCost) {
        this.paymentType = paymentType;
        this.priceCents = priceCents;
        this.description = description;
        this.subscriptionCost = subscriptionCost;
    }

    // Getters and Setters
    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(Long priceCents) {
        this.priceCents = priceCents;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSubscriptionCost() {
        return subscriptionCost;
    }

    public void setSubscriptionCost(Long subscriptionCost) {
        this.subscriptionCost = subscriptionCost;
    }
}

