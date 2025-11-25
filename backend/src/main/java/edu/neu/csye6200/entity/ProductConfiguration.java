package edu.neu.csye6200.entity;

import jakarta.persistence.*;

@Entity
public class ProductConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Tier tier = Tier.NORMAL;

    // Direct relationship to Payment (now possible with Single Table Inheritance)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    // Default constructor for JPA
    public ProductConfiguration() {
    }

    public ProductConfiguration(String productName, Tier tier, Payment payment) {
        this.productName = productName;
        this.tier = tier;
        this.payment = payment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}

