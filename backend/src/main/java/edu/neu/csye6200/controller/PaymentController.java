package edu.neu.csye6200.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.neu.csye6200.dto.PaymentDTO;
import edu.neu.csye6200.entity.Payment;
import edu.neu.csye6200.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Create a new payment (PayAsYouGo or Subscription)
     * POST /payments
     */
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDTO paymentDTO) {
        Payment createdPayment = paymentService.createPayment(paymentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }

    /**
     * Get all payments
     * GET /payments
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    /**
     * Get payment by ID
     * GET /payments/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    /**
     * Update an existing payment (partial update)
     * PATCH /payments/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable Long id,
            @RequestBody PaymentDTO paymentDTO) {
        Payment updatedPayment = paymentService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    /**
     * Delete a payment by ID
     * DELETE /payments/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}