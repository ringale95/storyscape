package edu.neu.csye6200.exception;

/**
 * Exception thrown when wallet deduction fails (e.g., insufficient funds).
 */
public class UpdateWalletFailedException extends RuntimeException {
    
    private final long requiredAmount;
    private final long availableAmount;
    
    public UpdateWalletFailedException(String message) {
        super(message);
        this.requiredAmount = 0;
        this.availableAmount = 0;
    }
    
    public UpdateWalletFailedException(String message, long requiredAmount, long availableAmount) {
        super(message);
        this.requiredAmount = requiredAmount;
        this.availableAmount = availableAmount;
    }
    
    public UpdateWalletFailedException(String message, Throwable cause) {
        super(message, cause);
        this.requiredAmount = 0;
        this.availableAmount = 0;
    }
    
    public long getRequiredAmount() {
        return requiredAmount;
    }
    
    public long getAvailableAmount() {
        return availableAmount;
    }
}

