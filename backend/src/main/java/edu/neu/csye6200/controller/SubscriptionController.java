package edu.neu.csye6200.controller;

import edu.neu.csye6200.dto.CreateSubscriptionDTO;
import edu.neu.csye6200.dto.SubscriptionDTO;
import edu.neu.csye6200.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Subscription Management
 * Handles all subscription-related operations
 */
@RestController
@RequestMapping("/api")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * POST /api/subscription - Create a new subscription
     * Available to authenticated users
     */
    @PostMapping("/subscription")
    public ResponseEntity<?> createSubscription(@RequestBody CreateSubscriptionDTO createDTO) {
        try {
            SubscriptionDTO subscription = subscriptionService.createSubscription(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating the subscription");
        }
    }

    /**
     * GET /api/subscriptions/user/{userId} - Get all subscriptions for a user
     * Available to authenticated users (should validate userId matches authenticated user)
     */
    @GetMapping("/subscriptions/user/{userId}")
    public ResponseEntity<?> getSubscriptionsByUserId(@PathVariable Long userId) {
        try {
            List<SubscriptionDTO> subscriptions = subscriptionService.getSubscriptionsByUserId(userId);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving subscriptions");
        }
    }

    /**
     * GET /api/subscriptions/user/{userId}/active - Get active subscriptions for a user
     * Available to authenticated users
     */
    @GetMapping("/subscriptions/user/{userId}/active")
    public ResponseEntity<?> getActiveSubscriptionsByUserId(@PathVariable Long userId) {
        try {
            List<SubscriptionDTO> subscriptions = subscriptionService.getActiveSubscriptionsByUserId(userId);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving active subscriptions");
        }
    }

    /**
     * DELETE /api/subscription/{id} - Cancel a subscription
     * Requires userId parameter to verify ownership
     */
    @DeleteMapping("/subscription/{id}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Long id, 
                                                @RequestParam Long userId) {
        try {
            subscriptionService.cancelSubscription(id, userId);
            return ResponseEntity.ok("Subscription cancelled successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while cancelling the subscription");
        }
    }

    /**
     * POST /api/subscription/{id}/renew - Renew a subscription
     * Available to subscription owner
     */
    @PostMapping("/subscription/{id}/renew")
    public ResponseEntity<?> renewSubscription(@PathVariable Long id) {
        try {
            SubscriptionDTO renewedSubscription = subscriptionService.renewSubscription(id);
            return ResponseEntity.ok(renewedSubscription);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while renewing the subscription");
        }
    }

    /**
     * POST /api/subscription/{id}/use-post - Use a post from subscription
     * Available to subscription owner
     */
    @PostMapping("/subscription/{id}/use-post")
    public ResponseEntity<?> useSubscriptionPost(@PathVariable Long id) {
        try {
            subscriptionService.useSubscriptionPost(id);
            return ResponseEntity.ok("Post used successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while using the post");
        }
    }
}
