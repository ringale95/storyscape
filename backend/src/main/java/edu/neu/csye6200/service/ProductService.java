package edu.neu.csye6200.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.exception.BillingFailedException;
import edu.neu.csye6200.exception.UserNotFoundException;
import edu.neu.csye6200.repository.UserRepository;

@Service
public class ProductService {

    @Autowired
    private BillingService billingService;

    @Autowired
    private UserRepository userRepository;

    
    public void processProductAction(Long userId, String productName, Long storyId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        try {
            // Process billing (pass storyId for context)
            billingService.processBilling(user, productName, storyId);
        } catch (BillingFailedException e) {
            billingService.rollbackBilling(user, productName);
            throw e;
        }

        try {
            // Perform the actual product action (e.g., feature the story)
            this.performProductAction(user, productName);
        } catch (Exception e) {
            billingService.rollbackBilling(user, productName);
            throw new RuntimeException("Product action failed: " + e.getMessage(), e);
        }
    }

    private void performProductAction(User user, String productName) {
    }
}
