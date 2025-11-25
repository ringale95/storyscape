package edu.neu.csye6200.dto;

import java.math.BigDecimal;

public class ProductDTO {
    
    private Long id;
    private String name;
    private String description;
    private ProductType productType;
    private BigDecimal price;
    private Integer postLimit;
    private BigDecimal writerRevenuePercentage;
    private BigDecimal companyRevenuePercentage;
    private ProductStatus status;
    private Boolean isActive;
    private Long tierId;

    // Constructors
    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, ProductType productType, 
                      BigDecimal price, ProductStatus status, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.productType = productType;
        this.price = price;
        this.status = status;
        this.isActive = isActive;
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

    public Long getTierId() {
        return tierId;
    }

    public void setTierId(Long tierId) {
        this.tierId = tierId;
    }
}
