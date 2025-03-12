# Use Eclipse Temurin Java 21 as the base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy maven files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package

# Command to start interactive shell
CMD ["bash"]