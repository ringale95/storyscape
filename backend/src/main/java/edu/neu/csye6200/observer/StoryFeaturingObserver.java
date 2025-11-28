package edu.neu.csye6200.observer;

import edu.neu.csye6200.entity.Invoice;
import edu.neu.csye6200.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.neu.csye6200.service.StoryService;

/**
 * Observer that features a story when an invoice is created for FeaturedPost product.
 * This implements the Observer pattern to decouple invoice creation from story featuring.
 */
@Component
public class StoryFeaturingObserver implements InvoiceObserver {
    
    @Autowired
    private StoryService storyService;
    
    @Override
    public void onInvoiceCreated(Invoice invoice, String productName, Long storyId) {
        // Only feature stories for FeaturedPost product
        if (!"FeaturedPost".equalsIgnoreCase(productName)) {
            return;
        }
        
        try {
            User user = invoice.getUser();
            if (user == null) {
                System.err.println("Cannot feature story: Invoice has no associated user");
                return;
            }
            
            // Feature the specified story, or user's most recent story if storyId is not provided
            storyService.featureStory(storyId);
        } catch (Exception e) {
            // Log error but don't fail the invoice creation process
            System.err.println("Failed to feature story for invoice " + invoice.getId() + 
                             ": " + e.getMessage());
        }
    }
}

