package com.payment.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PaymentRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Name must contain only alphabets and spaces")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Contact must be exactly 10 digits")
    private String contact;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Amount must be at least ₹1.00")
    @DecimalMax(value = "100000.00", message = "Amount must not exceed ₹100,000.00")
    private BigDecimal amount;

    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z]{3,}$", message = "UPI ID must be in format: username@upiname")
    private String upiId;

    private String paymentMethod; // "card" or "upi"

    // Constructors
    public PaymentRequest() {}

    public PaymentRequest(String name, String email, String contact, BigDecimal amount) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.amount = amount;
        this.paymentMethod = "card";
    }

    public PaymentRequest(String name, String email, String contact, BigDecimal amount, String upiId, String paymentMethod) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.amount = amount;
        this.upiId = upiId;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
