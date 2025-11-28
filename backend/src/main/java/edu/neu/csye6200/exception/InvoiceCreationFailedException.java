package edu.neu.csye6200.exception;

public class InvoiceCreationFailedException extends RuntimeException {

    public InvoiceCreationFailedException(String message) {
        super(message);
    }

    public InvoiceCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvoiceCreationFailedException(Throwable cause) {
        super(cause);
    }

    public InvoiceCreationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
