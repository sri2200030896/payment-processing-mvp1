package com.payment.dto;

import java.math.BigDecimal;

public class PaymentResponse {

    private Long id;
    private String name;
    private String email;
    private String contact;
    private BigDecimal amount;
    private String status;
    private String createdAt;

    // Constructors
    public PaymentResponse() {}

    public PaymentResponse(Long id, String name, String email, String contact, BigDecimal amount, String status, String createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
