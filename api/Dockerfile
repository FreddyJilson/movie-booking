# Build source code and create jar file.
FROM openjdk:8

# Set working directory in container
WORKDIR /app

# Copy source code files to container image
COPY ./api .

# Maven install dependencies
RUN ./mvnw install

# Expose port 8080 for the API
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/target/backend.jar"]