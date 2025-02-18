# Employee Records Management System

## Table of Contents
- [Introduction](#introduction)
- [Tasks Overview](#tasks-overview)
- [System Architecture](#system-architecture)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [Testing and Documentation](#testing-and-documentation)
    - [JUnit Tests](#junit-tests)
    - [Test Coverage with JaCoCo](#test-coverage-with-jacoco)
    - [Swagger Documentation](#swagger-documentation)
    - [Postman Collection](#postman-collection)
- [Docker Compose Configuration](#docker-compose-configuration)
- [Authors](#authors)

---

## Introduction
The **Employee Records Management System** is designed to efficiently manage employee information within an organization. The system supports CRUD operations for employees, roles, and permissions and provides a user-friendly interface built with Java Swing. The application is containerized with Docker for ease of deployment.

---

## Tasks Overview
1. **Design and Conception of the System**
    - Created detailed system architecture diagrams.
    - Designed database schema to handle employees, roles, and permissions efficiently.

2. **Implementation of CRUD for Roles and Permissions**
    - Added APIs for creating, reading, updating, and deleting roles and permissions.

3. **CRUD Operations for Employees**
    - Developed robust APIs to manage employee data.

4. **JUnit Tests**
    - Comprehensive unit tests to ensure code quality and reliability.

5. **Test Coverage with JaCoCo**
    - Verified test coverage and generated coverage reports.

6. **Swagger Documentation**
    - Integrated Swagger for API documentation.

7. **Postman Collection**
    - Created a Postman collection to test and demonstrate the APIs.

8. **Database Connection**
    - Configured the application to connect with an Oracle database using a Docker image.

9. **UI Development**
    - Built a user-friendly desktop application using Java Swing.

10. **Dockerization**
- Containerized the entire application for seamless deployment.

---

## System Architecture
The system is divided into three layers:
1. **Presentation Layer**: Java Swing-based user interface.
2. **Business Logic Layer**: Spring Boot services to handle core logic.
3. **Data Layer**: Oracle database for data persistence.

---

## Features
- User management (Roles and Permissions).
- Employee management.
- API testing via Postman.
- Swagger-based API documentation.
- Database containerized with Docker.
- Desktop application for user interaction.

---

## Technologies Used
- **Backend**: Java, Spring Boot
- **Frontend**: Java Swing
- **Database**: Oracle (Dockerized)
- **Testing**: JUnit, JaCoCo, Postman
- **Documentation**: Swagger, Markdown
- **Containerization**: Docker

---

## Database Setup
To set up the Oracle database using Docker:
1. **Run the container**:
   ```bash
   docker pull gvenzl/oracle-free
Configure the application: Update the following properties in the application.yml file:

 ```yaml

spring:
  datasource:
    url: jdbc:oracle:thin:@//localhost:1521/FREEPDB1
    username: system
    password: SysPassword1
    driver-class-name: oracle.jdbc.OracleDriver
  jpa:
    hibernate:
      ddl-auto: update
```
## Testing and Documentation

### JUnit Tests

Unit tests are written using JUnit to ensure application reliability.

To execute the tests:

```bash
mvn test
```
  ## Test Coverage with JaCoCo

 JaCoCo is used to measure the code coverage of the unit tests.

Add JaCoCo to `pom.xml`

  ```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.8</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Generate Coverage Report

Run the following Maven command to generate the report:

```bash
mvn clean install
```

The JaCoCo report will be available in the `target/site/jacoco` directory.

## View the Coverage Report

Open `target/site/jacoco/index.html` in your browser to view the detailed test coverage report.
# Swagger Documentation

Access the Swagger UI at: [http://localhost:8080/api/swagger-ui.html](http://localhost:8080/swagger-ui.html)

# Postman Collection

Import the provided Postman collection (`employee_management.postman_collection.json`) into Postman to test the APIs.
## Docker Compose Configuration
 docker-compose.yml

 ```yaml
 version: '3.8'

services:
  oracle:
    image: gvenzl/oracle-free
    container_name: "oracle"
    environment:
      ORACLE_PDB_NAME: FREEPDB1
      ORACLE_USER: system
      ORACLE_PASSWORD: SysPassword1
    ports:
      - "1521:1521"

  spring-app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: "employee_management"
    ports:
      - "8080:8080"
    depends_on:
      - oracle
    environment:
      SPRING_DATASOURCE_URL: jdbc:oracle:thin:@//oracle:1521/FREEPDB1
      SPRING_DATASOURCE_USERNAME: system
      SPRING_DATASOURCE_PASSWORD: SysPassword1
      SPRING_DATASOURCE_DRIVER_CLASS_NAME: oracle.jdbc.OracleDriver
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SERVER_PORT: 8080
      SPRING_SERVLET_CONTEXT_PATH: /api
      SPRINGDOC_API_DOCS_PATH: /api-docs
      SPRINGDOC_SWAGGER_UI_PATH: /swagger-ui.html
 ```
### Services

1. **Oracle Service**

    - **Image**: `gvenzl/oracle-free`
    - **Container Name**: `oracle`
    - **Environment Variables**:
        - `ORACLE_PDB_NAME`: `FREEPDB1` (Pluggable Database Name)
        - `ORACLE_USER`: `system`
        - `ORACLE_PASSWORD`: `SysPassword1`
    - **Ports Mapping**: `1521:1521` (Host port 1521 mapped to container port 1521)

2. **Spring Application Service**

    - **Build Context**: `.` (current directory)
    - **Dockerfile**: `Dockerfile`
    - **Container Name**: `employee_management`
    - **Ports Mapping**: `8080:8080` (Host port 8080 mapped to container port 8080)
    - **Dependencies**: Depends on the Oracle service
    - **Environment Variables**:
        - `SPRING_DATASOURCE_URL`: `jdbc:oracle:thin:@//oracle:1521/FREEPDB1`
        - `SPRING_DATASOURCE_USERNAME`: `system`
        - `SPRING_DATASOURCE_PASSWORD`: `SysPassword1`
        - `SPRING_DATASOURCE_DRIVER_CLASS_NAME`: `oracle.jdbc.OracleDriver`
        - `SPRING_JPA_HIBERNATE_DDL_AUTO`: `update`
        - `SERVER_PORT`: `8080`
        - `SPRING_SERVLET_CONTEXT_PATH`: `/api`
        - `SPRINGDOC_API_DOCS_PATH`: `/api-docs`
        - `SPRINGDOC_SWAGGER_UI_PATH`: `/swagger-ui.html`

### Dockerfile for Spring Application

```dockerfile
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
 ```
# Authors
Ikram Ait Kaddi – Software Engineer