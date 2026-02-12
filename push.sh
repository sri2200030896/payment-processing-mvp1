#!/bin/bash

# Color codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}🚀 GitHub Push Helper - Payment Processing MVP${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════════${NC}"

# Check current directory
if [ ! -d ".git" ]; then
    echo -e "${RED}❌ Error: Not in a git repository${NC}"
    exit 1
fi

echo -e "\n${YELLOW}📋 Current Status:${NC}"
echo "Repository: $(pwd)"
echo "Remote: $(git remote -v | grep origin | head -1)"
echo "Branch: $(git branch --show-current)"
echo "Commits: $(git log --oneline | wc -l)"

echo -e "\n${YELLOW}🔑 To push your code, you need a GitHub Personal Access Token:${NC}"
echo -e "\n${BLUE}Step 1: Go to https://github.com/settings/tokens${NC}"
echo "Step 2: Click 'Generate new token' → 'Generate new token (classic)'"
echo "Step 3: Name: 'payment-processing-mvp-push'"
echo "Step 4: Select scope: ☑️  repo"
echo "Step 5: Click 'Generate token' and COPY the token"
echo -e "\n${BLUE}Step 6: Run this command and paste the token when prompted:${NC}\n"
echo -e "${GREEN}git push -u origin main${NC}\n"

echo -e "${YELLOW}Credentials have been cleared from macOS Keychain.${NC}"
echo -e "${YELLOW}When prompted for 'username', enter: sri2200030896${NC}"
echo -e "${YELLOW}When prompted for 'password', paste your Personal Access Token${NC}"

echo -e "\n${BLUE}Ready to push? (y/n)${NC} "
read -r response

if [[ "$response" =~ ^[Yy]$ ]]; then
    echo -e "\n${GREEN}Attempting to push...${NC}\n"
    cd /Users/vijaykadarla/Desktop/payment-processing-mvp
    git push -u origin main
    
    if [ $? -eq 0 ]; then
        echo -e "\n${GREEN}✅ Success! Your code is now at:${NC}"
        echo -e "${GREEN}https://github.com/sri2200030896/payment-processing-mvp${NC}"
    else
        echo -e "\n${RED}❌ Push failed. Make sure your token is valid.${NC}"
    fi
else
    echo -e "\n${YELLOW}Push cancelled.${NC}"
fi
