package edu.neu.csye6200.factory;

import edu.neu.csye6200.dto.ProductDTO;
import org.springframework.stereotype.Component;
import edu.neu.csye6200.entity.*;

import java.math.BigDecimal;

/**
 * Factory pattern for creating Product instances
 * Encapsulates the product creation logic
 */
@Component
public class ProductFactory {

    /**
     * Create a Product entity from ProductDTO
     */
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setProductType(productDTO.getProductType());
        product.setPrice(productDTO.getPrice());
        
        // Set default status if not provided
        if (productDTO.getStatus() != null) {
            product.setStatus(productDTO.getStatus());
        }
        
        if (productDTO.getIsActive() != null) {
            product.setIsActive(productDTO.getIsActive());
        }

        // Configure product based on type
        switch (productDTO.getProductType()) {
            case PAY_AS_YOU_GO:
                configurePayAsYouGo(product, productDTO);
                break;
            case SUBSCRIPTION:
                configureSubscription(product, productDTO);
                break;
            case MEMBER_ONLY:
                configureMemberOnly(product, productDTO);
                break;
            default:
                throw new IllegalArgumentException("Unknown product type: " + productDTO.getProductType());
        }

        return product;
    }

    /**
     * Configure Pay-as-you-go product (5$ per post)
     */
    private void configurePayAsYouGo(Product product, ProductDTO productDTO) {
        if (product.getPrice() == null) {
            product.setPrice(new BigDecimal("5.00"));
        }
        // No post limit for pay-as-you-go
        product.setPostLimit(null);
    }

    /**
     * Configure Subscription product (50$ - 20 posts monthly)
     */
    private void configureSubscription(Product product, ProductDTO productDTO) {
        if (product.getPrice() == null) {
            product.setPrice(new BigDecimal("50.00"));
        }
        
        if (productDTO.getPostLimit() != null) {
            product.setPostLimit(productDTO.getPostLimit());
        } else {
            product.setPostLimit(20); // Default 20 posts
        }
    }

    /**
     * Configure Member-only content (10% to writer, 90% to company)
     */
    private void configureMemberOnly(Product product, ProductDTO productDTO) {
        if (productDTO.getWriterRevenuePercentage() != null) {
            product.setWriterRevenuePercentage(productDTO.getWriterRevenuePercentage());
        } else {
            product.setWriterRevenuePercentage(new BigDecimal("10.00"));
        }
        
        if (productDTO.getCompanyRevenuePercentage() != null) {
            product.setCompanyRevenuePercentage(productDTO.getCompanyRevenuePercentage());
        } else {
            product.setCompanyRevenuePercentage(new BigDecimal("90.00"));
        }
        
        // Validate revenue percentages sum to 100
        BigDecimal totalPercentage = product.getWriterRevenuePercentage()
                .add(product.getCompanyRevenuePercentage());
        
        if (totalPercentage.compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException(
                "Writer and company revenue percentages must sum to 100%");
        }
    }

    /**
     * Create a default Pay-as-you-go product
     */
    public Product createDefaultPayAsYouGo() {
        Product product = new Product(
            "Featured Post - Pay as you go",
            "Publish featured posts at $5 per post",
            ProductType.PAY_AS_YOU_GO,
            new BigDecimal("5.00")
        );
        return product;
    }

    /**
     * Create a default Subscription product
     */
    public Product createDefaultSubscription() {
        Product product = new Product(
            "Monthly Subscription",
            "Subscribe for $50/month and get 20 featured posts",
            ProductType.SUBSCRIPTION,
            new BigDecimal("50.00")
        );
        product.setPostLimit(20);
        return product;
    }

    /**
     * Create a default Member-only product
     */
    public Product createDefaultMemberOnly() {
        Product product = new Product(
            "Member Only Content",
            "Exclusive content with 10% revenue to writer, 90% to company",
            ProductType.MEMBER_ONLY,
            new BigDecimal("10.00")
        );
        product.setWriterRevenuePercentage(new BigDecimal("10.00"));
        product.setCompanyRevenuePercentage(new BigDecimal("90.00"));
        return product;
    }
}
