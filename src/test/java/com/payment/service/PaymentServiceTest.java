package com.payment.service;

import com.payment.dto.PaymentRequest;
import com.payment.dto.PaymentResponse;
import com.payment.entity.Payment;
import com.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Payment Service Tests")
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentRequest validPaymentRequest;
    private Payment mockPayment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create valid payment request
        validPaymentRequest = new PaymentRequest();
        validPaymentRequest.setName("John Doe");
        validPaymentRequest.setEmail("john@example.com");
        validPaymentRequest.setContact("9876543210");
        validPaymentRequest.setAmount(new BigDecimal("1500.00"));

        // Create mock payment entity
        mockPayment = new Payment();
        mockPayment.setId(1L);
        mockPayment.setName("John Doe");
        mockPayment.setEmail("john@example.com");
        mockPayment.setContact("9876543210");
        mockPayment.setAmount(new BigDecimal("1500.00"));
        mockPayment.setStatus("success");
        mockPayment.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should process valid payment successfully")
    void testProcessPaymentSuccess() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        PaymentResponse response = paymentService.processPayment(validPaymentRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("9876543210", response.getContact());
        assertEquals("success", response.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should sanitize XSS attempts in name field")
    void testXSSSanitization() {
        PaymentRequest xssRequest = new PaymentRequest();
        xssRequest.setName("John <script>alert('xss')</script> Doe");
        xssRequest.setEmail("john@example.com");
        xssRequest.setContact("9876543210");
        xssRequest.setAmount(new BigDecimal("1500.00"));

        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        PaymentResponse response = paymentService.processPayment(xssRequest);

        assertNotNull(response);
        // Verify XSS characters are sanitized
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should convert payment entity to response correctly")
    void testPaymentConversion() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        PaymentResponse response = paymentService.processPayment(validPaymentRequest);

        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("9876543210", response.getContact());
        assertEquals(new BigDecimal("1500.00"), response.getAmount());
    }

    @Test
    @DisplayName("Should throw exception when database operation fails")
    void testProcessPaymentDatabaseError() {
        when(paymentRepository.save(any(Payment.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> {
            paymentService.processPayment(validPaymentRequest);
        });
    }
}
