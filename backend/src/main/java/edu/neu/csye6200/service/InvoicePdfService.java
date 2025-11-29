package edu.neu.csye6200.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import edu.neu.csye6200.entity.Invoice;

/**
 * Service for generating PDF invoices
 */
@Service
public class InvoicePdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 20;
    private static final float TITLE_FONT_SIZE = 24;
    private static final float HEADER_FONT_SIZE = 12;
    private static final float NORMAL_FONT_SIZE = 10;

    /**
     * Helper method to write text to PDF
     */
    private float writeText(PDPageContentStream stream, float x, float y, String text,
            PDType1Font font, float fontSize) throws IOException {
        stream.beginText();
        stream.setFont(font, fontSize);
        stream.newLineAtOffset(x, y);
        stream.showText(text);
        stream.endText();
        return y - LINE_HEIGHT;
    }

    /**
     * Format payment type for display
     */
    private String formatPaymentType(String paymentType) {
        if (paymentType == null)
            return "";
        return switch (paymentType) {
            case "SUBSCRIPTION" -> "Subscription";
            case "PAYG" -> "Pay As You Go";
            default -> paymentType;
        };
    }

    /**
     * Generate PDF bytes for an invoice
     */
    public byte[] generateInvoicePdf(Invoice invoice) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream stream = new PDPageContentStream(document, page)) {
                float yPos = 750;
                PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDType1Font normalFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

                // Title
                yPos = writeText(stream, MARGIN, yPos, "INVOICE", boldFont, TITLE_FONT_SIZE);
                yPos -= 20;

                // Invoice ID
                yPos = writeText(stream, MARGIN, yPos, "Invoice ID: #" + invoice.getId(), normalFont, NORMAL_FONT_SIZE);
                yPos -= 10;

                // Customer Information
                if (invoice.getUser() != null) {
                    yPos = writeText(stream, MARGIN, yPos, "Bill To:", boldFont, HEADER_FONT_SIZE);
                    yPos = writeText(stream, MARGIN, yPos,
                            invoice.getUser().getFirstName() + " " + invoice.getUser().getLastName(),
                            normalFont, NORMAL_FONT_SIZE);
                    yPos = writeText(stream, MARGIN, yPos, invoice.getUser().getEmail(), normalFont, NORMAL_FONT_SIZE);
                    yPos -= 10;
                }

                // Invoice Details
                yPos -= 10;
                yPos = writeText(stream, MARGIN, yPos, "Invoice Details:", boldFont, HEADER_FONT_SIZE);

                // Description
                if (invoice.getDescription() != null && !invoice.getDescription().isEmpty()) {
                    yPos = writeText(stream, MARGIN, yPos, "Description: " + invoice.getDescription(),
                            normalFont, NORMAL_FONT_SIZE);
                }

                // Period
                if (invoice.getDateFrom() != null) {
                    String period = "Period: " + invoice.getDateFrom().format(DATE_FORMATTER) +
                            " to " + invoice.getDateTo().format(DATE_FORMATTER);
                    yPos = writeText(stream, MARGIN, yPos, period, normalFont, NORMAL_FONT_SIZE);
                }

                // Payment Type
                if (invoice.getPaymentType() != null && !invoice.getPaymentType().isEmpty()) {
                    String paymentTypeDisplay = formatPaymentType(invoice.getPaymentType());
                    yPos = writeText(stream, MARGIN, yPos, "Payment Type: " + paymentTypeDisplay,
                            normalFont, NORMAL_FONT_SIZE);
                }

                // Amount
                yPos -= 10;
                double amountDollars = invoice.getAmount() / 100.0;
                yPos = writeText(stream, MARGIN, yPos,
                        "Amount: $" + String.format("%.2f", amountDollars),
                        boldFont, HEADER_FONT_SIZE);

                // Created Date
                if (invoice.getCreatedAt() != null) {
                    yPos = writeText(stream, MARGIN, yPos,
                            "Created: " + invoice.getCreatedAt().format(DATETIME_FORMATTER),
                            normalFont, NORMAL_FONT_SIZE);
                }

                // Updated Date
                if (invoice.getUpdatedAt() != null) {
                    writeText(stream, MARGIN, yPos,
                            "Last Updated: " + invoice.getUpdatedAt().format(DATETIME_FORMATTER),
                            normalFont, NORMAL_FONT_SIZE);
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }
}