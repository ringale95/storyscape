package edu.neu.csye6200.factory;

import java.time.LocalDate;

import edu.neu.csye6200.entity.Invoice;
import edu.neu.csye6200.entity.Payment;
import edu.neu.csye6200.entity.PayAsYouGo;
import edu.neu.csye6200.entity.ProductConfiguration;
import edu.neu.csye6200.entity.Subscription;
import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.service.BillingService.BillingAction;

public class InvoiceFactory {

    private static InvoiceFactory instance;

    private InvoiceFactory() {
    }

    public static InvoiceFactory getInstance() {
        if (instance == null)
            instance = new InvoiceFactory();
        return instance;
    }

    public Invoice createInvoice(User user, ProductConfiguration config, BillingAction action) {
        Payment payment = config.getPayment();
        LocalDate today = LocalDate.now();

        // Calculate amount based on action
        long priceCents = action == BillingAction.DEDUCT
                ? payment.getPriceCents()
                : -payment.getPriceCents();

        // Determine payment type (payments are eagerly loaded via JOIN FETCH)
        String paymentType = payment instanceof Subscription ? "SUBSCRIPTION"
                : payment instanceof PayAsYouGo ? "PAYG"
                        : "UNKNOWN";

        // Store payment data directly as immutable snapshot
        return new Invoice(
                user,
                payment.getId(),
                paymentType,
                today,
                today,
                priceCents,
                paymentType + " for " + config.getProductName());
    }

}
