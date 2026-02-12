package com.payment.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("Payment Request Validation Tests")
class PaymentRequestValidationTest {

    @Autowired
    private Validator validator;

    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest();
    }

    @Test
    @DisplayName("Should accept valid payment request")
    void testValidPaymentRequest() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertTrue(violations.isEmpty(), "Valid request should have no violations");
    }

    @Test
    @DisplayName("Should reject name with less than 3 characters")
    void testNameTooShort() {
        paymentRequest.setName("Jo");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Short name should have violations");
    }

    @Test
    @DisplayName("Should reject name with special characters")
    void testNameWithSpecialCharacters() {
        paymentRequest.setName("John@Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Name with special characters should have violations");
    }

    @Test
    @DisplayName("Should reject invalid email")
    void testInvalidEmail() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail("invalid-email");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Invalid email should have violations");
    }

    @Test
    @DisplayName("Should reject contact with less than 10 digits")
    void testContactTooShort() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("987654321");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Contact with less than 10 digits should have violations");
    }

    @Test
    @DisplayName("Should reject contact with non-numeric characters")
    void testContactWithLetters() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("98765432a0");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Contact with letters should have violations");
    }

    @Test
    @DisplayName("Should reject amount below minimum")
    void testAmountBelowMinimum() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("0.50"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Amount below minimum should have violations");
    }

    @Test
    @DisplayName("Should reject amount above maximum")
    void testAmountAboveMaximum() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("150000.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Amount above maximum should have violations");
    }

    @Test
    @DisplayName("Should reject null name")
    void testNullName() {
        paymentRequest.setName(null);
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Null name should have violations");
    }

    @Test
    @DisplayName("Should reject null email")
    void testNullEmail() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail(null);
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertFalse(violations.isEmpty(), "Null email should have violations");
    }

    @Test
    @DisplayName("Should accept name with spaces")
    void testNameWithSpaces() {
        paymentRequest.setName("John Michael Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("1500.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertTrue(violations.isEmpty(), "Name with spaces should be valid");
    }

    @Test
    @DisplayName("Should accept minimum valid amount")
    void testMinimumValidAmount() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("1.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertTrue(violations.isEmpty(), "Minimum valid amount should be accepted");
    }

    @Test
    @DisplayName("Should accept maximum valid amount")
    void testMaximumValidAmount() {
        paymentRequest.setName("John Doe");
        paymentRequest.setEmail("john@example.com");
        paymentRequest.setContact("9876543210");
        paymentRequest.setAmount(new BigDecimal("100000.00"));

        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(paymentRequest);
        assertTrue(violations.isEmpty(), "Maximum valid amount should be accepted");
    }
}
