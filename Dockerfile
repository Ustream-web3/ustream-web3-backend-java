# Use a base image that has Java
FROM openjdk:22-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY target/ustreamweb3-backend-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
