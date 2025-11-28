package edu.neu.csye6200.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST controllers.
 * Handles custom exceptions and returns appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductConfigurationNotFoundException.class)
    public ResponseEntity<String> handleProductConfigurationNotFound(
            ProductConfigurationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<String> handlePaymentFailed(PaymentFailedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UpdateWalletFailedException.class)
    public ResponseEntity<String> handleDeductionFailed(UpdateWalletFailedException ex) {
        String message = ex.getMessage();
        if (ex.getRequiredAmount() > 0 || ex.getAvailableAmount() > 0) {
            message += " Required: " + ex.getRequiredAmount()
                    + ", Available: " + ex.getAvailableAmount();
        }
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(message);
    }

    @ExceptionHandler(BillingFailedException.class)
    public ResponseEntity<String> handleBillingFailed(BillingFailedException ex) {
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}
