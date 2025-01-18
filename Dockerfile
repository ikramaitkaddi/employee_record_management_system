# Use AdoptOpenJDK 17 as the base image
FROM openjdk:17-oracle

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged jar file into the container at path /
COPY target/employee_management-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Set environment variables for Spring
ENV SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
ENV SPRING_DATASOURCE_USERNAME=system
ENV SPRING_DATASOURCE_PASSWORD=SysPassword1
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=oracle.jdbc.OracleDriver
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SERVER_PORT=8080
ENV SPRING_SERVLET_CONTEXT_PATH=/api
ENV SPRINGDOC_API_DOCS_PATH=/api-docs
ENV SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html

# Specify the command to run on container start
CMD ["java", "-jar", "app.jar"]



