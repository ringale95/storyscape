package edu.neu.csye6200.controller;

import edu.neu.csye6200.dto.ProductDTO;
import edu.neu.csye6200.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product Catalog Management
 * Handles all product-related operations
 */
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * GET /api/products - Get all active products
     * Available to all authenticated users
     */
    @GetMapping("/products")
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
    @GetMapping("/products/{id}")
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
    @PostMapping("/product")
//    @PreAuthorize("hasRole('ADMIN')")
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
    @PutMapping("/product/{id}")
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

    /**
     * DELETE /api/product/{id} - Delete a product (soft delete)
     * Admin only
     */
    @DeleteMapping("/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the product");
        }
    }

    /**
     * PUT /api/product/{id}/activate - Activate a product
     * Admin only
     */
    @PutMapping("/product/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> activateProduct(@PathVariable Long id) {
        try {
            ProductDTO activatedProduct = productService.activateProduct(id);
            return ResponseEntity.ok(activatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while activating the product");
        }
    }

    /**
     * PUT /api/product/{id}/deactivate - Deactivate a product
     * Admin only
     */
    @PutMapping("/product/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateProduct(@PathVariable Long id) {
        try {
            ProductDTO deactivatedProduct = productService.deactivateProduct(id);
            return ResponseEntity.ok(deactivatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deactivating the product");
        }
    }
}
