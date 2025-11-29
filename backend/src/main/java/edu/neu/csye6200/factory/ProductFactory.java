package edu.neu.csye6200.factory;

import edu.neu.csye6200.dto.ProductDTO;
import org.springframework.stereotype.Component;
import edu.neu.csye6200.entity.Product;

/**
 * Factory pattern for creating Product instances
 * Encapsulates the product creation logic
 * 
 * Note: Product entity has been simplified to only contain name and
 * description.
 * Product-specific configurations (pricing, tiers, payment types) are handled
 * by ProductConfiguration entities.
 */
@Component
public class ProductFactory {

    /**
     * Create a Product entity from ProductDTO
     * Only sets name and description as Product entity has been simplified
     */
    public Product createProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("ProductDTO cannot be null");
        }

        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());

        return product;
    }

    /**
     * Create a default Featured Post product
     */
    public Product createDefaultFeaturedPost() {
        return new Product(
                "Featured Post",
                "Feature your story to get more visibility");
    }

    /**
     * Create a default Subscription product
     */
    public Product createDefaultSubscription() {
        return new Product(
                "Monthly Subscription",
                "Subscribe for monthly featured posts");
    }

    /**
     * Create a default Member-only product
     */
    public Product createDefaultMemberOnly() {
        return new Product(
                "Member Only Content",
                "Exclusive content for members only");
    }
}
