package edu.neu.csye6200.exception;

/**
 * Exception thrown when a user is not found.
 */
public class InvoiceNotFoundException extends RuntimeException {
    
    public InvoiceNotFoundException(String message) {
        super(message);
    }
    
    public InvoiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

