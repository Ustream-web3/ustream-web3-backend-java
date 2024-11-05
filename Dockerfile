# Start with a base image containing Maven and OpenJDK for building
FROM maven:3.9.4-openjdk-11 AS build

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

# Start with a base image containing Java runtime for the runtime stage
FROM openjdk:11-jre-slim

# Add Maintainer Info
LABEL maintainer="nnennahumphrey.nh@gmail.com"

# Set the working directory inside the runtime container
WORKDIR /app

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/ustreamweb3-backend-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
COPY --from=build /app/target/ustreamweb3-backend-0.0.1-SNAPSHOT.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
