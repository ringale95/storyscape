package edu.neu.csye6200.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.neu.csye6200.dto.ProductActionDTO;
import edu.neu.csye6200.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Process a product action (e.g., feature a story)
     * POST /product/{productName}/action
     * 
     * @param productName The product name (e.g., "FeaturedPost")
     * @param dto         Request body containing userId (required) and storyId
     *                    (optional)
     * @return Success message
     */
    @PostMapping("/{productName}/action")
    public ResponseEntity<String> processProductAction(
            @PathVariable String productName,
            @RequestBody ProductActionDTO dto) {

        // Exceptions are handled by GlobalExceptionHandler
        productService.processProductAction(dto.getUserId(), productName, dto.getStoryId());

        return ResponseEntity.status(HttpStatus.OK).body("Product action processed successfully");
    }
}
