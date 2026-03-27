# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Copy source code
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jdk-alpine

# Copy the built JAR from builder stage
COPY --from=build /target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
