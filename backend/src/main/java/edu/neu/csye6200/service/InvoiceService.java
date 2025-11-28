package edu.neu.csye6200.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6200.dto.InvoiceDTO;
import edu.neu.csye6200.entity.Invoice;
import edu.neu.csye6200.entity.User;
import edu.neu.csye6200.exception.InvoiceNotFoundException;
import edu.neu.csye6200.exception.UserNotFoundException;
import edu.neu.csye6200.repository.InvoiceRepository;
import edu.neu.csye6200.repository.UserRepository;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all invoices for a specific user
     */
    public List<InvoiceDTO> getInvoicesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Use JOIN FETCH to eagerly load payment relationship
        List<Invoice> invoices = invoiceRepository.findByUserWithPayment(user);

        return invoices.stream()
                .map(InvoiceDTO::new)
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceById(Long userId, Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + invoiceId));

        if (invoice.getUser().getId() != userId)
            throw new InvoiceNotFoundException("Invoice not found for user with id: " + userId);

        return new InvoiceDTO(invoice);
    }

    public Invoice getInvoiceForPdf(Long userId, Long invoiceId) {
        Invoice invoice = invoiceRepository.findByIdWithRelationships(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found with id: " + invoiceId));

        if (invoice.getUser().getId() != userId)
            throw new InvoiceNotFoundException("Invoice not found for user with id: " + userId);

        return invoice;
    }
}
