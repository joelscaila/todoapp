# TodoApp – REST API with Spring Boot, JWT and PostgreSQL

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)
![Docker](https://img.shields.io/badge/Docker-ready-blue)
![Status](https://img.shields.io/badge/Build-passing-success)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

Professional backend application for task management (TODOs), built with **Spring Boot 3**, **JWT**, **PostgreSQL**, **Docker**, **JPA/Hibernate**, and **Swagger/OpenAPI**.  
Includes secure authentication, validations, automated tests, and a fully dockerized environment for easy execution.

---

## 🚀 Main Technologies

- Java 21  
- Spring Boot 3  
- Spring Security + JWT  
- PostgreSQL 15  
- Docker & Docker Compose  
- JPA / Hibernate  
- Swagger / OpenAPI 3  
- JUnit 5 + Mockito  

---

## 🐳 How to Run the Project

The project is ready to run with a single command:

    docker compose up -d

This will start:

- The Spring Boot API  
- The PostgreSQL database  
- Automatic loading of initial demo data (demo user + TODOs)

---

## 👤 Demo User

To test the API without registering:

    username: demo
    password: 123456

---

## 📘 Swagger Documentation

Once the project is running, open:

http://localhost:8080/swagger-ui.html

### ⚠️ To avoid 403 errors

1. Log in using `/api/auth/login`  
2. Copy the JWT token  
3. Click the **Authorize** button in Swagger  
4. Paste the token:

        <your_token_here>

---

## 📡 Main Endpoints

### 🔐 Authentication

| Method | Endpoint            | Description   |
|--------|---------------------|---------------|
| POST   | /api/auth/register  | Register user |
| POST   | /api/auth/login     | Get JWT token |

### 📝 TODOs

| Method | Endpoint           | Description        |
|--------|--------------------|--------------------|
| GET    | /api/todos         | List user's TODOs  |
| POST   | /api/todos         | Create a TODO      |
| GET    | /api/todos/{id}    | Get a TODO         |
| PUT    | /api/todos/{id}    | Update a TODO      |
| DELETE | /api/todos/{id}    | Delete a TODO      |

---

## 🧱 General Architecture

    Client -> Controller -> Service -> Repository -> PostgreSQL

- **Controller**: exposes REST endpoints  
- **Service**: business logic  
- **Repository**: data access with JPA  
- **Security**: JWT filters and configuration  
- **Exception**: global error handling  
- **DTOs**: validation and data transport  

---

## 📁 Project Structure

    src/main/java/com/joel/todoapp
     ├── config
     ├── controller
     ├── dto
     ├── exception
     ├── mapper
     ├── model
     ├── repository
     ├── security
     └── service

---

## 🧪 Tests

The project includes tests for:

- Services  
- Controllers  
- Security  
- Exceptions  
- Mappers  

Run tests:

    mvn test

---

## 🧠 Technical Decisions

- JWT for stateless and scalable authentication  
- Docker Compose for zero‑configuration execution  
- PostgreSQL as a realistic production database  
- H2/Postgres profiles for development and testing  
- DTOs + validations for data integrity  
- GlobalExceptionHandler for consistent error responses  
- Swagger for clear and navigable documentation  

---

## 🔮 Roadmap

- [ ] Flyway migrations  
- [ ] Integration tests with Testcontainers  
- [ ] CI/CD with GitHub Actions  
- [ ] Pagination in endpoints  
- [ ] Advanced roles (admin)  
- [ ] Metrics with Spring Actuator  
