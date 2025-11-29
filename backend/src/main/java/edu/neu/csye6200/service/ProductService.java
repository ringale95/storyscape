package edu.neu.csye6200.service;

import edu.neu.csye6200.dto.ProductAccessDTO;
import edu.neu.csye6200.dto.ProductDTO;
import edu.neu.csye6200.entity.PayAsYouGo;
import edu.neu.csye6200.entity.Product;
import edu.neu.csye6200.entity.ProductConfiguration;
import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.exception.BillingFailedException;
import edu.neu.csye6200.exception.ProductAccessDeniedException;
import edu.neu.csye6200.exception.ProductConfigurationNotFoundException;
import edu.neu.csye6200.exception.UserNotFoundException;
import edu.neu.csye6200.factory.ProductFactory;
import edu.neu.csye6200.repository.ProductConfigurationRepository;
import edu.neu.csye6200.repository.ProductRepository;
import edu.neu.csye6200.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductConfigurationRepository productConfigurationRepository;

    @Autowired
    private ProductFactory productFactory;

    @Autowired
    private BillingService billingService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all products
     */
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get product by ID
     */
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
        return convertToDTO(product);
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

    @Transactional
    public void processProductAction(Long userId, Long productId, Long storyId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID is required");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        try {
            // Process billing (pass storyId for context)
            billingService.processBilling(user, product, storyId);
        } catch (BillingFailedException e) {
            billingService.rollbackBilling(user, product);
            throw e;
        }

        try {
            this.performProductAction(user, product);
        } catch (Exception e) {
            billingService.rollbackBilling(user, product);
            throw new RuntimeException("Product action failed: " + e.getMessage(), e);
        }
    }

    /**
     * Perform the actual product action based on product
     * This method can be extended to handle different product types
     */
    private void performProductAction(User user, Product product) {

    }

    // Helper Methods
    private void validateProductDTO(ProductDTO productDTO) {
        if (productDTO.getName() == null || productDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
    }

    private void updateProductFromDTO(Product product, ProductDTO productDTO) {
        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());

        return dto;
    }

    public Map<String, Object> getProductAccess(Long id, ProductAccessDTO dto) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));

            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + dto.getUserId()));

            return getAccess(user, product, dto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product access: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> getAccess(User user, Product product, ProductAccessDTO dto) {
        try {
            checkAccess(user, product, dto);
            return performProductAccessAction(user, product);
        } catch (Exception e) {
            throw e;
        }
    }

    private Map<String, Object> performProductAccessAction(User user, Product product) {
        return Map.of("result", "success");
    }

    private void checkAccess(User user, Product product, ProductAccessDTO dto) {
        ProductConfiguration productConfiguration = billingService.findProductConfiguration(user, product);
        if (productConfiguration == null || productConfiguration.getPayment() instanceof PayAsYouGo)
            throw new ProductAccessDeniedException(
                    "Product access denied for user: " + user.getId() + " and product: " + product.getName());
    }
}
