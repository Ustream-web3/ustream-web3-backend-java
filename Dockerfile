# Use an appropriate base image with JDK and Maven for the build stage
FROM maven:3.9.4-openjdk-22 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy the source code to the container
COPY src ./src

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package

# Use a smaller base image for the runtime
FROM openjdk:11-jre-slim

# Set the working directory inside the runtime container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/ustreamweb3-backend-0.0.1-SNAPSHOT.jar ./ustreamweb3-backend.jar

# Specify the command to run the JAR file
CMD ["java", "-jar", "ustreamweb3-backend.jar"]
