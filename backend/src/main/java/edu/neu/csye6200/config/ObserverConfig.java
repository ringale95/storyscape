package edu.neu.csye6200.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import edu.neu.csye6200.observer.InvoiceEventPublisher;
import edu.neu.csye6200.observer.StoryFeaturingObserver;

import jakarta.annotation.PostConstruct;

/**
 * Configuration class to wire up Observer pattern.
 * Registers observers with the InvoiceEventPublisher when the application starts.
 */
@Configuration
public class ObserverConfig {
    
    @Autowired
    private InvoiceEventPublisher invoiceEventPublisher;
    
    @Autowired
    private StoryFeaturingObserver storyFeaturingObserver;
    
    /**
     * Register observers after Spring context is initialized.
     */
    @PostConstruct
    public void registerObservers() {
        invoiceEventPublisher.subscribe(storyFeaturingObserver);
    }
}

