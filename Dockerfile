# ==============================================================================
# Multi-Stage Dockerfile for Job Profile Manager (JPM)
# Optimized for Java 21, Security (Non-root user), and Small Footprint
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build Stage
# ------------------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /build

# Copy Maven wrapper and POM first to leverage Docker layer caching
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies in offline-friendly layer
RUN ./mvnw dependency:go-offline -B

# Copy project source tree
COPY src/ src/

# Build application package skipping unit tests during image creation
RUN ./mvnw clean package -DskipTests -B

# ------------------------------------------------------------------------------
# Stage 2: Production Runtime Stage
# ------------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner

# Create dedicated non-root application user and group for security
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

# Copy the built jar artifact from builder stage
COPY --from=builder /build/target/job-profile-manager-*.jar app.jar

# Ensure proper file ownership
RUN chown -R spring:spring /app

# Switch to unprivileged user
USER spring:spring

# Expose standard Spring Boot HTTP port
EXPOSE 8080

# Environment variables with sensible defaults
ENV SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=8080 \
    JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

# Container Healthcheck targeting Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Launch application with optimized JVM memory management flags
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
