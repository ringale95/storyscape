package edu.neu.csye6200.exception;

/**
 * Exception thrown when wallet deduction fails (e.g., insufficient funds).
 */
public class ProductAccessDeniedException extends RuntimeException {
        
    public ProductAccessDeniedException(String message) {
        super(message);
    }
    
    public ProductAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}

