package edu.neu.csye6200.controller;

import edu.neu.csye6200.dto.ProductAccessDTO;
import edu.neu.csye6200.dto.ProductActionDTO;
import edu.neu.csye6200.dto.ProductDTO;
import edu.neu.csye6200.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Product Catalog Management
 * Handles all product-related operations
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * GET /api/products - Get all active products
     * Available to all authenticated users
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            List<ProductDTO> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/products/{id} - Get product by ID
     * Available to all authenticated users
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the product");
        }
    }

    /**
     * POST /api/product - Create a new product
     * Admin only
     */
    @PostMapping()
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the product");
        }
    }

    /**
     * PUT /api/product/{id} - Update an existing product
     * Admin only
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
            @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the product");
        }
    }

    @PostMapping("/{id}/action")
    public ResponseEntity<String> processProductAction(
            @PathVariable Long id,
            @RequestBody ProductActionDTO dto) {

        // Exceptions are handled by GlobalExceptionHandler
        productService.processProductAction(dto.getUserId(), id, dto.getStoryId());

        return ResponseEntity.status(HttpStatus.OK).body("Product action processed successfully");
    }

    @GetMapping("/{id}/access")
    public ResponseEntity<Map<String, Object>> getProductAccess(@PathVariable Long id, @RequestBody ProductAccessDTO dto) {
        try {
            Map<String, Object> access = productService.getProductAccess(id, dto);
            return ResponseEntity.ok(access);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
