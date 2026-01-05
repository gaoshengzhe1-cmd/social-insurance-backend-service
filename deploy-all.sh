#!/bin/bash

# Social Insurance Services Deployment Script
# This script builds and deploys all three microservices to Google Cloud Run

set -e

# Configuration
PROJECT_ID=${GOOGLE_CLOUD_PROJECT:-"your-project-id"}
REGION=${REGION:-"asia-northeast1"}
DB_HOST=${DB_HOST:-"localhost"}

echo "Starting deployment of Social Insurance Services..."
echo "Project: $PROJECT_ID"
echo "Region: $REGION"
echo "Database Host: $DB_HOST"

# Function to build and deploy a service
deploy_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    local db_name=$4
    local db_user=$5
    
    echo "=========================================="
    echo "Deploying $service_name"
    echo "=========================================="
    
    cd "$service_dir"
    
    # Build Docker image
    echo "Building Docker image for $service_name..."
    docker build -t "$service_name" .
    
    # Tag for Google Container Registry
    echo "Tagging image for GCR..."
    docker tag "$service_name" "gcr.io/$PROJECT_ID/$service_name"
    
    # Push to GCR
    echo "Pushing image to GCR..."
    docker push "gcr.io/$PROJECT_ID/$service_name"
    
    # Deploy to Cloud Run
    echo "Deploying to Cloud Run..."
    gcloud run deploy "$service_name" \
        --image "gcr.io/$PROJECT_ID/$service_name" \
        --platform managed \
        --region "$REGION" \
        --allow-unauthenticated \
        --port "$port" \
        --set-env-vars "DB_URL=r2dbc:postgresql://$DB_HOST:5432/$db_name" \
        --set-env-vars "DB_USER=$db_user" \
        --set-env-vars "DB_PASSWORD=$DB_PASSWORD" \
        --set-env-vars "FLYWAY_URL=jdbc:postgresql://$DB_HOST:5432/$db_name" \
        --quiet
    
    echo "$service_name deployed successfully!"
    
    # Get service URL
    SERVICE_URL=$(gcloud run services describe "$service_name" --region "$REGION" --format="value(status.url)")
    echo "Service URL: $SERVICE_URL"
    
    cd ..
}

# Check if required environment variables are set
if [ -z "$DB_PASSWORD" ]; then
    echo "Error: DB_PASSWORD environment variable is required"
    exit 1
fi

# Deploy all services
deploy_service "social-insurance-service" "social-insurance-service" 9003 "social_insurance_health" "social_insurance_health_app"
deploy_service "employment-insurance-service" "employment-insurance-service" 9004 "employment_insurance" "employment_insurance_app"
deploy_service "pension-insurance-service" "pension-insurance-service" 9005 "pension_insurance" "pension_insurance_app"

echo "=========================================="
echo "Deployment Complete!"
echo "=========================================="

# Display all service URLs
echo ""
echo "Service URLs:"
echo "=========================================="

SOCIAL_INSURANCE_URL=$(gcloud run services describe social-insurance-service --region "$REGION" --format="value(status.url)")
EMPLOYMENT_INSURANCE_URL=$(gcloud run services describe employment-insurance-service --region "$REGION" --format="value(status.url)")
PENSION_INSURANCE_URL=$(gcloud run services describe pension-insurance-service --region "$REGION" --format="value(status.url)")

echo "Social Insurance Service: $SOCIAL_INSURANCE_URL"
echo "Employment Insurance Service: $EMPLOYMENT_INSURANCE_URL"
echo "Pension Insurance Service: $PENSION_INSURANCE_URL"

echo ""
echo "Test Commands:"
echo "=========================================="
echo "# Health Insurance"
echo "curl \"$SOCIAL_INSURANCE_URL/health-insurance/calculate?monthlySalary=200000&age=35\""
echo ""
echo "# Employment Insurance"
echo "curl \"$EMPLOYMENT_INSURANCE_URL/employment-insurance/calculate?monthlySalary=200000&employmentType=GENERAL\""
echo ""
echo "# Pension Insurance"
echo "curl \"$PENSION_INSURANCE_URL/pension-insurance/calculate?monthlySalary=200000\""

echo ""
echo "Deployment completed successfully!"
