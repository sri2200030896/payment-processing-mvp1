# Quick Start Guide

Get the Payment Processing MVP up and running in 5 minutes!

## Prerequisites
- Java 17+
- Maven 3.8.1+
- MySQL 8.0+

## Step 1: Database Setup (2 minutes)

Open MySQL and run:
```sql
CREATE DATABASE payment_db;
```

## Step 2: Configure Connection (1 minute)

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/payment_db
spring.datasource.username=root
spring.datasource.password=root
```

Change `root` password if different.

## Step 3: Build & Run (2 minutes)

```bash
# Navigate to project
cd /Users/vijaykadarla/Desktop/payment-processing-mvp

# Build
mvn clean install

# Run
mvn spring-boot:run
```

## Step 4: Access Application

Open browser: **http://localhost:8080**

## That's It! 🎉

The application is now running with:
- ✅ REST API on http://localhost:8080/api
- ✅ Frontend form on http://localhost:8080
- ✅ MySQL database connected
- ✅ Full validation and error handling

## Quick Test

### Submit a Payment (in browser)
1. Fill the form with valid data
2. Click "Pay Now"
3. See success message with Payment ID

### Test via cURL
```bash
curl -X POST http://localhost:8080/api/payment \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "contact": "9876543210",
    "amount": 1500.00
  }'
```

## Troubleshooting

**MySQL not connecting?**
- Check MySQL is running: `mysql --version`
- Verify database exists: `USE payment_db;`
- Update password in application.properties

**Port 8080 in use?**
- Edit `application.properties`: `server.port=8081`

**Build failing?**
- Clear cache: `mvn clean`
- Update Maven: `mvn --version`
- Check Java 17: `java -version`

## Next Steps

- Review [README.md](README.md) for full documentation
- Check [API Documentation](README.md#api-documentation) for endpoints
- Run tests: `mvn test`
- Explore code structure: [Project Structure](README.md#project-structure)

---

**Need Help?** See Troubleshooting in [README.md](README.md#troubleshooting)
