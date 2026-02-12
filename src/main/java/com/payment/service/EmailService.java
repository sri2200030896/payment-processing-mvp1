package com.payment.service;

import com.payment.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    /**
     * Send payment confirmation email
     */
    public void sendPaymentConfirmationEmail(PaymentResponse payment) {
        // Check if email is configured
        if (mailSender == null) {
            log.warn("Email service not configured. Skipping email for payment ID: {}", payment.getId());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(payment.getEmail());
            helper.setSubject("Payment Confirmation - Payment ID: #" + payment.getId());
            helper.setFrom("noreply@paymentmvp.com");

            String emailContent = buildEmailContent(payment);
            helper.setText(emailContent, true);

            mailSender.send(message);
            log.info("Payment confirmation email sent to: {}", payment.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", payment.getEmail(), e);
            // Don't throw exception - payment is already processed
        } catch (Exception e) {
            log.error("Unexpected error sending email: {}", e.getMessage(), e);
        }
    }

    /**
     * Build HTML email content
     */
    private String buildEmailContent(PaymentResponse payment) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 5px; text-align: center; }" +
                ".content { background: #f9f9f9; padding: 20px; margin-top: 20px; border-radius: 5px; }" +
                ".detail { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #ddd; }" +
                ".label { font-weight: bold; color: #667eea; }" +
                ".footer { text-align: center; color: #999; font-size: 12px; margin-top: 20px; }" +
                ".success-badge { display: inline-block; background: #27ae60; color: white; padding: 5px 10px; border-radius: 3px; margin: 10px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>✓ Payment Successful!</h1>" +
                "<p>Thank you for your payment</p>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='detail'>" +
                "<span class='label'>Payment ID:</span>" +
                "<span>#" + payment.getId() + "</span>" +
                "</div>" +
                "<div class='detail'>" +
                "<span class='label'>Name:</span>" +
                "<span>" + escapeHtml(payment.getName()) + "</span>" +
                "</div>" +
                "<div class='detail'>" +
                "<span class='label'>Email:</span>" +
                "<span>" + escapeHtml(payment.getEmail()) + "</span>" +
                "</div>" +
                "<div class='detail'>" +
                "<span class='label'>Contact:</span>" +
                "<span>" + escapeHtml(payment.getContact()) + "</span>" +
                "</div>" +
                "<div class='detail'>" +
                "<span class='label'>Amount:</span>" +
                "<span>₹" + String.format("%.2f", payment.getAmount()) + "</span>" +
                "</div>" +
                "<div class='detail'>" +
                "<span class='label'>Status:</span>" +
                "<span><span class='success-badge'>" + payment.getStatus().toUpperCase() + "</span></span>" +
                "</div>" +
                "<div class='detail'>" +
                "<span class='label'>Date & Time:</span>" +
                "<span>" + payment.getCreatedAt() + "</span>" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>This is an automated message. Please do not reply to this email.</p>" +
                "<p>&copy; 2026 Payment Processing MVP. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Escape HTML to prevent XSS
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#039;");
    }
}
