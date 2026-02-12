#!/bin/bash

# Payment Processing MVP - Installation Verification Script
# This script verifies all system requirements and project setup

echo "=================================================="
echo "Payment Processing MVP - Setup Verification"
echo "=================================================="
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Counter for checks
PASSED=0
FAILED=0

# Function to check command
check_command() {
    local cmd=$1
    local name=$2
    
    if command -v $cmd &> /dev/null; then
        echo -e "${GREEN}✓${NC} $name is installed"
        ((PASSED++))
        return 0
    else
        echo -e "${RED}✗${NC} $name is NOT installed"
        ((FAILED++))
        return 1
    fi
}

# Function to check file
check_file() {
    local file=$1
    local name=$2
    
    if [ -f "$file" ]; then
        echo -e "${GREEN}✓${NC} $name exists"
        ((PASSED++))
        return 0
    else
        echo -e "${RED}✗${NC} $name does NOT exist"
        ((FAILED++))
        return 1
    fi
}

# Function to get version
get_version() {
    local cmd=$1
    local format=$2
    
    if command -v $cmd &> /dev/null; then
        echo "  Version: $($cmd $format 2>&1 | head -1)"
    fi
}

echo "1. Checking System Requirements..."
echo "================================="

# Check Java
check_command "java" "Java"
get_version "java" "-version"

# Check Maven
check_command "mvn" "Maven"
get_version "mvn" "-version"

# Check MySQL
check_command "mysql" "MySQL Client"
get_version "mysql" "-V"

echo ""
echo "2. Checking Project Files..."
echo "================================="

# Check important files
check_file "pom.xml" "pom.xml"
check_file "README.md" "README.md"
check_file "QUICKSTART.md" "QUICKSTART.md"
check_file "src/main/resources/application.properties" "application.properties"
check_file "src/main/resources/static/index.html" "index.html"

echo ""
echo "3. Checking Source Code Files..."
echo "================================="

# Check Java files
JAVA_FILES=(
    "src/main/java/com/payment/PaymentProcessingMvpApplication.java"
    "src/main/java/com/payment/controller/PaymentController.java"
    "src/main/java/com/payment/service/PaymentService.java"
    "src/main/java/com/payment/entity/Payment.java"
    "src/main/java/com/payment/dto/PaymentRequest.java"
    "src/main/java/com/payment/dto/PaymentResponse.java"
    "src/main/java/com/payment/repository/PaymentRepository.java"
    "src/main/java/com/payment/exception/GlobalExceptionHandler.java"
)

for file in "${JAVA_FILES[@]}"; do
    filename=$(basename "$file")
    check_file "$file" "$filename"
done

echo ""
echo "4. Checking Test Files..."
echo "================================="

# Check test files
TEST_FILES=(
    "src/test/java/com/payment/service/PaymentServiceTest.java"
    "src/test/java/com/payment/dto/PaymentRequestValidationTest.java"
    "src/test/java/com/payment/controller/PaymentControllerTest.java"
)

for file in "${TEST_FILES[@]}"; do
    filename=$(basename "$file")
    check_file "$file" "$filename"
done

echo ""
echo "5. Checking Docker Files..."
echo "================================="

check_file "Dockerfile" "Dockerfile"
check_file "docker-compose.yml" "docker-compose.yml"

echo ""
echo "6. Checking Documentation..."
echo "================================="

check_file "README.md" "README.md"
check_file "QUICKSTART.md" "QUICKSTART.md"
check_file "PROJECT_STRUCTURE.md" "PROJECT_STRUCTURE.md"
check_file "DEPLOYMENT.md" "DEPLOYMENT.md"
check_file "IMPLEMENTATION_SUMMARY.md" "IMPLEMENTATION_SUMMARY.md"
check_file "Payment_Processing_MVP.postman_collection.json" "Postman Collection"

echo ""
echo "7. Checking Configuration Files..."
echo "================================="

check_file ".env.example" ".env.example"
check_file ".gitignore" ".gitignore"

echo ""
echo "=================================================="
echo "Verification Summary"
echo "=================================================="
echo -e "${GREEN}✓ Passed: $PASSED${NC}"
echo -e "${RED}✗ Failed: $FAILED${NC}"
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}All checks passed! ✓${NC}"
    echo ""
    echo "Next steps:"
    echo "1. Read QUICKSTART.md for quick setup"
    echo "2. Create MySQL database: CREATE DATABASE payment_db;"
    echo "3. Configure application.properties with your DB credentials"
    echo "4. Run: mvn clean install && mvn spring-boot:run"
    echo ""
    exit 0
else
    echo -e "${RED}Some checks failed! ✗${NC}"
    echo ""
    echo "Please install missing components:"
    echo "- Java 17+: https://www.oracle.com/java/technologies/downloads/"
    echo "- Maven 3.8.1+: https://maven.apache.org/download.cgi"
    echo "- MySQL 8.0+: https://dev.mysql.com/downloads/mysql/"
    echo ""
    exit 1
fi
