# Use official Maven image as builder
FROM maven:3.8.4-openjdk-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build application
RUN mvn clean package -DskipTests

# Use official Java runtime as base image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/target/payment-processing-mvp-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD java -cp app.jar org.springframework.boot.loader.JarLauncher && curl -f http://localhost:8080/api/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
