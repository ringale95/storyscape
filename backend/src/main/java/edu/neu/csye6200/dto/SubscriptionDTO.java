package edu.neu.csye6200.dto;

import edu.neu.csye6200.entity.SubscriptionStatus;

import java.time.LocalDateTime;

public class SubscriptionDTO {
    
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private SubscriptionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean autoRenew;
    private Integer postsRemaining;

    // Constructors
    public SubscriptionDTO() {
    }

    public SubscriptionDTO(Long id, Long userId, Long productId, String productName, 
                           SubscriptionStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public Integer getPostsRemaining() {
        return postsRemaining;
    }

    public void setPostsRemaining(Integer postsRemaining) {
        this.postsRemaining = postsRemaining;
    }
}
