package edu.neu.csye6200.observer;

import edu.neu.csye6200.entity.Invoice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject in Observer pattern.
 * Manages observers and notifies them when invoices are created.
 */
@Component
public class InvoiceEventPublisher {
    
    private final List<InvoiceObserver> observers = new ArrayList<>();
    
    /**
     * Register an observer to be notified of invoice creation events.
     * 
     * @param observer The observer to register
     */
    public void subscribe(InvoiceObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Unregister an observer.
     * 
     * @param observer The observer to unregister
     */
    public void unsubscribe(InvoiceObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Notify all registered observers that an invoice was created.
     * 
     * @param invoice The invoice that was created
     * @param productName The product name associated with the invoice
     * @param storyId Optional story ID for product-specific actions
     */
    public void notifyInvoiceCreated(Invoice invoice, String productName, Long storyId) {
        for (InvoiceObserver observer : observers) {
            try {
                observer.onInvoiceCreated(invoice, productName, storyId);
            } catch (Exception e) {
                // Log error but don't fail the invoice creation
                System.err.println("Error notifying observer: " + observer.getClass().getName() + 
                                 ". Error: " + e.getMessage());
            }
        }
    }
}

