package edu.neu.csye6200.service;

import edu.neu.csye6200.dto.ProductDTO;
import edu.neu.csye6200.entity.Tier;
import edu.neu.csye6200.factory.ProductFactory;
import edu.neu.csye6200.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductFactory productFactory;

    /**
     * Get all active products
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findByIsActiveTrueAndStatus(ProductStatus.ACTIVE);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get product by ID
     */
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    /**
     * Get products available for a specific tier
     */
    public List<ProductDTO> getProductsForTier(Tier tier) {
        List<Product> products = productRepository.findByIsActiveTrueAndStatus(ProductStatus.ACTIVE);
        return products.stream()
                .filter(p -> p.getTier() == null || p.getTier().equals(tier))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new product
     */
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        validateProductDTO(productDTO);
        
        Product product = productFactory.createProduct(productDTO);
        Product savedProduct = productRepository.save(product);
        
        return convertToDTO(savedProduct);
    }

    /**
     * Update an existing product
     */
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

        validateProductDTO(productDTO);
        updateProductFromDTO(existingProduct, productDTO);
        
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    /**
     * Delete a product (soft delete)
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        
        product.setIsActive(false);
        product.setStatus(ProductStatus.DISCONTINUED);
        productRepository.save(product);
    }

    /**
     * Activate a product
     */
    @Transactional
    public ProductDTO activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        
        product.setIsActive(true);
        product.setStatus(ProductStatus.ACTIVE);
        Product updatedProduct = productRepository.save(product);
        
        return convertToDTO(updatedProduct);
    }

    /**
     * Deactivate a product
     */
    @Transactional
    public ProductDTO deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        
        product.setIsActive(false);
        product.setStatus(ProductStatus.INACTIVE);
        Product updatedProduct = productRepository.save(product);
        
        return convertToDTO(updatedProduct);
    }

    // Helper Methods
    private void validateProductDTO(ProductDTO productDTO) {
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (productDTO.getProductType() == null) {
            throw new IllegalArgumentException("Product type is required");
        }
        if (productDTO.getPrice() == null || productDTO.getPrice().signum() < 0) {
            throw new IllegalArgumentException("Valid product price is required");
        }
        
        // Validate revenue percentages for member-only content
        if (productDTO.getProductType().name().equals("MEMBER_ONLY")) {
            if (productDTO.getWriterRevenuePercentage() == null || 
                productDTO.getCompanyRevenuePercentage() == null) {
                throw new IllegalArgumentException("Revenue percentages are required for member-only content");
            }
        }
    }

    private void updateProductFromDTO(Product product, ProductDTO productDTO) {
        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }
        if (productDTO.getPostLimit() != null) {
            product.setPostLimit(productDTO.getPostLimit());
        }
        if (productDTO.getWriterRevenuePercentage() != null) {
            product.setWriterRevenuePercentage(productDTO.getWriterRevenuePercentage());
        }
        if (productDTO.getCompanyRevenuePercentage() != null) {
            product.setCompanyRevenuePercentage(productDTO.getCompanyRevenuePercentage());
        }
        if (productDTO.getStatus() != null) {
            product.setStatus(productDTO.getStatus());
        }
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setProductType(product.getProductType());
        dto.setPrice(product.getPrice());
        dto.setPostLimit(product.getPostLimit());
        dto.setWriterRevenuePercentage(product.getWriterRevenuePercentage());
        dto.setCompanyRevenuePercentage(product.getCompanyRevenuePercentage());
        dto.setStatus(product.getStatus());
        dto.setIsActive(product.getIsActive());
        
        // Handle Tier enum - store tier name if present
        if (product.getTier() != null) {
            // Since ProductDTO.tierId is Long, we'll skip it
            // Or you can change ProductDTO to have String tierName instead
            dto.setTierId(null); 
        }
        
        return dto;
    }
}
