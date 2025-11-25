package edu.neu.csye6200.service;

import edu.neu.csye6200.dto.CreateSubscriptionDTO;
import edu.neu.csye6200.dto.SubscriptionDTO;
import edu.neu.csye6200.entity.*;
import edu.neu.csye6200.repository.ProductRepository;
import edu.neu.csye6200.repository.SubscriptionRepository;
import edu.neu.csye6200.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Optional: Remove this if you don't want invoice integration yet
    // @Autowired
    // private InvoiceService invoiceService;

    /**
     * Create a new subscription
     */
    @Transactional
    public SubscriptionDTO createSubscription(CreateSubscriptionDTO createDTO) {
        // Validate user
        User user = userRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + createDTO.getUserId()));

        // Validate product
        Product product = productRepository.findByIdAndIsActiveTrue(createDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found or inactive with id: " + createDTO.getProductId()));

        // Check if product is available for user's tier
        if (user.getTier() != null && !product.isAvailableForTier(user.getTier())) {
            throw new IllegalStateException("This product is not available for your tier");
        }

        // Check for existing active subscription for the same product
        Optional<Subscription> existingSubscription = subscriptionRepository
                .findActiveSubscriptionByUserAndProduct(user.getId(), product.getId());
        
        if (existingSubscription.isPresent()) {
            throw new IllegalStateException("User already has an active subscription for this product");
        }

        // Create subscription
        LocalDateTime startDate = createDTO.getStartDate() != null ? 
                createDTO.getStartDate() : LocalDateTime.now();
        
        Subscription subscription = new Subscription(user, product, startDate);
        
        if (createDTO.getAutoRenew() != null) {
            subscription.setAutoRenew(createDTO.getAutoRenew());
        }

        // Set end date based on product type
        if (product.isSubscriptionBased()) {
            subscription.setEndDate(startDate.plusMonths(1)); // Monthly subscription
        }

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        // Generate invoice (OPTIONAL - uncomment when Invoice entity is ready)
        // invoiceService.createInvoiceForSubscription(savedSubscription);

        return convertToDTO(savedSubscription);
    }

    /**
     * Get all subscriptions for a user
     */
    public List<SubscriptionDTO> getSubscriptionsByUserId(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        return subscriptions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active subscriptions for a user
     */
    public List<SubscriptionDTO> getActiveSubscriptionsByUserId(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
        
        return subscriptions.stream()
                .filter(Subscription::isActive)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cancel a subscription
     */
    @Transactional
    public void cancelSubscription(Long subscriptionId, Long userId) {
        Subscription subscription = subscriptionRepository
                .findByIdAndUserId(subscriptionId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Subscription not found or does not belong to user"));

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Subscription is already cancelled");
        }

        subscription.cancel();
        subscriptionRepository.save(subscription);
    }

    /**
     * Renew a subscription
     */
    @Transactional
    public SubscriptionDTO renewSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot renew a cancelled subscription");
        }

        LocalDateTime newEndDate = LocalDateTime.now().plusMonths(1);
        subscription.renew(newEndDate);
        
        Subscription renewedSubscription = subscriptionRepository.save(subscription);

        // Generate invoice for renewal (OPTIONAL - uncomment when Invoice entity is ready)
        // invoiceService.createInvoiceForSubscription(renewedSubscription);

        return convertToDTO(renewedSubscription);
    }

    /**
     * Use a post from subscription
     */
    @Transactional
    public void useSubscriptionPost(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        if (!subscription.isActive()) {
            throw new IllegalStateException("Subscription is not active");
        }

        if (!subscription.hasPostsRemaining()) {
            throw new IllegalStateException("No posts remaining in subscription");
        }

        subscription.decrementPostsRemaining();
        subscriptionRepository.save(subscription);
    }

    /**
     * Process expired subscriptions
     */
    @Transactional
    public void processExpiredSubscriptions() {
        List<Subscription> expiredSubscriptions = subscriptionRepository
                .findExpiredSubscriptions(LocalDateTime.now());

        for (Subscription subscription : expiredSubscriptions) {
            if (subscription.getAutoRenew()) {
                try {
                    renewSubscription(subscription.getId());
                } catch (Exception e) {
                    // Log error and mark subscription as expired
                    subscription.setStatus(SubscriptionStatus.EXPIRED);
                    subscriptionRepository.save(subscription);
                }
            } else {
                subscription.setStatus(SubscriptionStatus.EXPIRED);
                subscriptionRepository.save(subscription);
            }
        }
    }

    // Helper Methods
    private SubscriptionDTO convertToDTO(Subscription subscription) {
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());
        dto.setUserId(subscription.getUser().getId());
        dto.setProductId(subscription.getProduct().getId());
        dto.setProductName(subscription.getProduct().getName());
        dto.setStatus(subscription.getStatus());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setAutoRenew(subscription.getAutoRenew());
        dto.setPostsRemaining(subscription.getPostsRemaining());
        
        return dto;
    }
}
