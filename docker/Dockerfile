# ---------- Builder stage (Maven + JDK) ----------
FROM maven:3.9.5-eclipse-temurin-21 AS builder

WORKDIR /workspace

# Copy only the pom files first to leverage Docker cache (optional optimization)
COPY ../../common-framework/pom.xml common-framework/
COPY ../../security-framework/pom.xml security-framework/
COPY ../../cloudinary-framework/pom.xml cloudinary-framework/
COPY ../../user-service/pom.xml user-service/

# Copy source code
COPY ../../common-framework/ common-framework/
COPY ../../security-framework/ security-framework/
COPY ../../cloudinary-framework/ cloudinary-framework/
COPY ../../user-service/ user-service/

# Build/install modules in order (single RUN to keep cache & fewer layers)
RUN mvn -f common-framework/pom.xml clean install -DskipTests \
 && mvn -f security-framework/pom.xml clean install -DskipTests \
 && mvn -f cloudinary-framework/pom.xml clean install -DskipTests \
 && mvn -f user-service/pom.xml clean package -DskipTests

# ---------- Runtime stage (slim JRE) ----------
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# Switch to the root (admin) user inside the container.
# Only the root user is allowed to install system packages.
USER root

# Update the package index (list of available packages),
# install 'curl' without unnecessary extra packages,
# then clean up the package cache to keep the image small.
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Switch back to a non-root user for better security.
# Your app should NOT run as root inside production containers.
USER 1000

# Copy only the needed jar(s) from builder stage
# Adjust the source path/wildcard to match your actual jar filename(s)
COPY --from=builder /workspace/user-service/target/*.jar ./app.jar

# (Optional) add non-root user, expose port, healthcheck, etc.
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
