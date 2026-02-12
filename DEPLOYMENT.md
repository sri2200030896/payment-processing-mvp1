# Deployment Guide

Complete guide for deploying the Payment Processing MVP to production.

## Table of Contents
1. [Local Development](#local-development)
2. [Docker Deployment](#docker-deployment)
3. [Production Deployment](#production-deployment)
4. [Environment Configuration](#environment-configuration)
5. [Database Migration](#database-migration)

## Local Development

### Prerequisites
- Java 17+
- Maven 3.8.1+
- MySQL 8.0+

### Setup Steps

1. **Clone the Repository**
```bash
cd /Users/vijaykadarla/Desktop/payment-processing-mvp
```

2. **Create Database**
```sql
CREATE DATABASE payment_db;
```

3. **Configure Properties**
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/payment_db
spring.datasource.username=root
spring.datasource.password=root
```

4. **Build Application**
```bash
mvn clean install
```

5. **Run Application**
```bash
mvn spring-boot:run
```

6. **Access Application**
- Frontend: http://localhost:8080
- API: http://localhost:8080/api
- Health: http://localhost:8080/api/health

## Docker Deployment

### Prerequisites
- Docker 20.10+
- Docker Compose 2.0+

### Setup Steps

1. **Build and Run with Docker Compose**
```bash
docker-compose up -d
```

This will:
- Create MySQL 8.0 container
- Build and run Spring Boot application
- Set up networking and health checks

2. **Verify Services**
```bash
# Check running containers
docker-compose ps

# View logs
docker-compose logs api
docker-compose logs mysql

# Health check
curl http://localhost:8080/api/health
```

3. **Access Application**
- Frontend: http://localhost:8080
- API: http://localhost:8080/api
- MySQL: localhost:3306

4. **Stop Services**
```bash
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Docker Commands

```bash
# Build images
docker-compose build

# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose stop

# Remove services
docker-compose down

# Remove all data
docker-compose down -v
```

## Production Deployment

### Platform-Specific Deployment

#### 1. Heroku Deployment

**Prerequisites**
- Heroku CLI installed
- Heroku account
- GitHub repository

**Setup**
```bash
# Login to Heroku
heroku login

# Create Heroku app
heroku create payment-processing-mvp

# Set environment variables
heroku config:set SPRING_DATASOURCE_URL=your_db_url
heroku config:set SPRING_DATASOURCE_USERNAME=your_user
heroku config:set SPRING_DATASOURCE_PASSWORD=your_password

# Deploy
git push heroku main
```

#### 2. AWS EC2 Deployment

**Prerequisites**
- AWS account
- EC2 instance running Ubuntu 20.04+
- Java 17 installed on instance

**Setup**
```bash
# SSH into instance
ssh -i your-key.pem ubuntu@your-instance-ip

# Install MySQL
sudo apt-get update
sudo apt-get install mysql-server

# Install Java
sudo apt-get install openjdk-17-jdk

# Clone repository
git clone your-repo-url
cd payment-processing-mvp

# Build
mvn clean package

# Run with systemd
sudo cp target/payment-processing-mvp-1.0.0.jar /opt/payment-api.jar
sudo systemctl restart payment-api
```

**Systemd Service File**
```ini
# /etc/systemd/system/payment-api.service
[Unit]
Description=Payment Processing API
After=network.target

[Service]
Type=simple
User=ubuntu
WorkingDirectory=/opt
ExecStart=/usr/bin/java -jar /opt/payment-api.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

#### 3. Azure App Service Deployment

**Prerequisites**
- Azure account
- Azure CLI installed

**Setup**
```bash
# Login to Azure
az login

# Create resource group
az group create --name payment-rg --location eastus

# Create App Service plan
az appservice plan create --name payment-plan --resource-group payment-rg --sku B2 --is-linux

# Deploy
mvn azure-webapp:deploy
```

#### 4. DigitalOcean Deployment

**Prerequisites**
- DigitalOcean account
- Droplet (Ubuntu 20.04+)

**Setup**
```bash
# SSH into droplet
ssh root@your-droplet-ip

# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Clone and deploy
git clone your-repo-url
cd payment-processing-mvp
docker-compose up -d
```

## Environment Configuration

### Development Environment
```properties
# src/main/resources/application-dev.properties
spring.datasource.url=jdbc:mysql://localhost:3306/payment_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
logging.level.root=INFO
logging.level.com.payment=DEBUG
```

### Production Environment
```properties
# src/main/resources/application-prod.properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.root=WARN
logging.level.com.payment=INFO
server.ssl.enabled=true
server.ssl.key-store=${SSL_KEYSTORE}
server.ssl.key-store-password=${SSL_PASSWORD}
```

### Run with Specific Profile
```bash
# Development
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Production
java -jar payment-processing-mvp-1.0.0.jar --spring.profiles.active=prod
```

## Database Migration

### Initial Setup
```sql
-- Create database
CREATE DATABASE payment_db;

-- Hibernate will auto-create tables when set to 'update' mode
-- Or manually create if needed:
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

### Backup Database
```bash
# Dump database
mysqldump -u root -p payment_db > backup_$(date +%Y%m%d).sql

# Restore database
mysql -u root -p payment_db < backup_20260212.sql
```

### Database Maintenance
```bash
# Optimize tables
OPTIMIZE TABLE payments;

# Analyze tables
ANALYZE TABLE payments;

# Check table integrity
CHECK TABLE payments;
```

## Monitoring & Logging

### Application Monitoring
```bash
# Check application logs
tail -f logs/app.log

# Monitor JVM
jps -l

# Monitor resource usage
top -p $(pgrep -f payment-processing)
```

### Logging Configuration
```properties
# src/main/resources/logback-spring.xml
logging.file.name=logs/app.log
logging.file.max-size=10MB
logging.file.max-history=10
logging.level.root=WARN
logging.level.com.payment=INFO
```

## Health Checks

### Application Health
```bash
curl http://localhost:8080/api/health
```

### Docker Health
```bash
docker-compose ps
```

## Performance Optimization

### Database Optimization
- Add indexes on frequently queried columns (done)
- Use connection pooling (HikariCP)
- Enable query caching

### Application Optimization
- Use response caching
- Implement pagination for list endpoints
- Add request rate limiting
- Compress responses with gzip

### Monitoring
- Set up application performance monitoring (APM)
- Enable distributed tracing
- Monitor database query performance

## Security for Production

### HTTPS/SSL
```bash
# Generate self-signed certificate
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650

# Configure in application.properties
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your-password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=tomcat
```

### WAF & DDoS Protection
- Use AWS WAF
- Enable DDoS protection
- Configure rate limiting

### Database Security
- Use strong passwords
- Enable SSL for database connections
- Restrict database access by IP
- Regular backups

## Rollback Procedure

### Version Management
```bash
# Tag releases
git tag v1.0.0
git push origin v1.0.0

# Checkout previous version
git checkout v0.9.0
mvn clean package
```

### Database Rollback
```bash
# Restore from backup
mysql -u root -p payment_db < backup_20260211.sql
```

## Troubleshooting Deployment

### Common Issues

**Issue: Connection timeout to database**
```
Solution:
1. Check MySQL server is running
2. Verify network connectivity
3. Check firewall rules
4. Verify database credentials
```

**Issue: Port already in use**
```
Solution:
1. Change port in application.properties
2. Kill process using port: lsof -i :8080
```

**Issue: OutOfMemory error**
```
Solution:
1. Increase JVM heap: -Xmx1024m
2. Optimize database queries
3. Enable garbage collection logging
```

## Maintenance

### Regular Tasks
- Monitor logs daily
- Check database size weekly
- Review performance metrics weekly
- Run backups daily
- Update dependencies monthly
- Security patches as needed

### Updates
```bash
# Update dependencies
mvn clean install

# Update base image
docker pull openjdk:17-jdk-slim

# Rebuild and redeploy
docker-compose up -d --build
```

---

**Last Updated:** February 12, 2026
