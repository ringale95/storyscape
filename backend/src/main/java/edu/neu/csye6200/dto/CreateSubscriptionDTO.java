package edu.neu.csye6200.dto;

import java.time.LocalDateTime;

public class CreateSubscriptionDTO {
    
    private Long userId;
    private Long productId;
    private LocalDateTime startDate;
    private Boolean autoRenew;

    // Constructors
    public CreateSubscriptionDTO() {
    }

    public CreateSubscriptionDTO(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
        this.autoRenew = true;
    }

    // Getters and Setters
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Boolean getAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(Boolean autoRenew) {
        this.autoRenew = autoRenew;
    }
}
