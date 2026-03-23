# Use Java 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/TestAssessmentProject.jar app.jar

# Expose port (optional but good practice)
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","/app/app.jar"]