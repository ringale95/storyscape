package edu.neu.csye6200.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6200.dto.ProductConfigurationDTO;
import edu.neu.csye6200.entity.Payment;
import edu.neu.csye6200.entity.Product;
import edu.neu.csye6200.entity.ProductConfiguration;
import edu.neu.csye6200.entity.Tier;
import edu.neu.csye6200.repository.PaymentRepository;
import edu.neu.csye6200.repository.ProductConfigurationRepository;
import edu.neu.csye6200.repository.ProductRepository;

import java.util.List;

@Service
public class ProductConfigurationService {

    @Autowired
    private ProductConfigurationRepository productConfigurationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create a new product configuration
     */
    public ProductConfiguration createProductConfiguration(ProductConfigurationDTO dto) {
        // Validate product exists
        if (dto.getProductId() == null) {
            throw new IllegalArgumentException("Product ID is required");
        }
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));

        // Validate payment exists
        Payment payment = paymentRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + dto.getPaymentId()));

        // Parse tier
        Tier tier;
        try {
            tier = Tier.valueOf(dto.getTier().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid tier: " + dto.getTier() + ". Must be NORMAL, CORE, or PRO");
        }

        // Create and save
        ProductConfiguration productConfiguration = new ProductConfiguration(
                product,
                tier,
                payment);

        return productConfigurationRepository.save(productConfiguration);
    }

    /**
     * Get all product configurations
     */
    public List<ProductConfiguration> getAllProductConfigurations() {
        return productConfigurationRepository.findAll();
    }

    /**
     * Get a specific product configuration by ID
     */
    public ProductConfiguration getProductConfigurationById(Long id) {
        return productConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductConfiguration not found with id: " + id));
    }

    /**
     * Update an existing product configuration (PATCH - partial update)
     * Only updates fields that are provided (non-null)
     */
    public ProductConfiguration updateProductConfiguration(Long id, ProductConfigurationDTO dto) {
        ProductConfiguration existingConfig = productConfigurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductConfiguration not found with id: " + id));

        // Update product only if provided
        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));
            existingConfig.setProduct(product);
        }

        // Update tier only if provided
        if (dto.getTier() != null) {
            try {
                Tier tier = Tier.valueOf(dto.getTier().toUpperCase());
                existingConfig.setTier(tier);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid tier: " + dto.getTier() + ". Must be NORMAL, CORE, or PRO");
            }
        }

        // Update payment only if provided
        if (dto.getPaymentId() != null) {
            Payment payment = paymentRepository.findById(dto.getPaymentId())
                    .orElseThrow(() -> new RuntimeException("Payment not found with id: " + dto.getPaymentId()));
            existingConfig.setPayment(payment);
        }

        return productConfigurationRepository.save(existingConfig);
    }

    /**
     * Delete a product configuration by ID
     */
    public void deleteProductConfiguration(Long id) {
        if (!productConfigurationRepository.existsById(id)) {
            throw new RuntimeException("ProductConfiguration not found with id: " + id);
        }
        productConfigurationRepository.deleteById(id);
    }
}

