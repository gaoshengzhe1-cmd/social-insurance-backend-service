-- ===========================================
-- Database Setup Script for Social Insurance Services
-- Run this script to create databases and users for all three services
-- ===========================================

-- Create databases for each service
CREATE DATABASE health_insurance_db;
CREATE DATABASE employment_insurance_db;
CREATE DATABASE pension_insurance_db;

-- Create users for each service
CREATE USER health_insurance_app WITH PASSWORD 'NewStrongPassword123!';
CREATE USER employment_insurance_app WITH PASSWORD 'NewStrongPassword123!';
CREATE USER pension_insurance_app WITH PASSWORD 'NewStrongPassword123!';

-- Grant privileges to users
-- Health Insurance Service
GRANT ALL PRIVILEGES ON DATABASE health_insurance_db TO health_insurance_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO health_insurance_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO health_insurance_app;

-- Employment Insurance Service
GRANT ALL PRIVILEGES ON DATABASE employment_insurance_db TO employment_insurance_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO employment_insurance_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO employment_insurance_app;

-- Pension Insurance Service
GRANT ALL PRIVILEGES ON DATABASE pension_insurance_db TO pension_insurance_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO pension_insurance_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO pension_insurance_app;

-- Grant usage on schema for all users
GRANT USAGE ON SCHEMA public TO health_insurance_app;
GRANT USAGE ON SCHEMA public TO employment_insurance_app;
GRANT USAGE ON SCHEMA public TO pension_insurance_app;

-- Create extensions needed by the applications
\c health_insurance_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c employment_insurance_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\c pension_insurance_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Verify setup
\l

-- Display user information
\du

-- ===========================================
-- Post-setup verification commands:
-- ===========================================

-- Test connections (run these after the services are deployed):
-- psql -h localhost -U health_insurance_app -d health_insurance_db
-- psql -h localhost -U employment_insurance_app -d employment_insurance_db  
-- psql -h localhost -U pension_insurance_app -d pension_insurance_db

-- Check if tables were created by Flyway (after service startup):
-- \c health_insurance_db
-- \dt

-- \c employment_insurance_db
-- \dt

-- \c pension_insurance_db
-- \dt

-- ===========================================
-- Service startup with profiles:
-- ===========================================

# Development:
-- SPRING_PROFILES_ACTIVE=dev

# Test:  
-- SPRING_PROFILES_ACTIVE=test

# Production:
-- SPRING_PROFILES_ACTIVE=prod

-- ===========================================
-- Production Notes:
-- ===========================================
-- 1. Change default passwords before production deployment
-- 2. Use Cloud SQL for production databases
-- 3. Configure proper network security groups
-- 4. Set up database backups and monitoring
-- 5. Consider using connection pooling
-- 6. Use environment variables for production credentials
