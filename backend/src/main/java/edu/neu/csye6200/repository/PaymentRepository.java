package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Payment entities.
 * Works with Single Table Inheritance - can query PayAsYouGo and Subscription.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
