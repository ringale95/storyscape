package edu.neu.csye6200.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Payment Entity using Single Table Inheritance.
 * All payment types (PayAsYouGo, Subscription) are stored in one table.
 */
@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_cents", nullable = false)
    private long priceCents;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor for JPA
    public Payment() {
    }

    public Payment(long priceCents, String description) {
        this.priceCents = priceCents;
        this.description = description;
    }

    // JPA Lifecycle Hooks
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPriceCents() {
        return priceCents;
    }

    public void setPriceCents(long priceCents) {
        this.priceCents = priceCents;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

