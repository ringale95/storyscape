package edu.neu.csye6200.factory;

import java.lang.reflect.InvocationTargetException;

import edu.neu.csye6200.entity.Payment;

/**
 * OOD Principles Used:
 *
 * 1. Singleton Pattern – Ensures only one instance of PaymentFactory exists.
 *
 * 2. Factory Pattern – Centralizes and abstracts creation of Payment objects.
 *
 * 3. Polymorphism – Returns Payment (parent type) while creating subclasses.
 *
 * 4. Abstraction – Hides complex object creation logic from client code.
 *
 * 5. Encapsulation – Keeps all Payment construction rules inside the factory.
 */

public class PaymentFactory {
    private static PaymentFactory instance;

    private PaymentFactory() {
    }

    public static PaymentFactory getInstance() {
        if (instance == null)
            instance = new PaymentFactory();
        return instance;
    }

    public Payment createPayment(Class<? extends Payment> paymentClass, long priceCents, String description,
                                 long subscriptionCost) {
        try {
            if (paymentClass.getSimpleName().equals("Subscription")) {
                return paymentClass.getDeclaredConstructor(Long.class, String.class, long.class)
                        .newInstance(Long.valueOf(subscriptionCost), description, priceCents);
            } else {
                return paymentClass.getDeclaredConstructor(long.class, String.class)
                        .newInstance(priceCents, description);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException e) {
            throw new RuntimeException("Failed to create payment of type: " + paymentClass.getSimpleName(), e);
        }
    }
}

