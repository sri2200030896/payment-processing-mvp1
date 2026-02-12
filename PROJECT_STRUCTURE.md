# Payment Processing MVP - Project Structure

```
payment-processing-mvp/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/payment/
│   │   │       ├── PaymentProcessingMvpApplication.java      [Main Spring Boot Application]
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   └── PaymentController.java                [REST API Endpoints]
│   │   │       │       - POST   /api/payment
│   │   │       │       - GET    /api/payments
│   │   │       │       - GET    /api/payments/:id
│   │   │       │       - GET    /api/payments/status/:status
│   │   │       │       - GET    /api/health
│   │   │       │
│   │   │       ├── service/
│   │   │       │   └── PaymentService.java                   [Business Logic Layer]
│   │   │       │       - processPayment()
│   │   │       │       - getAllPayments()
│   │   │       │       - getPaymentById()
│   │   │       │       - getPaymentsByStatus()
│   │   │       │       - sanitizeInput() [XSS Prevention]
│   │   │       │
│   │   │       ├── entity/
│   │   │       │   └── Payment.java                          [JPA Entity - Database Table]
│   │   │       │       - id (PK)
│   │   │       │       - name, email, contact
│   │   │       │       - amount, status
│   │   │       │       - createdAt, updatedAt
│   │   │       │
│   │   │       ├── dto/
│   │   │       │   ├── PaymentRequest.java                   [Request DTO with Validation]
│   │   │       │   ├── PaymentResponse.java                  [Response DTO]
│   │   │       │   └── ApiResponse.java                      [Standard API Response]
│   │   │       │
│   │   │       ├── repository/
│   │   │       │   └── PaymentRepository.java                [Database Access Layer]
│   │   │       │
│   │   │       └── exception/
│   │   │           └── GlobalExceptionHandler.java           [Centralized Exception Handling]
│   │   │
│   │   └── resources/
│   │       ├── application.properties                        [Spring Boot Configuration]
│   │       └── static/
│   │           └── index.html                                [Frontend - Payment Form]
│   │               - Responsive Design
│   │               - Real-time Validation
│   │               - Success/Error Messages
│   │               - Payment Details Display
│   │
│   └── test/
│       └── java/
│           └── com/payment/
│               ├── service/
│               │   └── PaymentServiceTest.java               [Service Layer Tests]
│               ├── dto/
│               │   └── PaymentRequestValidationTest.java     [Validation Tests]
│               └── controller/
│                   └── PaymentControllerTest.java            [Integration Tests]
│
├── pom.xml                                                   [Maven Dependencies & Build Config]
├── README.md                                                 [Comprehensive Documentation]
├── QUICKSTART.md                                             [Quick Start Guide]
├── .env.example                                              [Environment Variables Template]
├── .gitignore                                                [Git Ignore Rules]
├── Dockerfile                                                [Docker Image Configuration]
├── docker-compose.yml                                        [Docker Compose for MySQL + API]
└── Payment_Processing_MVP.postman_collection.json            [Postman API Tests]
```

## Key Files Explanation

### Backend Files

| File | Purpose |
|------|---------|
| `PaymentProcessingMvpApplication.java` | Spring Boot main application class with CORS config |
| `PaymentController.java` | REST endpoints, request/response handling |
| `PaymentService.java` | Business logic, validation, sanitization |
| `Payment.java` | JPA entity with database annotations |
| `PaymentRequest.java` | DTO with validation constraints |
| `PaymentRepository.java` | Database query methods |
| `GlobalExceptionHandler.java` | Centralized error handling |

### Frontend Files

| File | Purpose |
|------|---------|
| `index.html` | Payment form with styling and JavaScript |
| Includes | Validation, API calls, error handling |

### Configuration Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven dependencies (Spring Boot, MySQL, Testing) |
| `application.properties` | Spring Boot configuration (DB, server, logging) |
| `.env.example` | Environment variables template |
| `.gitignore` | Git ignore patterns |

### Testing Files

| File | Purpose |
|------|---------|
| `PaymentServiceTest.java` | Service layer unit tests |
| `PaymentRequestValidationTest.java` | DTO validation tests (14 test cases) |
| `PaymentControllerTest.java` | Controller integration tests |

### Deployment Files

| File | Purpose |
|------|---------|
| `Dockerfile` | Docker image for API |
| `docker-compose.yml` | Docker Compose for MySQL + API |

### Documentation Files

| File | Purpose |
|------|---------|
| `README.md` | Complete documentation (setup, API, schema) |
| `QUICKSTART.md` | Quick start guide (5 minutes) |
| `Payment_Processing_MVP.postman_collection.json` | Postman collection for API testing |

## API Endpoints Summary

```
GET    /api/health                      ✓ Health check
POST   /api/payment                     ✓ Process payment
GET    /api/payments                    ✓ Get all payments
GET    /api/payments/:id                ✓ Get payment by ID
GET    /api/payments/status/:status     ✓ Get payments by status
```

## Database Schema

```sql
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

## Technology Stack Summary

### Backend
- Spring Boot 3.2.0 (Java 17)
- JPA/Hibernate ORM
- MySQL 8.0 Database
- Maven Build Tool

### Frontend
- HTML5 (Semantic)
- CSS3 (Responsive, Modern Design)
- Vanilla JavaScript (Fetch API)

### Testing
- JUnit 5
- Mockito
- Spring Test

### Security
- Input Validation (Client + Server)
- XSS Prevention (HTML entity encoding)
- SQL Injection Prevention (Parameterized queries)
- CORS Configuration

---

**Status:** ✅ Complete and Functional  
**Created:** February 12, 2026
