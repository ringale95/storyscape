package edu.neu.csye6200.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "auto_renew", nullable = false)
    private Boolean autoRenew = true;

    @Column(name = "posts_remaining")
    private Integer postsRemaining;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Subscription() {
    }

    public Subscription(User user, Product product, LocalDateTime startDate) {
        this.user = user;
        this.product = product;
        this.startDate = startDate;
        this.status = SubscriptionStatus.ACTIVE;
        this.autoRenew = true;
        
        // Set post limit based on product
        if (product.getPostLimit() != null) {
            this.postsRemaining = product.getPostLimit();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Business Logic Methods
    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE && 
               (this.endDate == null || this.endDate.isAfter(LocalDateTime.now()));
    }

    public boolean hasPostsRemaining() {
        return this.postsRemaining == null || this.postsRemaining > 0;
    }

    public void decrementPostsRemaining() {
        if (this.postsRemaining != null && this.postsRemaining > 0) {
            this.postsRemaining--;
        }
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.autoRenew = false;
        this.endDate = LocalDateTime.now();
    }

    public void suspend() {
        this.status = SubscriptionStatus.SUSPENDED;
    }

    public void renew(LocalDateTime newEndDate) {
        this.status = SubscriptionStatus.ACTIVE;
        this.endDate = newEndDate;
        
        // Reset post limit for subscription-based products
        if (this.product.getPostLimit() != null) {
            this.postsRemaining = this.product.getPostLimit();
        }
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", productId=" + (product != null ? product.getId() : null) +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
