# Payment Processing MVP

A full-stack payment processing application built with **Java Spring Boot**, **MySQL**, and **HTML5/JavaScript**. This project demonstrates core full-stack development principles including RESTful API design, form validation, database operations, and error handling.

## 📋 Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Error Handling](#error-handling)
- [Security Features](#security-features)
- [Screenshots](#screenshots)
- [Troubleshooting](#troubleshooting)

## Overview

This MVP (Minimum Viable Product) implements a secure payment processing system with:
- Client-side form validation with real-time feedback
- Server-side validation for all inputs
- XSS prevention through input sanitization
- RESTful API with proper error handling
- MySQL database with proper schema
- Responsive UI design

**Time Limit:** 3-4 hours  
**Level:** Entry-level Full Stack Developer  
**Position:** Developer - Full Stack

## Technology Stack

### Backend
- **Framework:** Spring Boot 3.2.0
- **Language:** Java 17
- **Database:** MySQL 8.0
- **Build Tool:** Apache Maven
- **ORM:** JPA/Hibernate
- **Validation:** Jakarta Validation API

### Frontend
- **HTML5** - Semantic markup
- **CSS3** - Responsive styling with modern gradients
- **Vanilla JavaScript** - Client-side validation and API integration

### Additional Tools
- **Testing:** JUnit 5, Mockito
- **Logging:** SLF4J
- **Code Tools:** Lombok

## Features

### ✅ Core Features
- [x] Responsive payment form with validation
- [x] Client-side form validation with real-time feedback
- [x] Server-side validation for all fields
- [x] RESTful API endpoints for payment processing
- [x] MySQL database integration
- [x] Success/error message display
- [x] Payment history retrieval
- [x] Comprehensive error handling

### ✅ Bonus Features
- [x] Input sanitization for XSS prevention
- [x] Unit tests for validation logic
- [x] CORS configuration
- [x] Logging and monitoring
- [x] Health check endpoint
- [x] Advanced error responses
- [x] Payment details display after success

## Project Structure

```
payment-processing-mvp/
├── src/
│   ├── main/
│   │   ├── java/com/payment/
│   │   │   ├── PaymentProcessingMvpApplication.java
│   │   │   ├── controller/
│   │   │   │   └── PaymentController.java
│   │   │   ├── service/
│   │   │   │   └── PaymentService.java
│   │   │   ├── entity/
│   │   │   │   └── Payment.java
│   │   │   ├── dto/
│   │   │   │   ├── PaymentRequest.java
│   │   │   │   ├── PaymentResponse.java
│   │   │   │   └── ApiResponse.java
│   │   │   ├── repository/
│   │   │   │   └── PaymentRepository.java
│   │   │   └── exception/
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           └── index.html
│   └── test/
│       └── java/com/payment/service/
│           └── PaymentServiceTest.java
├── pom.xml
├── README.md
└── .env.example
```

## Prerequisites

Before you begin, ensure you have the following installed:

### System Requirements
- **Java 17 or higher** - [Download Java](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.8.1 or higher** - [Download Maven](https://maven.apache.org/download.cgi)
- **MySQL Server 8.0 or higher** - [Download MySQL](https://dev.mysql.com/downloads/mysql/)

### Verify Installation
```bash
java -version
mvn -version
mysql --version
```

## Installation & Setup

### Step 1: Clone or Navigate to Project Directory
```bash
cd /Users/vijaykadarla/Desktop/payment-processing-mvp
```

### Step 2: Create MySQL Database
Open MySQL CLI and run:

```sql
-- Create database
CREATE DATABASE payment_db;

-- Use the database
USE payment_db;

-- The Payment table will be created automatically by Hibernate
-- But you can manually create it if needed:

CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    contact VARCHAR(10) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_status (status)
);
```

### Step 3: Configure Database Connection
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/payment_db
spring.datasource.username=root
spring.datasource.password=root
```

**Note:** Update the password if your MySQL root user has a different password.

### Step 4: Build the Project
```bash
mvn clean install
```

This will download all dependencies and compile the project.

## Running the Application

### Step 1: Start MySQL Server
```bash
# macOS (with Homebrew)
mysql.server start

# Or using services
sudo systemctl start mysql

# Or start MySQL Community Server application
```

### Step 2: Run Spring Boot Application
```bash
# Option 1: Using Maven
mvn spring-boot:run

# Option 2: Run JAR file
mvn clean package
java -jar target/payment-processing-mvp-1.0.0.jar
```

### Step 3: Access the Application
Open your browser and navigate to:
```
http://localhost:8080
```

### Step 4: Verify API is Running
Check the health endpoint:
```bash
curl http://localhost:8080/api/health
```

Expected response:
```json
{
  "success": true,
  "message": "Payment Processing API is running"
}
```

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### 1. Process Payment
**Endpoint:** `POST /api/payment`

**Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "contact": "9876543210",
  "amount": 1500.00
}
```

**Success Response (201 Created):**
```json
{
  "success": true,
  "message": "Payment processed successfully",
  "paymentId": "123",
  "data": {
    "id": 123,
    "name": "John Doe",
    "email": "john@example.com",
    "contact": "9876543210",
    "amount": 1500.00,
    "status": "success",
    "createdAt": "2026-02-12 10:30:45"
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "name": "Name must be between 3 and 50 characters",
    "email": "Invalid email format",
    "contact": "Contact must be exactly 10 digits",
    "amount": "Amount must be at least ₹1.00"
  }
}
```

### Validation Rules

| Field | Rule | Example |
|-------|------|---------|
| **Name** | 3-50 characters, alphabets and spaces only | "John Doe" |
| **Email** | Valid email format | "john@example.com" |
| **Contact** | Exactly 10 digits | "9876543210" |
| **Amount** | Decimal with 2 places, ₹1.00 - ₹100,000.00 | "1500.00" |

### 2. Get All Payments
**Endpoint:** `GET /api/payments`

**Response:**
```json
{
  "success": true,
  "message": "Payments retrieved successfully"
}
```

### 3. Get Payment by ID
**Endpoint:** `GET /api/payments/:id`

**Example:** `GET /api/payments/123`

**Response:**
```json
{
  "success": true,
  "message": "Payment retrieved successfully",
  "data": {
    "id": 123,
    "name": "John Doe",
    "email": "john@example.com",
    "contact": "9876543210",
    "amount": 1500.00,
    "status": "success",
    "createdAt": "2026-02-12 10:30:45"
  }
}
```

### 4. Get Payments by Status
**Endpoint:** `GET /api/payments/status/:status`

**Example:** `GET /api/payments/status/success`

**Status Values:** `pending`, `success`, `failed`

### 5. Health Check
**Endpoint:** `GET /api/health`

**Response:**
```json
{
  "success": true,
  "message": "Payment Processing API is running"
}
```

## Database Schema

### payments Table

```sql
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT 'Customer full name',
    email VARCHAR(100) NOT NULL COMMENT 'Customer email address',
    contact VARCHAR(10) NOT NULL COMMENT 'Customer contact number (10 digits)',
    amount DECIMAL(10, 2) NOT NULL COMMENT 'Payment amount in rupees',
    status VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT 'Payment status: pending, success, failed',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Payment creation timestamp',
    updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last updated timestamp',
    INDEX idx_email (email),
    INDEX idx_status (status)
);
```

### Schema Diagram

```
┌─────────────────────────────────────┐
│          PAYMENTS TABLE             │
├─────────────────────────────────────┤
│ id (PK)           | BIGINT          │
│ name              | VARCHAR(50)     │
│ email             | VARCHAR(100)    │
│ contact           | VARCHAR(10)     │
│ amount            | DECIMAL(10,2)   │
│ status            | VARCHAR(20)     │
│ created_at        | TIMESTAMP       │
│ updated_at        | TIMESTAMP       │
│                   |                 │
│ Indexes:          |                 │
│ - idx_email       |                 │
│ - idx_status      |                 │
└─────────────────────────────────────┘
```

## Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test
```bash
mvn test -Dtest=PaymentServiceTest
```

### Test Coverage
The project includes unit tests for:
- ✅ Valid payment processing
- ✅ XSS sanitization
- ✅ Data validation
- ✅ Entity conversion
- ✅ Error handling

### Test Output
```
[INFO] Running com.payment.service.PaymentServiceTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
```

## Error Handling

### Validation Errors (400 Bad Request)
```json
{
  "success": false,
  "message": "Validation failed",
  "errors": {
    "name": "Name must be between 3 and 50 characters",
    "email": "Invalid email format"
  }
}
```

### Not Found Error (404)
```json
{
  "success": false,
  "message": "Payment not found with ID: 999"
}
```

### Server Error (500)
```json
{
  "success": false,
  "message": "An unexpected error occurred: [error details]"
}
```

## Security Features

### 1. Input Validation
- **Client-side:** Real-time validation with user feedback
- **Server-side:** Annotation-based validation (@NotBlank, @Email, @Pattern, etc.)

### 2. XSS Prevention
- Input sanitization in service layer
- HTML entity encoding for special characters
- Content Security Policy headers ready

### 3. SQL Injection Prevention
- JPA/Hibernate parameterized queries
- No raw SQL queries in the code

### 4. CORS Configuration
- Configured for localhost development
- Easily extensible for production domains

### 5. Data Integrity
- Required field validation
- Proper data type constraints
- Timestamp tracking for audit

## Screenshots

### Payment Form
The application displays a modern, responsive payment form with:
- Clean gradient background
- Real-time validation feedback
- Error highlighting
- Success message display
- Payment details summary

### Form Features
- ✨ Responsive design (works on mobile, tablet, desktop)
- 🎨 Modern styling with smooth transitions
- ✓ Real-time validation with error messages
- 📱 Touch-friendly input fields
- 🎯 Clear visual feedback for errors

## Troubleshooting

### Issue: Connection refused to MySQL
```
Error: Communications link failure
```
**Solution:**
1. Check if MySQL server is running
2. Verify connection details in `application.properties`
3. Ensure database exists: `CREATE DATABASE payment_db;`

### Issue: Port 8080 already in use
```
Error: Address already in use
```
**Solution:**
Change port in `application.properties`:
```properties
server.port=8081
```

### Issue: Cannot create table
**Solution:**
1. Drop and recreate the database:
   ```sql
   DROP DATABASE IF EXISTS payment_db;
   CREATE DATABASE payment_db;
   ```
2. Set `spring.jpa.hibernate.ddl-auto=create`
3. Restart the application

### Issue: Frontend cannot connect to backend
```
Error: Failed to connect to payment server
```
**Solution:**
1. Ensure backend is running on `http://localhost:8080`
2. Check CORS configuration
3. Open browser console (F12) for detailed errors

### Issue: Validation errors not showing
**Solution:**
1. Check browser console for JavaScript errors
2. Verify network request in Network tab
3. Check server logs for validation details

## API Testing with cURL

### Test Payment Submission
```bash
curl -X POST http://localhost:8080/api/payment \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane@example.com",
    "contact": "8765432109",
    "amount": 2500.00
  }'
```

### Test Invalid Data
```bash
curl -X POST http://localhost:8080/api/payment \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jo",
    "email": "invalid-email",
    "contact": "123",
    "amount": 100
  }'
```

### Get All Payments
```bash
curl http://localhost:8080/api/payments
```

### Check Health
```bash
curl http://localhost:8080/api/health
```

## Production Considerations

For production deployment, consider:
1. Environment-specific properties files
2. Database connection pooling (HikariCP)
3. HTTPS/SSL configuration
4. Rate limiting
5. JWT authentication
6. Database backups
7. Monitoring and logging
8. Docker containerization

## Development Notes

### Key Classes

**PaymentProcessingMvpApplication.java**
- Main Spring Boot application entry point
- CORS configuration

**PaymentController.java**
- REST API endpoints
- Request/response handling
- Error responses

**PaymentService.java**
- Business logic
- Input sanitization
- Database operations

**Payment.java**
- JPA entity
- Database table mapping
- Auto-timestamp management

**PaymentRequest.java**
- Request DTO with validation annotations
- Client data validation

**GlobalExceptionHandler.java**
- Centralized exception handling
- Validation error formatting

## Learning Outcomes

This project demonstrates:
- ✅ REST API design principles
- ✅ Full-stack development workflow
- ✅ Form validation (client & server)
- ✅ Database design and operations
- ✅ Error handling and user feedback
- ✅ Security best practices
- ✅ Code organization and architecture
- ✅ Testing practices

## License

This project is created as an assignment for full-stack developer evaluation.

## Support

For issues or questions:
1. Check the Troubleshooting section
2. Review the API Documentation
3. Check browser console (F12) for client-side errors
4. Check server logs for backend errors

---

**Created:** February 12, 2026  
**Status:** Complete and Functional ✅
