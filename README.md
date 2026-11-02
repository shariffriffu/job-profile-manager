# Job Profile Manager (JPM)

A production-oriented, centralised career profile management platform built with **Java 21** and **Spring Boot 3.3.5**.

The application serves as a single source of truth for an individual's professional profile, maintaining factually accurate career records (designation, summary, skills, work experience, projects, education, certifications, and resumes) with architectural support for AI-driven tailoring and multi-platform synchronisation.

---

## Quick Navigation

- [Architecture Overview](#architecture-overview)
- [Prerequisites](#prerequisites)
- [How to Start & Connect](#how-to-start--connect)
  - [Option 1: Quickstart (Standalone Local Profile)](#option-1-quickstart-standalone-local-profile-no-external-db-needed)
  - [Option 2: Connecting to PostgreSQL (Production / Standard Mode)](#option-2-connecting-to-postgresql-production--standard-mode)
- [Interactive API Testing](#interactive-api-testing)
  - [Using Swagger UI](#1-interactive-swagger-ui)
  - [Using cURL or PowerShell](#2-sample-api-requests-curl--powershell)
- [Running Automated Tests](#running-automated-tests)
- [Configuration & Environment Reference](#configuration--environment-reference)
- [Package & Project Structure](#package-structure)
- [Milestone Roadmap](#future-roadmap)

---

## Architecture Overview

Job Profile Manager is built as a **Modular Monolith** adhering to clean layered architecture, SOLID principles, and strict separation of concerns:

```
com.jobmanager
├── config               # Application, OpenAPI 3.0 & JPA auditing configurations
├── security             # Spring Security 6 stateless filter chain
├── common               # BaseEntity (UUID, timestamps), ApiResponse, ErrorResponse, GlobalExceptionHandler
├── user                 # User identity, credentials & account lifecycle
├── profile              # Master Career Profile domain module (Milestone 1)
│   ├── controller       # REST API endpoints (/api/v1/profiles)
│   ├── dto              # Request and immutable response DTOs (Records)
│   ├── entity           # JPA entities & Enums (UUID primary keys, audit fields)
│   ├── mapper           # Decoupled entity-to-DTO mapper
│   ├── repository       # Spring Data JPA repository
│   └── service          # Transactional business logic & interface
├── experience           # Work history, roles, and accomplishments
├── skills               # Taxonomy of technical skills and proficiency levels
├── projects             # Portfolio projects, tech stacks, and metrics
├── education            # Academic qualifications and timelines
├── certifications       # Credentials, verification URLs, and expiry
├── resume               # Resume variants, snapshots, and exports
├── ai                   # AI content optimisation & factual alignment engine
├── job                  # Target job descriptions & requirement parser
├── matcher              # ATS keyword scoring & semantic gap analysis
├── integration          # Multi-platform adapters
│   ├── linkedin         # LinkedIn official API / authorized sync
│   ├── naukri           # Naukri profile synchronizer & manual export kits
│   ├── glassdoor        # Glassdoor alignment adapter
│   ├── indeed           # Indeed platform adapter
│   ├── github           # GitHub portfolio and repository showcase
│   ├── monster          # Monster adapter
│   └── foundit          # Foundit (Monster APAC) adapter
├── sync                 # Cross-platform sync engine and conflict resolver
└── audit                # Transaction audit logging and historical tracking
```

---

## Prerequisites

Before starting, ensure you have:
1. **Java 21 LTS** installed (`java -version`)
   - If using OpenJDK 21, ensure `JAVA_HOME` points to your JDK 21 directory.
2. **Git** installed (`git --version`)
3. **PostgreSQL 14+** (Required only when running in PostgreSQL mode; not required for quickstart mode)
4. Maven is included via the bundled wrapper (`mvnw.cmd` on Windows, `./mvnw` on Linux/macOS).

---

## How to Start & Connect

### Option 1: Quickstart (Standalone Local Profile, No External DB Needed)

If you want to run the application immediately without creating a database or configuring PostgreSQL credentials, use the built-in **`local` profile** (uses embedded in-memory database with PostgreSQL compatibility):

#### Windows PowerShell:
```powershell
# Set JAVA_HOME (if not already in system environment)
$env:JAVA_HOME = "C:\Users\Shariff\.jdks\openjdk-21"

# Run the application with the local profile
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

#### Windows CMD:
```cmd
set JAVA_HOME=C:\Users\Shariff\.jdks\openjdk-21
mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

#### Linux / macOS:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

The application will start on port `8080` (or `SERVER_PORT` if specified).

---

### Option 2: Connecting to PostgreSQL (Production / Standard Mode)

In standard mode, the application connects to PostgreSQL and uses **Flyway** to automatically execute migrations and create tables, indexes, and constraints.

#### Step 1: Create the PostgreSQL Database
Open PostgreSQL CLI (`psql`) or pgAdmin and run:
```sql
CREATE DATABASE job_profile_manager;
```

#### Step 2: Set Environment Variables
Configure your database connection parameters:

**Windows PowerShell:**
```powershell
$env:JAVA_HOME = "C:\Users\Shariff\.jdks\openjdk-21"
$env:DB_HOST = "localhost"
$env:DB_PORT = "5432"
$env:DB_NAME = "job_profile_manager"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = "your_postgres_password_here"
```

**Windows CMD:**
```cmd
set JAVA_HOME=C:\Users\Shariff\.jdks\openjdk-21
set DB_HOST=localhost
set DB_PORT=5432
set DB_NAME=job_profile_manager
set SPRING_DATASOURCE_USERNAME=postgres
set SPRING_DATASOURCE_PASSWORD=your_postgres_password_here
```

**Linux / macOS:**
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=job_profile_manager
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=your_postgres_password_here
```

#### Step 3: Run the Application
```powershell
.\mvnw.cmd spring-boot:run
```
*(On Linux/macOS: `./mvnw spring-boot:run`)*

On startup, Flyway will automatically execute `V1__init_schema.sql` and output:
```
Successfully applied 1 migration to schema "public" (execution time 00:00.045s)
Started JobProfileManagerApplication in 3.5 seconds
```

---

## Interactive API Testing

Once the application is running:

### 1. Interactive Swagger UI
Open your browser to:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI 3.0 JSON Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- **Actuator Health**: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

---

### 2. Sample API Requests (cURL / PowerShell)

#### A. Health Check
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -Method Get
```
*Expected response:*
```json
{
  "status": "UP"
}
```

#### B. Create Master Profile (`POST /api/v1/profiles`)
```powershell
$body = @{
    fullName = "Jane Doe"
    email = "jane.doe@example.com"
    currentDesignation = "Senior Cloud Architect"
    headline = "Senior Enterprise Architect & Java 21 Specialist"
    location = "San Francisco, CA"
    summary = "Architect with 10+ years designing enterprise distributed systems."
    linkedinUrl = "https://linkedin.com/in/janedoe"
    githubUrl = "https://github.com/janedoe"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/v1/profiles" -Method Post -Body $body -ContentType "application/json"
```
*Expected response (`201 Created` with Location header):*
```json
{
  "success": true,
  "message": "Profile created successfully",
  "data": {
    "id": "262f063b-0c86-48d2-a622-c94c1a88f676",
    "fullName": "Jane Doe",
    "email": "jane.doe@example.com",
    "headline": "Senior Enterprise Architect & Java 21 Specialist",
    "currentDesignation": "Senior Cloud Architect",
    "status": "DRAFT",
    "version": 0,
    "createdAt": "2026-11-02T09:34:36Z",
    "updatedAt": "2026-11-02T09:34:36Z"
  }
}
```

#### C. Retrieve Profile by ID (`GET /api/v1/profiles/{id}`)
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/profiles/262f063b-0c86-48d2-a622-c94c1a88f676" -Method Get
```

#### D. Update Profile (`PUT /api/v1/profiles/{id}`)
```powershell
$updateBody = @{
    fullName = "Jane Doe"
    email = "jane.doe@example.com"
    currentDesignation = "Principal Software Architect"
    headline = "Principal Cloud & Distributed Systems Architect"
    location = "Seattle, WA"
    summary = "12+ years designing resilient, high-throughput cloud architectures."
    status = "ACTIVE"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/v1/profiles/262f063b-0c86-48d2-a622-c94c1a88f676" -Method Put -Body $updateBody -ContentType "application/json"
```

#### E. Delete Profile (`DELETE /api/v1/profiles/{id}`)
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/v1/profiles/262f063b-0c86-48d2-a622-c94c1a88f676" -Method Delete
```
*Expected response: HTTP 204 No Content*

---

## Running Automated Tests

The project includes unit tests with Mockito and web-slice integration tests with Spring MockMvc:

```powershell
# Set JAVA_HOME
$env:JAVA_HOME = "C:\Users\Shariff\.jdks\openjdk-21"

# Execute test suite
.\mvnw.cmd test
```

### Test Suite Summary:
- **`JobProfileManagerApplicationTests`**: Verifies full Spring ApplicationContext bootstrapping.
- **`ProfileControllerTest`**: Tests MockMvc REST serialization, validation rules, HTTP status codes (`201`, `200`, `204`, `400`, `404`), and error envelopes.
- **`ProfileServiceTest`**: Validates transactional business logic, duplicate email enforcement, mapper conversions, and repository interactions.
- **Execution Result**: **14 tests, 0 failures, 0 errors, 100% pass rate**.

---

## Configuration & Environment Reference

All properties can be customized via environment variables:

| Environment Variable | Property Key | Default Value | Description |
| :--- | :--- | :--- | :--- |
| `SERVER_PORT` | `server.port` | `8080` | HTTP port |
| `DB_HOST` | `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `DB_NAME` | `job_profile_manager` | PostgreSQL database name |
| `SPRING_DATASOURCE_USERNAME` | `spring.datasource.username` | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `spring.datasource.password` | `postgres` | Database password |
| `SPRING_PROFILES_ACTIVE` | `spring.profiles.active` | *none* | Active profile (`local`, `test`, etc.) |

---

## Packaging an Executable JAR

To build a standalone executable JAR:
```powershell
.\mvnw.cmd clean package
```

Run the built JAR:
```powershell
# Standalone local in-memory mode:
java -jar target/job-profile-manager-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

# Or connected to PostgreSQL:
java -jar target/job-profile-manager-0.0.1-SNAPSHOT.jar
```

---

## Future Roadmap

- [x] **Milestone 1**: Project setup, application foundation & Master Profile CRUD
- [ ] **Milestone 2**: Master professional profile management (work experiences, technical skills, projects, education, certifications)
- [ ] **Milestone 3**: Resume and profile version management
- [ ] **Milestone 4**: AI-powered summary and experience optimisation (factual, non-hallucinatory)
- [ ] **Milestone 5**: Job description analysis and ATS keyword matching
- [ ] **Milestone 6**: Platform integration adapter architecture (LinkedIn, Naukri, Indeed, Glassdoor, GitHub, Monster, Foundit)
- [ ] **Milestone 7**: OAuth connections and authorised profile synchronisation
- [ ] **Milestone 8**: React + TypeScript dashboard
- [ ] **Milestone 9**: Docker deployment, security hardening and production readiness
