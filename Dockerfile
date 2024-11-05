# Use an official Java runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the rest of the application code
COPY . .

# Install Maven and build the application
RUN ./mvnw clean package

# Expose the application port (optional, update if your app uses a specific port)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "target/ustreamweb3-backend-0.0.1-SNAPSHOT.jar"]
