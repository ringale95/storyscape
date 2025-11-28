package edu.neu.csye6200.exception;

/**
 * Exception thrown when a product configuration is not found.
 */
public class ProductConfigurationNotFoundException extends RuntimeException {
    
    public ProductConfigurationNotFoundException(String message) {
        super(message);
    }
    
    public ProductConfigurationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

