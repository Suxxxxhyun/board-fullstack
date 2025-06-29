# Build stage
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

# Download dependencies
RUN gradle dependencies --no-daemon

# Copy source code
COPY src ./src

# Build the application
RUN gradle build 
# RUN gradle build --no-daemon

# Runtime stage
FROM openjdk:21-jdk

WORKDIR /app

# Create non-root user
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy the built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appuser app.jar

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
