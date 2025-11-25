package edu.neu.csye6200.dto;

public class ProductConfigurationDTO {
    private String productName;
    private String tier; // "NORMAL", "CORE", or "PRO"
    private Long paymentId;

    public ProductConfigurationDTO() {
    }

    public ProductConfigurationDTO(String productName, String tier, Long paymentId) {
        this.productName = productName;
        this.tier = tier;
        this.paymentId = paymentId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

