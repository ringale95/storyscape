package edu.neu.csye6200.exception;

/**
 * Exception thrown when billing processing fails.
 */
public class BillingFailedException extends RuntimeException {

    public BillingFailedException(String message) {
        super(message);
    }

    public BillingFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
