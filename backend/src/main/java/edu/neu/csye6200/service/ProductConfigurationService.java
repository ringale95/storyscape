package edu.neu.csye6200.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6200.dto.ProductConfigurationDTO;
import edu.neu.csye6200.entity.Payment;
import edu.neu.csye6200.entity.ProductConfiguration;
import edu.neu.csye6200.entity.Tier;
import edu.neu.csye6200.repository.PaymentRepository;
import edu.neu.csye6200.repository.ProductConfigurationRepository;

import java.util.List;

@Service
public class ProductConfigurationService {

    @Autowired
    private ProductConfigurationRepository productConfigurationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Create a new product configuration
     */
    public ProductConfiguration createProductConfiguration(ProductConfigurationDTO dto) {
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
                dto.getProductName(),
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

        // Update product name only if provided
        if (dto.getProductName() != null) {
            existingConfig.setProductName(dto.getProductName());
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

