package com.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    // Hardcoded credentials
    private static final String VALID_USERNAME = "srikanth";
    private static final String VALID_PASSWORD = "1234";

    // Token storage (in production, use Redis or database)
    private Map<String, AuthToken> tokens = new HashMap<>();

    /**
     * Authenticate user with username and password
     */
    public String authenticate(String username, String password) {
        log.info("Authentication attempt for user: {}", username);

        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password are required");
        }

        if (!username.equals(VALID_USERNAME) || !password.equals(VALID_PASSWORD)) {
            log.warn("Authentication failed for user: {}", username);
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Generate token
        String token = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(token, username, System.currentTimeMillis() + (24 * 60 * 60 * 1000)); // 24 hours

        tokens.put(token, authToken);
        log.info("Authentication successful for user: {}", username);

        return token;
    }

    /**
     * Verify if token is valid
     */
    public boolean verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        AuthToken authToken = tokens.get(token);
        if (authToken == null) {
            log.warn("Token not found: {}", token);
            return false;
        }

        // Check if token is expired
        if (authToken.expiresAt < System.currentTimeMillis()) {
            tokens.remove(token);
            log.warn("Token expired: {}", token);
            return false;
        }

        return true;
    }

    /**
     * Get username from token
     */
    public String getUsernameFromToken(String token) {
        AuthToken authToken = tokens.get(token);
        if (authToken != null && authToken.expiresAt > System.currentTimeMillis()) {
            return authToken.username;
        }
        return null;
    }

    /**
     * Logout (invalidate token)
     */
    public void logout(String token) {
        if (token != null) {
            tokens.remove(token);
            log.info("Token invalidated: {}", token);
        }
    }

    /**
     * Inner class for token storage
     */
    private static class AuthToken {
        String token;
        String username;
        long expiresAt;

        AuthToken(String token, String username, long expiresAt) {
            this.token = token;
            this.username = username;
            this.expiresAt = expiresAt;
        }
    }
}
