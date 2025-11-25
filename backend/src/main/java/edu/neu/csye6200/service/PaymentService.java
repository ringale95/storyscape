package edu.neu.csye6200.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6200.dto.PaymentDTO;
import edu.neu.csye6200.entity.Payment;
import edu.neu.csye6200.entity.PayAsYouGo;
import edu.neu.csye6200.entity.Subscription;
import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.factory.PaymentFactory;
import edu.neu.csye6200.repository.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {

    private PaymentFactory paymentFactory = PaymentFactory.getInstance();

    @Autowired
    private PaymentRepository paymentRepository;

    public boolean generateBill(User user, String productName) {
        return true;
    }

    /**
     * Create a new payment (PayAsYouGo or Subscription)
     */
    public Payment createPayment(PaymentDTO paymentDTO) {
        Class<? extends Payment> paymentClass;

        if ("subscription".equalsIgnoreCase(paymentDTO.getPaymentType())) {
            paymentClass = Subscription.class;
        } else if ("payg".equalsIgnoreCase(paymentDTO.getPaymentType())) {
            paymentClass = PayAsYouGo.class;
        } else {
            throw new IllegalArgumentException("Invalid payment type: " + paymentDTO.getPaymentType() +
                    ". Must be 'payg' or 'subscription'");
        }

        // Create payment using factory
        Payment payment = paymentFactory.createPayment(
                paymentClass,
                paymentDTO.getPriceCents(),
                paymentDTO.getDescription(),
                paymentDTO.getSubscriptionCost());

        // Save to database (works with Single Table Inheritance)
        return paymentRepository.save(payment);
    }

    /**
     * Get all payments (both PayAsYouGo and Subscription)
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    /**
     * Get a specific payment by ID
     */
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
    }

    /**
     * Update an existing payment (PATCH - partial update)
     * Only updates fields that are provided (non-null)
     * Note: Cannot change payment type (PayAsYouGo <-> Subscription)
     */
    public Payment updatePayment(Long id, PaymentDTO paymentDTO) {
        // Find existing payment
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        // Update common fields only if provided (non-null)
        if (paymentDTO.getPriceCents() != null) {
            existingPayment.setPriceCents(paymentDTO.getPriceCents());
        }

        if (paymentDTO.getDescription() != null) {
            existingPayment.setDescription(paymentDTO.getDescription());
        }

        // Update subscription-specific fields if it's a Subscription and value is
        // provided
        if (existingPayment instanceof Subscription) {
            Subscription subscription = (Subscription) existingPayment;
            if (paymentDTO.getSubscriptionCost() != null) {
                subscription.setSubscriptionCost(paymentDTO.getSubscriptionCost());
            }
        }

        // Save and return updated payment
        return paymentRepository.save(existingPayment);
    }

    /**
     * Delete a payment by ID
     */
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }
}
