# Use a base image with JDK (which includes Java)
FROM openjdk:11-jdk-slim

# Set the working directory
WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy the Maven wrapper files and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Install project dependencies
RUN ./mvnw dependency:go-offline

# Copy the application source code
COPY src ./src

# Package the application
RUN ./mvnw clean package

# Copy the jar file into the container
COPY target/ustreamweb3-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
