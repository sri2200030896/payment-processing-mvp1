package com.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private boolean success;
    private String message;
    private PaymentResponse data;
    private Map<String, String> errors;
    private String paymentId;

    // Constructors
    public ApiResponse() {}

    public ApiResponse(boolean success, String message, PaymentResponse data, Map<String, String> errors, String paymentId) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.paymentId = paymentId;
    }

    // Static builder method
    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentResponse getData() {
        return data;
    }

    public void setData(PaymentResponse data) {
        this.data = data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    // Builder class
    public static class Builder {
        private boolean success;
        private String message;
        private PaymentResponse data;
        private Map<String, String> errors;
        private String paymentId;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(PaymentResponse data) {
            this.data = data;
            return this;
        }

        public Builder errors(Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public Builder paymentId(String paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(success, message, data, errors, paymentId);
        }
    }
}
