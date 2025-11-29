package edu.neu.csye6200.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import edu.neu.csye6200.entity.Invoice;
import edu.neu.csye6200.entity.Payment;
import edu.neu.csye6200.entity.Product;
import edu.neu.csye6200.entity.PayAsYouGo;
import edu.neu.csye6200.entity.ProductConfiguration;
import edu.neu.csye6200.entity.Subscription;
import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.exception.BillingFailedException;
import edu.neu.csye6200.exception.UpdateWalletFailedException;
import edu.neu.csye6200.exception.InvoiceCreationFailedException;
import edu.neu.csye6200.exception.PaymentFailedException;
import edu.neu.csye6200.exception.ProductConfigurationNotFoundException;
import edu.neu.csye6200.exception.UserNotFoundException;
import edu.neu.csye6200.factory.InvoiceFactory;
import edu.neu.csye6200.observer.InvoiceEventPublisher;
import edu.neu.csye6200.repository.InvoiceRepository;
import edu.neu.csye6200.repository.ProductConfigurationRepository;
import edu.neu.csye6200.repository.UserRepository;

/**
 * BillingService handles payment processing for product actions.
 * Checks subscriptions, processes PayAsYouGo payments, and deducts from wallet.
 */
@Service
public class BillingService {

    public enum BillingAction {
        CREDIT,
        DEDUCT
    }

    @Autowired
    private ProductConfigurationRepository productConfigurationRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceEventPublisher invoiceEventPublisher;

    private InvoiceFactory invoiceFactory = InvoiceFactory.getInstance();

    public void processBilling(User user, Product product) {
        processBilling(user, product, null);
    }

    public void processBilling(User user, Product product, Long storyId) {
        processBilling(user, product, storyId, BillingAction.DEDUCT);
    }

    public void rollbackBilling(User user, Product product) {
        processBilling(user, product, null, BillingAction.CREDIT);
    }

    private void createInvoice(User user, ProductConfiguration config, BillingAction action, Long storyId) {
        try {
            Invoice invoice = invoiceFactory.createInvoice(user, config, action);
            invoiceRepository.save(invoice);
            
            // Notify observers about invoice creation (Observer pattern)
            // This decouples invoice creation from story featuring
            // Pass storyId as additional context for observers
            invoiceEventPublisher.notifyInvoiceCreated(invoice, config.getProduct(), storyId);
        } catch (Exception e) {
            throw new InvoiceCreationFailedException(
                    "Failed to create invoice for user: " + user.getId() + ". " + e.getMessage(), e);
        }
    }

    private void updateWallet(User user, long amountCents, BillingAction action) {
        User refreshedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));

        long currentBalance = refreshedUser.getWalletCents();

        if (action == BillingAction.DEDUCT && currentBalance < amountCents) {
            throw new UpdateWalletFailedException(
                    "Insufficient wallet balance for user id: " + user.getId()
                            + ". Required: " + amountCents
                            + ", available: " + currentBalance,
                    amountCents,
                    currentBalance);
        }

        try {

            long newBalance = action == BillingAction.DEDUCT ? currentBalance - amountCents
                    : currentBalance + amountCents;

            refreshedUser.setWalletCents(newBalance);
            userRepository.save(refreshedUser);
        } catch (Exception e) {
            throw new UpdateWalletFailedException(
                    "Failed to update from wallet for user id: " + user.getId() + ". " + e.getMessage(),
                    amountCents,
                    currentBalance);
        }
    }

    /**
     * Check if a payment is a Subscription type
     */
    private boolean isSubscriptionPayment(Payment payment) {
        return payment instanceof Subscription;
    }

    /**
     * Check if a payment is a PayAsYouGo type
     */
    private boolean isPayAsYouGoPayment(Payment payment) {
        return payment instanceof PayAsYouGo;
    }

    /**
     * Find the appropriate ProductConfiguration for billing.
     * First checks if user has a subscription ProductConfiguration for this
     * product.
     * If not, falls back to PayAsYouGo configuration.
     */
    private ProductConfiguration findProductConfiguration(User user, Product product) {
        // Refresh user with subscriptions and payments eagerly loaded
        User refreshedUser = userRepository.findByIdWithSubscriptions(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));

        // 1. Check if user has a subscription ProductConfiguration for this product
        ProductConfiguration subscriptionConfig = refreshedUser.getSubscriptions().stream()
                .filter(config -> config.getProduct().getName().equals(product.getName())
                        && config.getTier().equals(user.getTier())
                        && config.getPayment() != null
                        && isSubscriptionPayment(config.getPayment()))
                .findFirst()
                .orElse(null);

        if (subscriptionConfig != null) {
            return subscriptionConfig;
        }

        // 2. Fall back to PayAsYouGo configuration (default)
        // Fetch all configurations with payments eagerly loaded, then filter for PayAsYouGo
        List<ProductConfiguration> configs = productConfigurationRepository
                .findByProductAndTierWithPayment(product, user.getTier());
        
        ProductConfiguration payAsYouGoConfig = configs.stream()
                .filter(config -> config.getPayment() != null 
                        && isPayAsYouGoPayment(config.getPayment()))
                .findFirst()
                .orElseThrow(() -> new ProductConfigurationNotFoundException(
                        "PayAsYouGo product configuration not found for product: "
                                + product.getName() + " and tier: " + user.getTier()));

        return payAsYouGoConfig;
    }

    private void processBilling(User user, Product product, Long storyId, BillingAction action) {
        try {
            // 1. Find appropriate product configuration (subscription if available,
            // otherwise PayAsYouGo)
            ProductConfiguration config = findProductConfiguration(user, product);

            // 2. Get payment details
            Payment payment = config.getPayment();
            if (payment == null) {
                throw new PaymentFailedException("Payment configuration not found for product: " + product.getName());
            }

            long amountToDeduct = payment.getPriceCents();

            // 3. Deduct from wallet
            updateWallet(user, amountToDeduct, action);

            // 4. Create invoice (pass storyId for observer context)
            createInvoice(user, config, action, storyId);
        } catch (ProductConfigurationNotFoundException | PaymentFailedException | UpdateWalletFailedException
                | InvoiceCreationFailedException e) {
            // Re-throw specific exceptions
            throw e;
        } catch (Exception e) {
            // Wrap any other exceptions in BillingFailedException
            throw new BillingFailedException("Billing failed for product: " + product.getName() + ". " + e.getMessage(), e);
        }
    }
}