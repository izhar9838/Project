# Stage 1: Build the Spring Boot application
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory

WORKDIR /app

# Copy the pom.xml and install dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/School_Project-1.0.jar app.jar

# Expose port 9090 (adjust if your app uses a different port)
EXPOSE 9090

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]