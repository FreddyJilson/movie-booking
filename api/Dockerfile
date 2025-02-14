# Stage 1: Build the application using Maven
FROM maven:3.8.6-openjdk-11-slim AS build

# Set working directory for the build stage
WORKDIR /app

# Copy the pom.xml and install dependencies (this is cached if dependencies don't change)
COPY ./api/pom.xml .

# Download dependencies to leverage Docker cache
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY ./api /app

# Run the Maven build and package the application (this will only re-run if source code changes)
RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM openjdk:11-jre-slim

# Set working directory for the runtime environment
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/api.jar /app/api.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/api.jar"]
