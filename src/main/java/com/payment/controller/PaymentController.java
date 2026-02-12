package com.payment.controller;

import com.payment.dto.ApiResponse;
import com.payment.dto.PaymentRequest;
import com.payment.dto.PaymentResponse;
import com.payment.service.PaymentService;
import com.payment.service.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"}, allowedHeaders = "*")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Process a payment request
     * POST /api/payment
     */
    @PostMapping("/payment")
    public ResponseEntity<ApiResponse> processPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Processing payment for email: {}", paymentRequest.getEmail());
        
        try {
            PaymentResponse paymentResponse = paymentService.processPayment(paymentRequest);
            
            ApiResponse response = ApiResponse.builder()
                    .success(true)
                    .message("Payment processed successfully")
                    .paymentId(String.valueOf(paymentResponse.getId()))
                    .data(paymentResponse)
                    .build();
            
            log.info("Payment processed successfully with ID: {}", paymentResponse.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage(), e);
            
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .message("Failed to process payment: " + e.getMessage())
                    .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get all payments
     * GET /api/payments
     */
    @GetMapping("/payments")
    public ResponseEntity<?> getAllPayments() {
        log.info("Fetching all payments");
        
        try {
            List<PaymentResponse> payments = paymentService.getAllPayments();
            
            java.util.Map<String, Object> response = new java.util.LinkedHashMap<>();
            response.put("success", true);
            response.put("message", "Payments retrieved successfully");
            response.put("data", payments);
            
            log.info("Retrieved {} payments", payments.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching payments: {}", e.getMessage(), e);
            
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .message("Failed to retrieve payments: " + e.getMessage())
                    .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get payment by ID
     * GET /api/payments/:id
     */
    @GetMapping("/payments/{id}")
    public ResponseEntity<ApiResponse> getPaymentById(@PathVariable Long id) {
        log.info("Fetching payment with ID: {}", id);
        
        try {
            PaymentResponse payment = paymentService.getPaymentById(id);
            
            ApiResponse response = ApiResponse.builder()
                    .success(true)
                    .message("Payment retrieved successfully")
                    .data(payment)
                    .build();
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Payment not found: {}", e.getMessage());
            
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("Error fetching payment: {}", e.getMessage(), e);
            
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .message("Failed to retrieve payment: " + e.getMessage())
                    .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get payments by status
     * GET /api/payments/status/:status
     */
    @GetMapping("/payments/status/{status}")
    public ResponseEntity<?> getPaymentsByStatus(@PathVariable String status) {
        log.info("Fetching payments with status: {}", status);
        
        try {
            List<PaymentResponse> payments = paymentService.getPaymentsByStatus(status);
            
            java.util.Map<String, Object> response = new java.util.LinkedHashMap<>();
            response.put("success", true);
            response.put("message", "Payments retrieved successfully");
            response.put("data", payments);
            
            log.info("Retrieved {} payments with status: {}", payments.size(), status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching payments by status: {}", e.getMessage(), e);
            
            ApiResponse response = ApiResponse.builder()
                    .success(false)
                    .message("Failed to retrieve payments: " + e.getMessage())
                    .build();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Health check endpoint
     * GET /api/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        ApiResponse response = ApiResponse.builder()
                .success(true)
                .message("Payment Processing API is running")
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * Login endpoint for dashboard authentication
     * POST /api/auth/login
     */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");

            if (username == null || password == null) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Username and password are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            String token = authenticationService.authenticate(username, password);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("message", "Authentication successful");
            response.put("token", token);
            response.put("username", username);

            log.info("User {} logged in successfully", username);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            log.error("Login error: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Verify token endpoint
     * POST /api/auth/verify
     */
    @PostMapping("/auth/verify")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");

            if (token == null || token.isEmpty()) {
                Map<String, Object> response = new LinkedHashMap<>();
                response.put("success", false);
                response.put("valid", false);
                return ResponseEntity.ok(response);
            }

            boolean isValid = authenticationService.verifyToken(token);
            String username = authenticationService.getUsernameFromToken(token);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("valid", isValid);
            response.put("username", username);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token verification error: {}", e.getMessage(), e);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", false);
            response.put("valid", false);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Logout endpoint
     * POST /api/auth/logout
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            authenticationService.logout(token);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("message", "Logged out successfully");

            log.info("User logged out");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Logout error: {}", e.getMessage(), e);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", false);
            response.put("message", "Logout failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate UPI QR code
     * POST /api/qr-code
     */
    @PostMapping("/qr-code")
    public ResponseEntity<?> generateQRCode(@RequestBody Map<String, String> request) {
        try {
            String upiId = request.get("upiId");
            String name = request.get("name");
            String amount = request.get("amount");

            if (upiId == null || name == null || amount == null) {
                Map<String, Object> errorResponse = new LinkedHashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "UPI ID, name, and amount are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            String qrCodeDataUrl = paymentService.generateUPIQRCode(upiId, name, amount);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("message", "QR code generated successfully");
            response.put("qrCode", qrCodeDataUrl);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("QR code generation error: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to generate QR code: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
