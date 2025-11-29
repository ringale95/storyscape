package edu.neu.csye6200.dto;

import edu.neu.csye6200.entity.Invoice;

/**
 * Data Transfer Object for Invoice responses.
 * Maps Invoice entity to frontend-friendly format.
 */
public class InvoiceDTO {
    private Long id;
    private Long subscriptionId; // Payment ID (referred to as subscriptionId in frontend)
    private String dateFrom;
    private String dateTo;
    private Double amount; // Amount in dollars (converted from cents)
    private String description;
    private String createdAt;
    private String updatedAt;
    private String pdfUrl; // Placeholder for PDF URL

    public InvoiceDTO() {
    }

    public InvoiceDTO(Invoice invoice) {
        this.id = invoice.getId();
        // Use stored paymentId instead of Payment reference
        this.subscriptionId = invoice.getPaymentId();
        this.dateFrom = invoice.getDateFrom() != null ? invoice.getDateFrom().toString() : null;
        this.dateTo = invoice.getDateTo() != null ? invoice.getDateTo().toString() : null;
        // Convert cents to dollars
        this.amount = invoice.getAmount() / 100.0;
        this.description = invoice.getDescription();
        this.createdAt = invoice.getCreatedAt() != null ? invoice.getCreatedAt().toString() : null;
        this.updatedAt = invoice.getUpdatedAt() != null ? invoice.getUpdatedAt().toString() : null;
        this.pdfUrl = "#"; // Placeholder - can be implemented later
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}
