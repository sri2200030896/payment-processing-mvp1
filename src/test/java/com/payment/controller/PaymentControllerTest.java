package com.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.dto.PaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Payment Controller Integration Tests")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentRequest validPaymentRequest;

    @BeforeEach
    void setUp() {
        validPaymentRequest = new PaymentRequest();
        validPaymentRequest.setName("John Doe");
        validPaymentRequest.setEmail("john@example.com");
        validPaymentRequest.setContact("9876543210");
        validPaymentRequest.setAmount(new BigDecimal("1500.00"));
    }

    @Test
    @DisplayName("Should process valid payment and return 201 Created")
    void testProcessPaymentSuccess() throws Exception {
        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validPaymentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payment processed successfully"))
                .andExpect(jsonPath("$.paymentId").exists())
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@example.com"))
                .andExpect(jsonPath("$.data.status").value("success"));
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid name")
    void testProcessPaymentInvalidName() throws Exception {
        PaymentRequest invalidRequest = new PaymentRequest();
        invalidRequest.setName("Jo");
        invalidRequest.setEmail("john@example.com");
        invalidRequest.setContact("9876543210");
        invalidRequest.setAmount(new BigDecimal("1500.00"));

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors.name").exists());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid email")
    void testProcessPaymentInvalidEmail() throws Exception {
        PaymentRequest invalidRequest = new PaymentRequest();
        invalidRequest.setName("John Doe");
        invalidRequest.setEmail("invalid-email");
        invalidRequest.setContact("9876543210");
        invalidRequest.setAmount(new BigDecimal("1500.00"));

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid contact")
    void testProcessPaymentInvalidContact() throws Exception {
        PaymentRequest invalidRequest = new PaymentRequest();
        invalidRequest.setName("John Doe");
        invalidRequest.setEmail("john@example.com");
        invalidRequest.setContact("123");
        invalidRequest.setAmount(new BigDecimal("1500.00"));

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors.contact").exists());
    }

    @Test
    @DisplayName("Should return 400 Bad Request for invalid amount")
    void testProcessPaymentInvalidAmount() throws Exception {
        PaymentRequest invalidRequest = new PaymentRequest();
        invalidRequest.setName("John Doe");
        invalidRequest.setEmail("john@example.com");
        invalidRequest.setContact("9876543210");
        invalidRequest.setAmount(new BigDecimal("0.50"));

        mockMvc.perform(post("/api/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors.amount").exists());
    }

    @Test
    @DisplayName("Should get all payments")
    void testGetAllPayments() throws Exception {
        mockMvc.perform(get("/api/payments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payments retrieved successfully"));
    }

    @Test
    @DisplayName("Should get health check")
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payment Processing API is running"));
    }

    @Test
    @DisplayName("Should return 404 for non-existent payment")
    void testGetNonExistentPayment() throws Exception {
        mockMvc.perform(get("/api/payments/99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
