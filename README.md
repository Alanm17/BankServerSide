# Bank Server

Backend server for a modern banking system, built with Spring Boot and MySQL.

## Features
- **User Management**: Registration, authentication, and profile management.
- **Accounts**: Create and manage bank accounts.
- **Transactions**: Deposit, withdraw, and transfer funds.
- **Security**: Secure endpoints and data handling.

## Tech Stack
- **Java 21**
- **Spring Boot 3.4.1** (Web, Data JPA, Thymeleaf)
- **MySQL 8**
- **Docker & Docker Compose**

## How to Run

### Prerequisites
- Docker & Docker Compose
- Java 21 (for local dev)

### Run with Docker (Recommended)
```bash
docker-compose up --build
```
The server will start on port `8081`.

### Run Locally
1. Ensure MySQL is running on port `3307` (or update `application.properties`).
2. Run the application:
```bash
./mvnw spring-boot:run
```
