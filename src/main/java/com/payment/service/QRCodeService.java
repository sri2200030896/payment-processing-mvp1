package com.payment.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QRCodeService {

    private static final Logger log = LoggerFactory.getLogger(QRCodeService.class);
    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;

    /**
     * Generate UPI QR code for payment
     * UPI format: upi://pay?pa=UPI_ID&pn=NAME&am=AMOUNT&tn=DESCRIPTION
     */
    public String generateUPIQRCode(String upiId, String payerName, String amount) {
        try {
            // Build UPI string
            String upiString = String.format(
                    "upi://pay?pa=%s&pn=%s&am=%s&tn=Payment",
                    upiId.replace("@", "%40"),
                    payerName.replace(" ", "%%20"),
                    amount
            );

            log.info("Generating QR code for UPI: {}", upiId);

            // Generate QR code
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    upiString,
                    BarcodeFormat.QR_CODE,
                    QR_WIDTH,
                    QR_HEIGHT
            );

            // Convert to image
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            // Encode to Base64 for embedding in HTML/JSON
            String base64Image = Base64.getEncoder().encodeToString(pngData);
            String dataUrl = "data:image/png;base64," + base64Image;

            log.info("QR code generated successfully");
            return dataUrl;
        } catch (Exception e) {
            log.error("Error generating QR code: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate QR code: " + e.getMessage());
        }
    }

    /**
     * Generate simple text QR code for display
     */
    public String generateUPIString(String upiId, String payerName, String amount) {
        return String.format(
                "upi://pay?pa=%s&pn=%s&am=%s&tn=Payment",
                upiId,
                payerName.replace(" ", " "),
                amount
        );
    }
}
