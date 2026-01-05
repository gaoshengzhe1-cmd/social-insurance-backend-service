# Social Insurance Services Deployment Guide

## Overview

This project has been split into three independent microservices for separate deployment on Google Cloud Run:

1. **Social Insurance Service** (Port 9003) - Health Insurance calculation
2. **Employment Insurance Service** (Port 9004) - Employment Insurance calculation  
3. **Pension Insurance Service** (Port 9005) - Pension Insurance calculation

Each service has its own:
- Database schema
- Flyway migrations
- Docker configuration
- Build configuration

## Service Endpoints

### Social Insurance Service (Health Insurance)
- **Port**: 9003
- **Endpoint**: `/health-insurance/calculate`
- **Parameters**: `monthlySalary`, `age`
- **Database**: `social_insurance_health`

### Employment Insurance Service
- **Port**: 9004
- **Endpoint**: `/employment-insurance/calculate`
- **Parameters**: `monthlySalary`, `employmentType` (default: GENERAL)
- **Database**: `employment_insurance`

### Pension Insurance Service
- **Port**: 9005
- **Endpoint**: `/pension-insurance/calculate`
- **Parameters**: `monthlySalary`
- **Database**: `pension_insurance`

## Database Setup

Each service requires its own PostgreSQL database:

```sql
-- Create databases
CREATE DATABASE social_insurance_health;
CREATE DATABASE employment_insurance;
CREATE DATABASE pension_insurance;

-- Create users for each service
CREATE USER social_insurance_health_app WITH PASSWORD 'NewStrongPassword123!';
CREATE USER employment_insurance_app WITH PASSWORD 'NewStrongPassword123!';
CREATE USER pension_insurance_app WITH PASSWORD 'NewStrongPassword123!';

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE social_insurance_health TO social_insurance_health_app;
GRANT ALL PRIVILEGES ON DATABASE employment_insurance TO employment_insurance_app;
GRANT ALL PRIVILEGES ON DATABASE pension_insurance TO pension_insurance_app;
```

## Docker Build Commands

### Social Insurance Service
```bash
cd social-insurance-service
docker build -t social-insurance-service .
```

### Employment Insurance Service
```bash
cd employment-insurance-service
docker build -t employment-insurance-service .
```

### Pension Insurance Service
```bash
cd pension-insurance-service
docker build -t pension-insurance-service .
```

## Google Cloud Run Deployment

### Prerequisites
1. Install Google Cloud SDK
2. Authenticate: `gcloud auth login`
3. Set project: `gcloud config set project YOUR_PROJECT_ID`

### Enable Required Services
```bash
gcloud services enable run.googleapis.com
gcloud services enable sqladmin.googleapis.com
```

### Deploy Services

#### 1. Deploy Social Insurance Service
```bash
gcloud run deploy social-insurance-service \
  --image gcr.io/PROJECT_ID/social-insurance-service \
  --platform managed \
  --region asia-northeast1 \
  --allow-unauthenticated \
  --port 9003 \
  --set-env-vars DB_URL=r2dbc:postgresql://DB_HOST:5432/social_insurance_health \
  --set-env-vars DB_USER=social_insurance_health_app \
  --set-env-vars DB_PASSWORD=YOUR_DB_PASSWORD \
  --set-env-vars FLYWAY_URL=jdbc:postgresql://DB_HOST:5432/social_insurance_health
```

#### 2. Deploy Employment Insurance Service
```bash
gcloud run deploy employment-insurance-service \
  --image gcr.io/PROJECT_ID/employment-insurance-service \
  --platform managed \
  --region asia-northeast1 \
  --allow-unauthenticated \
  --port 9004 \
  --set-env-vars DB_URL=r2dbc:postgresql://DB_HOST:5432/employment_insurance \
  --set-env-vars DB_USER=employment_insurance_app \
  --set-env-vars DB_PASSWORD=YOUR_DB_PASSWORD \
  --set-env-vars FLYWAY_URL=jdbc:postgresql://DB_HOST:5432/employment_insurance
```

#### 3. Deploy Pension Insurance Service
```bash
gcloud run deploy pension-insurance-service \
  --image gcr.io/PROJECT_ID/pension-insurance-service \
  --platform managed \
  --region asia-northeast1 \
  --allow-unauthenticated \
  --port 9005 \
  --set-env-vars DB_URL=r2dbc:postgresql://DB_HOST:5432/pension_insurance \
  --set-env-vars DB_USER=pension_insurance_app \
  --set-env-vars DB_PASSWORD=YOUR_DB_PASSWORD \
  --set-env-vars FLYWAY_URL=jdbc:postgresql://DB_HOST:5432/pension_insurance
```

## Environment Variables

Each service supports the following environment variables:

### Database Configuration
- `DB_URL`: R2DBC database URL
- `DB_USER`: Database username
- `DB_PASSWORD`: Database password
- `FLYWAY_URL`: JDBC database URL for migrations

### Service Configuration
- `PORT`: Service port (default varies by service)
- `SPRING_PROFILES_ACTIVE`: Spring profile (default: default)

### Service-Specific Configuration
- **Social Insurance**: `social.insurance.care-insurance-age-threshold` (default: 40)
- **Employment Insurance**: `employment.insurance.rounding-threshold` (default: 0.50000001)

## Testing Services

### Health Insurance Service
```bash
curl "https://social-insurance-service-URL/health-insurance/calculate?monthlySalary=200000&age=35"
```

### Employment Insurance Service
```bash
curl "https://employment-insurance-service-URL/employment-insurance/calculate?monthlySalary=200000&employmentType=GENERAL"
```

### Pension Insurance Service
```bash
curl "https://pension-insurance-service-URL/pension-insurance/calculate?monthlySalary=200000"
```

## Monitoring

Each service exposes actuator endpoints:
- `/actuator/health` - Health check
- `/actuator/info` - Application info
- `/actuator/metrics` - Metrics

## Scaling

Each service can be scaled independently in Cloud Run:
```bash
# Scale to 3 instances
gcloud run services update social-insurance-service --platform managed --region asia-northeast1 --max-instances 3
```

## Security Considerations

1. Use Cloud SQL for production databases
2. Enable IAM authentication for databases
3. Use secret manager for sensitive configuration
4. Enable VPC-Connector for private database access
5. Set up proper CORS policies for frontend applications

## Troubleshooting

### Common Issues
1. **Database Connection Failures**: Check database URLs and credentials
2. **Migration Failures**: Verify Flyway configuration and database permissions
3. **Port Conflicts**: Ensure each service uses different ports
4. **Memory Issues**: Monitor Cloud Run instance memory usage

### Logs
```bash
# View service logs
gcloud logs read "resource.type=cloud_run_revision AND resource.labels.service_name=social-insurance-service"
```

## Migration from Monolith

The original monolithic service has been split as follows:
- Health insurance logic → Social Insurance Service
- Employment insurance logic → Employment Insurance Service  
- Pension insurance logic → Pension Insurance Service
- Tax calculation logic → Can be added as separate service if needed

Each service maintains its own data schema and can be updated independently.
