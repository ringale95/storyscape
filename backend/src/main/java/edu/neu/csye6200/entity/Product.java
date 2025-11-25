package edu.neu.csye6200.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "post_limit")
    private Integer postLimit;

    @Column(name = "writer_revenue_percentage", precision = 5, scale = 2)
    private BigDecimal writerRevenuePercentage;

    @Column(name = "company_revenue_percentage", precision = 5, scale = 2)
    private BigDecimal companyRevenuePercentage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Optional: Tier as enum (uncomment if you want tier-based products)
    @Enumerated(EnumType.STRING)
    @Column(name = "tier")
    private Tier tier;

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
    public Product() {
    }

    public Product(String name, String description, ProductType productType, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.productType = productType;
        this.price = price;
        this.status = ProductStatus.ACTIVE;
        this.isActive = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPostLimit() {
        return postLimit;
    }

    public void setPostLimit(Integer postLimit) {
        this.postLimit = postLimit;
    }

    public BigDecimal getWriterRevenuePercentage() {
        return writerRevenuePercentage;
    }

    public void setWriterRevenuePercentage(BigDecimal writerRevenuePercentage) {
        this.writerRevenuePercentage = writerRevenuePercentage;
    }

    public BigDecimal getCompanyRevenuePercentage() {
        return companyRevenuePercentage;
    }

    public void setCompanyRevenuePercentage(BigDecimal companyRevenuePercentage) {
        this.companyRevenuePercentage = companyRevenuePercentage;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
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
    public boolean isAvailableForTier(Tier userTier) {
        if (this.tier == null) {
            return true; // Available for all tiers
        }
        return this.tier.equals(userTier);
    }

    public boolean isSubscriptionBased() {
        return this.productType == ProductType.SUBSCRIPTION;
    }

    public boolean isPayAsYouGo() {
        return this.productType == ProductType.PAY_AS_YOU_GO;
    }

    public boolean isMemberOnlyContent() {
        return this.productType == ProductType.MEMBER_ONLY;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productType=" + productType +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
