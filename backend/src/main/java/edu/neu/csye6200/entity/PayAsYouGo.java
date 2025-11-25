package edu.neu.csye6200.entity;

import jakarta.persistence.*;

/**
 * Pay-As-You-Go payment type.
 * Stored in the payments table with discriminator value 'PAYG'.
 */
@Entity
@DiscriminatorValue("PAYG")
public class PayAsYouGo extends Payment {

    // Default constructor for JPA
    public PayAsYouGo() {
        super();
    }

    public PayAsYouGo(long priceCents, String description) {
        super(priceCents, description);
    }
}