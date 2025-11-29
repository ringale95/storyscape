package edu.neu.csye6200.dto;

/**
 * DTO for Product Action requests
 * Contains metadata needed to process product actions like featuring a story
 */
public class ProductAccessDTO {

    private Long userId;
    private Long productId;

    // Constructors
    public ProductAccessDTO() {
    }

    public ProductAccessDTO(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
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
}
