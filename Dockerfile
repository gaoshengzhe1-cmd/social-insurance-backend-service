# Multi-stage build for Spring Boot (Gradle, Java 21)

# ===== Build stage =====
FROM gradle:8.10.0-jdk21-alpine AS build

WORKDIR /app

# Copy Gradle wrapper and build files first (for better layer cache)
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew gradlew.bat ./

# Copy source code
COPY src src

# Build executable jar (skip tests; they are disabled anyway)
RUN ./gradlew bootJar --no-daemon

# ===== Runtime stage =====
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose application port (matches server.port in application-dev.properties)
EXPOSE 9002

# Environment variables to let you override DB connection from docker run
# Example (host PostgreSQL):
#   -e DB_URL=r2dbc:postgresql://host.docker.internal:5432/social_insurance
#   -e FLYWAY_URL=jdbc:postgresql://host.docker.internal:5432/social_insurance
ENV SPRING_PROFILES_ACTIVE=dev

ENTRYPOINT ["java","-jar","app.jar"]
