package edu.neu.csye6200.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.csye6200.dto.InvoiceDTO;
import edu.neu.csye6200.entity.Invoice;
import edu.neu.csye6200.service.InvoicePdfService;
import edu.neu.csye6200.service.InvoiceService;

@RestController
@RequestMapping("/users")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoicePdfService invoicePdfService;

    @GetMapping("/{userId}/invoices")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByUserId(@PathVariable Long userId) {
        List<InvoiceDTO> invoices = invoiceService.getInvoicesByUserId(userId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{userId}/invoices/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long userId, @PathVariable Long invoiceId) {
        InvoiceDTO invoice = invoiceService.getInvoiceById(userId, invoiceId);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/{userId}/invoices/{invoiceId}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long userId, @PathVariable Long invoiceId) {
        try {
            // Get invoice with all relationships loaded
            Invoice invoice = invoiceService.getInvoiceForPdf(userId, invoiceId);

            // Generate PDF
            byte[] pdfBytes = invoicePdfService.generateInvoicePdf(invoice);

            // Set response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "invoice_" + invoiceId + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
