package edu.neu.csye6200.dto;

public class ProductConfigurationDTO {
    private Long productId; // Product ID instead of productName
    private String tier; // "NORMAL", "CORE", or "PRO"
    private Long paymentId;

    public ProductConfigurationDTO() {
    }

    public ProductConfigurationDTO(Long productId, String tier, Long paymentId) {
        this.productId = productId;
        this.tier = tier;
        this.paymentId = paymentId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
}

