#!/bin/bash

# Define colors for better output readability
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Starting CDR Service Setup...${NC}"

# Step 1: Clean and build the project
echo -e "${GREEN}🔨 Building the project with Maven...${NC}"
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Maven build failed!${NC}"
    exit 1
fi

# Step 2: Build and run Docker containers
echo -e "${GREEN}🐳 Starting Docker containers...${NC}"
docker compose up --build -d

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Docker startup failed!${NC}"
    exit 1
fi

# Step 3: Check if PostgreSQL is running
echo -e "${GREEN}📡 Waiting for PostgreSQL to start...${NC}"
until docker exec $(docker ps -qf "name=db") pg_isready -U user -d cdrdb; do
    sleep 2
done
echo -e "${GREEN}✅ PostgreSQL is running!${NC}"

# Step 4: Check if the application is running
echo -e "${GREEN}⏳ Waiting for the CDR Service to start...${NC}"
until curl -s http://localhost:8080/actuator/health | grep -q "UP"; do
    sleep 5
done
echo -e "${GREEN}✅ CDR Service is running!${NC}"

# Step 5: Deploy to Kubernetes (if enabled)
read -p "Would you like to deploy to Kubernetes? (y/n): " deploy_k8s
if [[ "$deploy_k8s" == "y" || "$deploy_k8s" == "Y" ]]; then
    echo -e "${GREEN}☸️ Deploying to Kubernetes...${NC}"
    kubectl apply -f k8s/
    echo -e "${GREEN}✅ Kubernetes deployment completed!${NC}"
fi

# Step 6: Open Swagger documentation in browser
echo -e "${GREEN}🌍 Opening Swagger API documentation...${NC}"
xdg-open http://localhost:8080/swagger-ui.html || open http://localhost:8080/swagger-ui.html || echo "Please open http://localhost:8080/swagger-ui.html manually"

echo -e "${GREEN}🎉 CDR Service is up and running!${NC}"
