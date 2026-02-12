package com.payment.service;

import com.payment.dto.PaymentRequest;
import com.payment.dto.PaymentResponse;
import com.payment.entity.Payment;
import com.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private QRCodeService qrCodeService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Process a payment request
     */
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        try {
            // Create payment entity
            Payment payment = new Payment();
            payment.setName(sanitizeInput(paymentRequest.getName().trim()));
            payment.setEmail(sanitizeInput(paymentRequest.getEmail().trim().toLowerCase()));
            payment.setContact(paymentRequest.getContact());
            payment.setAmount(paymentRequest.getAmount());
            payment.setStatus("success");

            // Save to database
            Payment savedPayment = paymentRepository.save(payment);

            log.info("Payment processed successfully. Payment ID: {}, Email: {}", savedPayment.getId(), savedPayment.getEmail());

            PaymentResponse response = convertToResponse(savedPayment);

            // Send confirmation email asynchronously
            try {
                emailService.sendPaymentConfirmationEmail(response);
            } catch (Exception e) {
                log.warn("Email sending failed but payment was successful: {}", e.getMessage());
            }

            return response;
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process payment: " + e.getMessage());
        }
    }

    /**
     * Generate QR code for UPI payment
     */
    public String generateUPIQRCode(String upiId, String payerName, String amount) {
        return qrCodeService.generateUPIQRCode(upiId, payerName, amount);
    }

    /**
     * Get all payments
     */
    public List<PaymentResponse> getAllPayments() {
        try {
            List<Payment> payments = paymentRepository.findAll();
            log.info("Retrieved {} payments from database", payments.size());
            return payments.stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error retrieving payments: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve payments: " + e.getMessage());
        }
    }

    /**
     * Get payment by ID
     */
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));
        return convertToResponse(payment);
    }

    /**
     * Get payments by status
     */
    public List<PaymentResponse> getPaymentsByStatus(String status) {
        List<Payment> payments = paymentRepository.findByStatus(status);
        return payments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Sanitize input to prevent XSS attacks
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("/", "&#x2F;");
    }

    /**
     * Convert Payment entity to PaymentResponse DTO
     */
    private PaymentResponse convertToResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setName(payment.getName());
        response.setEmail(payment.getEmail());
        response.setContact(payment.getContact());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setCreatedAt(payment.getCreatedAt().format(DATE_FORMATTER));
        return response;
    }
}
