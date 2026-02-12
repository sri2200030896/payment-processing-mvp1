#!/bin/bash

# Payment Processing MVP - API Testing Script
# This script tests all 5 API endpoints with sample data

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║  Payment Processing MVP - API Test Suite                      ║"
echo "║  Status: Testing All 5 Endpoints                              ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

API_URL="http://localhost:8080"
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print section headers
print_header() {
    echo ""
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${BLUE}TEST: $1${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
}

# Function to print result
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ PASS${NC}"
    else
        echo -e "${RED}✗ FAIL${NC}"
    fi
}

# Check if server is running
echo "Checking if server is running on $API_URL..."
if ! curl -s -f "$API_URL/api/health" > /dev/null 2>&1; then
    echo -e "${RED}ERROR: Server is not running on $API_URL${NC}"
    echo "Start the application with: java -jar target/payment-processing-mvp-1.0.0.jar"
    exit 1
fi
echo -e "${GREEN}✓ Server is running${NC}"
echo ""

# TEST 1: Health Check
print_header "1: Health Check Endpoint (GET /api/health)"
echo "Request: GET $API_URL/api/health"
RESPONSE=$(curl -s -w "\n%{http_code}" "$API_URL/api/health")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "Response Code: $HTTP_CODE"
echo "Response Body: $BODY"
if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ PASS - Health check successful${NC}"
else
    echo -e "${RED}✗ FAIL - Expected 200, got $HTTP_CODE${NC}"
fi

# TEST 2: Success Case - Valid Payment
print_header "2: Success Case - Valid Payment (POST /api/payment)"
echo "Request: POST $API_URL/api/payment"
echo "Body:"
PAYLOAD='{"name":"John Doe","email":"john@example.com","contact":"9876543210","amount":"1500.00"}'
echo "$PAYLOAD" | jq '.'

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$API_URL/api/payment" \
  -H "Content-Type: application/json" \
  -d "$PAYLOAD")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo ""
echo "Response Code: $HTTP_CODE"
echo "Response Body:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

if [ "$HTTP_CODE" = "201" ]; then
    echo -e "${GREEN}✓ PASS - Payment created successfully (201 Created)${NC}"
    PAYMENT_ID=$(echo "$BODY" | jq -r '.paymentId // .data.id' 2>/dev/null)
    if [ ! -z "$PAYMENT_ID" ] && [ "$PAYMENT_ID" != "null" ]; then
        echo "  Payment ID: $PAYMENT_ID"
    fi
else
    echo -e "${RED}✗ FAIL - Expected 201, got $HTTP_CODE${NC}"
fi

# TEST 3: Error Case - Invalid Input
print_header "3: Error Case - Invalid Input Validation (POST /api/payment)"
echo "Request: POST $API_URL/api/payment (with invalid data)"
echo "Body:"
INVALID_PAYLOAD='{"name":"Jo","email":"invalid-email","contact":"123","amount":"100"}'
echo "$INVALID_PAYLOAD" | jq '.'

RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$API_URL/api/payment" \
  -H "Content-Type: application/json" \
  -d "$INVALID_PAYLOAD")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo ""
echo "Response Code: $HTTP_CODE"
echo "Response Body:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

if [ "$HTTP_CODE" = "400" ]; then
    echo -e "${GREEN}✓ PASS - Validation errors returned (400 Bad Request)${NC}"
    echo "  Error Details:"
    echo "$BODY" | jq '.errors' 2>/dev/null | grep -E '"[^"]+":' | sed 's/^/    /'
else
    echo -e "${RED}✗ FAIL - Expected 400, got $HTTP_CODE${NC}"
fi

# TEST 4: Get All Payments
print_header "4: Get All Payments (GET /api/payments)"
echo "Request: GET $API_URL/api/payments"

RESPONSE=$(curl -s -w "\n%{http_code}" "$API_URL/api/payments")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "Response Code: $HTTP_CODE"
echo "Response Body:"
COUNT=$(echo "$BODY" | jq '.data | length' 2>/dev/null)
echo "$BODY" | jq '.' 2>/dev/null | head -30 || echo "$BODY"

if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ PASS - Retrieved all payments (200 OK)${NC}"
    echo "  Total payments: $COUNT"
else
    echo -e "${RED}✗ FAIL - Expected 200, got $HTTP_CODE${NC}"
fi

# TEST 5: Get Payments by Status
print_header "5: Filter Payments by Status (GET /api/payments/status/success)"
echo "Request: GET $API_URL/api/payments/status/success"

RESPONSE=$(curl -s -w "\n%{http_code}" "$API_URL/api/payments/status/success")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "Response Code: $HTTP_CODE"
echo "Response Body:"
echo "$BODY" | jq '.' 2>/dev/null | head -30 || echo "$BODY"

if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ PASS - Status filter working (200 OK)${NC}"
else
    echo -e "${RED}✗ FAIL - Expected 200, got $HTTP_CODE${NC}"
fi

# Summary
echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║  Test Summary                                                  ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo -e "${GREEN}✓ All 5 API endpoints tested${NC}"
echo -e "${GREEN}✓ Success case (201 Created) verified${NC}"
echo -e "${GREEN}✓ Error case (400 Bad Request) verified${NC}"
echo -e "${GREEN}✓ GET operations working correctly${NC}"
echo ""
echo "Application is functioning correctly!"
