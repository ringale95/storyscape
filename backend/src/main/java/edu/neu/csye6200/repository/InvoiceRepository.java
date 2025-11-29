package edu.neu.csye6200.repository;

import edu.neu.csye6200.entity.Invoice;
import edu.neu.csye6200.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

        /**
         * Find all invoices for a specific user
         */
        List<Invoice> findByUser(User user);

        /**
         * Find all invoices for a specific user (payment data is stored directly, no join needed)
         */
        @Query("SELECT i FROM Invoice i WHERE i.user = :user ORDER BY i.createdAt DESC")
        List<Invoice> findByUserWithPayment(@Param("user") User user);

        /**
         * Find invoice by ID with user eagerly fetched (payment data is stored directly)
         */
        @Query("SELECT i FROM Invoice i JOIN FETCH i.user WHERE i.id = :invoiceId")
        java.util.Optional<Invoice> findByIdWithRelationships(@Param("invoiceId") Long invoiceId);

        /**
         * Find active subscription invoices for a user (dateTo >= today)
         * Uses stored payment_type column instead of joining with payments table
         */
        @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId AND i.paymentType = 'SUBSCRIPTION' AND i.dateTo >= :today")
        List<Invoice> findActiveSubscriptions(@Param("userId") Long userId, @Param("today") LocalDate today);

        /**
         * Check if user has an active subscription for a specific payment
         * Uses stored payment_type and payment_id columns instead of joining with payments table
         */
        @Query("SELECT i FROM Invoice i WHERE i.user.id = :userId AND i.paymentId = :paymentId AND i.paymentType = 'SUBSCRIPTION' AND i.dateTo >= :today")
        Optional<Invoice> findActiveSubscriptionForPayment(@Param("userId") Long userId,
                        @Param("paymentId") Long paymentId,
                        @Param("today") LocalDate today);
}
