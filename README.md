# NPP API

A modern web-based system for planning, reporting, and evaluating the activities of academic staff.

## 📋 Table of Contents

- [Prerequisites](#prerequisites)
- [Dependencies](#dependencies)
- [Project Structure](#project-structure)
- [Environment Variables](#environment-variables)
- [How to Run](#how-to-run)
    - [Local Development](#local-development)
    - [Docker (Recommended)](#docker-recommended)
    - [Production Deployment](#production-deployment)
- [API Documentation](#api-documentation)
- [CI/CD](#cicd)
- [Troubleshooting](#troubleshooting)

---

## 🔧 Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **Docker** & **Docker Compose** (for containerized deployment)
- **PostgreSQL 14+** (if running locally without Docker)

---

## 📦 Dependencies

### Core Framework (22 dependencies)
- **Spring Boot 3.5.4** - Application framework
- **Spring Data JPA** - Database ORM
- **Spring Security** - Authentication & Authorization
- **Spring Web** - REST API
- **Spring Boot Actuator** - Health checks and metrics
- **Spring Boot Docker Compose** - Docker integration (optional, runtime)

### Database (4 dependencies)
- **PostgreSQL** (runtime) - Primary database
- **Flyway 11.7.2** - Database migrations
    - `flyway-core`
    - `flyway-database-postgresql`
- **Hypersistence Utils 3.8.1** (`hibernate-63`) - JPA utilities

### Security & JWT (4 dependencies)
- **Spring Security** - Security framework
- **JJWT 0.11.5** - JWT token handling
    - `jjwt-api`
    - `jjwt-impl` (runtime)
    - `jjwt-jackson` (runtime)

### Documentation
- **Springdoc OpenAPI 2.1.0** - API documentation (Swagger UI)
    - `springdoc-openapi-starter-webmvc-ui`

### Utilities (7 dependencies)
- **Lombok 1.18.34** - Reduce boilerplate (provided)
- **MapStruct 1.5.5.Final** - Entity-DTO mapping
    - `mapstruct`
    - `mapstruct-processor` (provided)
- **Spring Boot Validation** - Request validation
- **Resilience4j 2.2.0** - Fault tolerance
    - `resilience4j-spring-boot3`
- **Spring Boot Mail** - Email support
- **Logstash Logback Encoder 7.4** - JSON logging

### Testing (2 dependencies)
- **Spring Boot Test** (test scope)
- **Spring Security Test** (test scope)

### Code Quality
- **Checkstyle** - Code style linter (Maven plugin)

**Total: 22 Maven dependencies**

---

## 📁 Project Structure
```
npp-api/
├── docs/                        # Documentation
│   └── images/                  # Images for docs
│       └── .gitkeep
│   └── api/                  
│       └── api-docs.yaml        # OpenAPI documentation
├── back/                        # Source code
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/ua/cn/stu/npp/npp_portal_backend/
│   │   │   │   ├── config/          # Configuration classes
│   │   │   │   ├── controller/      # REST controllers
│   │   │   │   ├── dto/
│   │   │   │   │   ├── request/     # Request DTOs
│   │   │   │   │   └── response/    # Response DTOs
│   │   │   │   ├── entity/          # JPA entities
│   │   │   │   ├── enums/           # Enumerations
│   │   │   │   ├── exception/       # Custom exceptions
│   │   │   │   ├── mapper/          # Mappers
│   │   │   │   ├── repository/      # Repositories
│   │   │   │   └── service/         # Business logic
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── logback-spring.xml
│   │   └── test/
│   ├── Dockerfile               # Multi-stage Docker image
│   ├── .dockerignore
│   ├── checkstyle.xml           # Code style rules
│   └── pom.xml                  # Maven config
├── .env.example                 # Environment template
├── .gitignore
├── docker-compose.yml           # Local development
└── README.md
```

---

## ⚙️ Environment Variables

### Required (No defaults)
```bash
DB_PASSWORD=your_postgres_password
```

### Optional (With defaults)
```bash
# Database
DB_URL=jdbc:postgresql://localhost:5432/npp_db
DB_NAME=npp_db
DB_USERNAME=postgres
DB_PORT=5432

# Application
SPRING_PROFILE=dev          # dev | local | prod
SERVER_PORT=8080
APP_PORT=8080

# JPA
DDL_AUTO=update            # validate | update | create | create-drop
SHOW_SQL=true
FORMAT_SQL=true

# Logging
LOGGING_LEVEL=INFO         # TRACE | DEBUG | INFO | WARN | ERROR
```

---

## 🚀 How to Run

### Local Development

#### 1. Setup Environment
```bash
# Clone repository
git clone https://github.com/your-username/npp-portal-backend.git
cd npp-portal-backend

# Create .env file
cp .env.example .env

# Edit .env and set DB_PASSWORD
nano .env
```

#### 2. Create Database
```sql
CREATE DATABASE npp_db;
```

#### 3. Run Application
```bash
cd back

# Build
mvn clean install

# Run
mvn spring-boot:run
```

Application will be available at: http://localhost:8080

---

### Docker (Recommended)

#### 1. Setup Environment
```bash
# Create .env file in repository root
cp .env.example .env

# Set your database password
echo "DB_PASSWORD=your_secure_password" >> .env
```

#### 2. Run with Docker Compose
```bash
# Start all services (PostgreSQL + Backend)
docker-compose up -d

# View logs
docker-compose logs -f backend

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

#### 3. Build Docker Image Manually
```bash
cd back

# Build image
docker build -t npp-portal-backend:latest .

# Run container
docker run -d \
  --name npp-backend \
  -p 8080:8080 \
  -e SPRING_PROFILE=prod \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/npp_db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=your_password \
  npp-portal-backend:latest
```

---

### Production Deployment

#### 1. Build Production Image
```bash
cd back
docker build -t npp-portal-backend:prod --target runtime .
```

#### 2. Set Environment Variables
```bash
export SPRING_PROFILE=prod
export DB_URL=jdbc:postgresql://your-db-host:5432/npp_db
export DB_USERNAME=postgres
export DB_PASSWORD=your_secure_password
export LOGGING_LEVEL=WARN
export DDL_AUTO=validate
export SHOW_SQL=false
```

#### 3. Run Container
```bash
docker run -d \
  --name npp-backend-prod \
  -p 8080:8080 \
  -e SPRING_PROFILE=prod \
  -e DB_URL=$DB_URL \
  -e DB_USERNAME=$DB_USERNAME \
  -e DB_PASSWORD=$DB_PASSWORD \
  -e LOGGING_LEVEL=WARN \
  -e DDL_AUTO=validate \
  --restart unless-stopped \
  npp-portal-backend:prod
```

#### 4. View JSON Logs
```bash
# View logs in JSON format
docker logs npp-backend-prod

# Follow logs
docker logs -f npp-backend-prod

# Parse JSON logs with jq
docker logs npp-backend-prod | jq .
```

---

## 📡 API Documentation

Once running, access:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **Health Check:** http://localhost:8080/actuator/health

### Example Endpoints
```
GET    /api/v1/institutes
POST   /api/v1/institutes
GET    /api/v1/faculties
GET    /api/v1/departments
GET    /api/v1/users
POST   /api/v1/activities
GET    /api/v1/dictionaries/{type}

Public (no auth):
GET    /api/v1/public/hierarchy/institutes
GET    /api/v1/public/search/teacher?query={name}
```

---

## 🔄 CI/CD

### Code Quality Check (Checkstyle)
```bash
cd back

# Run checkstyle
mvn checkstyle:check

# Generate checkstyle report
mvn checkstyle:checkstyle
```

### Running Tests
```bash
cd back

# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report
```

### CI Pipeline Example (.github/workflows/ci.yml)
```yaml
name: CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
          distribution: 'temurin'
          
      - name: Run Checkstyle
        run: cd back && mvn checkstyle:check
        
      - name: Run Tests
        run: cd back && mvn test
        
      - name: Build Docker Image
        run: cd back && docker build -t npp-portal-backend:${{ github.sha }} .
```

---

## 🛠️ Troubleshooting

### Cannot connect to database
```bash
# Check PostgreSQL is running
docker ps | grep postgres

# Check database exists
docker exec -it npp-postgres psql -U postgres -l

# Check environment variables
docker exec npp-backend env | grep DB_
```

### Port already in use
```bash
# Change port in .env
SERVER_PORT=8081
APP_PORT=8081

# Or kill process using port 8080
lsof -ti:8080 | xargs kill -9  # Linux/Mac
netstat -ano | findstr :8080   # Windows
```

### Logs not showing
```bash
# View Docker logs
docker logs npp-backend

# Check if JSON logging is enabled
docker exec npp-backend env | grep SPRING_PROFILE

# Should be "prod" for JSON logs
```

### Checkstyle errors
```bash
# View detailed errors
mvn checkstyle:checkstyle

# Check report
open back/target/site/checkstyle.html
```

---

## 🔒 Security Notes

- Never commit `.env` file
- Use strong passwords in production
- Set `DDL_AUTO=validate` in production
- Disable SQL logging in production
- Use HTTPS in production
- Implement JWT authentication (coming soon)

