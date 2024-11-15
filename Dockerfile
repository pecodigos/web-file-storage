#Use the latest Maven image with OpenJDK 21
FROM maven:3.9.5-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Use a lightweight JRE image to run the application
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/web_file_storage-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]