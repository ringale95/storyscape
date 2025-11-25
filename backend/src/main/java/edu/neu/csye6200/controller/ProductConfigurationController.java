package edu.neu.csye6200.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.neu.csye6200.dto.ProductConfigurationDTO;
import edu.neu.csye6200.entity.ProductConfiguration;
import edu.neu.csye6200.service.ProductConfigurationService;

import java.util.List;

@RestController
@RequestMapping("/product-configurations")
public class ProductConfigurationController {

    @Autowired
    private ProductConfigurationService productConfigurationService;

    /**
     * Create a new product configuration
     * POST /product-configurations
     */
    @PostMapping
    public ResponseEntity<ProductConfiguration> createProductConfiguration(@RequestBody ProductConfigurationDTO dto) {
        ProductConfiguration created = productConfigurationService.createProductConfiguration(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get all product configurations
     * GET /product-configurations
     */
    @GetMapping
    public ResponseEntity<List<ProductConfiguration>> getAllProductConfigurations() {
        return ResponseEntity.ok(productConfigurationService.getAllProductConfigurations());
    }

    /**
     * Get product configuration by ID
     * GET /product-configurations/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductConfiguration> getProductConfigurationById(@PathVariable Long id) {
        return ResponseEntity.ok(productConfigurationService.getProductConfigurationById(id));
    }

    /**
     * Update an existing product configuration by ID
     * PATCH /product-configurations/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProductConfiguration> updateProductConfiguration(
            @PathVariable Long id,
            @RequestBody ProductConfigurationDTO dto) {
        ProductConfiguration updated = productConfigurationService.updateProductConfiguration(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a product configuration by ID
     * DELETE /product-configurations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductConfiguration(@PathVariable Long id) {
        productConfigurationService.deleteProductConfiguration(id);
        return ResponseEntity.noContent().build();
    }
}

