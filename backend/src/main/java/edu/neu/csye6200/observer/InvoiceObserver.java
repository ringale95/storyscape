package edu.neu.csye6200.observer;

import edu.neu.csye6200.entity.Invoice;

/**
 * Observer interface for Invoice creation events.
 * Implementations of this interface will be notified when an invoice is created.
 */
public interface InvoiceObserver {
    /**
     * Called when an invoice is created.
     * 
     * @param invoice The invoice that was created
     * @param productName The product name associated with the invoice
     * @param storyId Optional story ID for product-specific actions (e.g., featuring a story)
     */
    void onInvoiceCreated(Invoice invoice, String productName, Long storyId);
}

