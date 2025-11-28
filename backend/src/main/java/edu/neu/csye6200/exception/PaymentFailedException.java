package edu.neu.csye6200.exception;

/**
 * Exception thrown when payment processing fails.
 */
public class PaymentFailedException extends RuntimeException {
    
    public PaymentFailedException(String message) {
        super(message);
    }
    
    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}

