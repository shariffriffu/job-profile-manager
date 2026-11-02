# Job Profile Manager (JPM)

A production-oriented, centralised career profile management platform built with **Java 21** and **Spring Boot 3.3.x**.

The application serves as a single source of truth for an individual's professional profile, maintaining factually accurate career records (designation, summary, skills, work experience, projects, education, certifications, and resumes) with future support for AI-driven tailoring and multi-platform synchronisation.

---

## Architecture Overview

Job Profile Manager is designed as a **Modular Monolith** adhering to SOLID principles, clean layered architecture, and strict separation of concerns.

```
com.jobmanager
├── config               # Application, OpenAPI and JPA audit configurations
├── security             # Spring Security 6 authentication & authorization filters
├── common               # Base entities, standard response envelopes, exception handling
├── user                 # User identity, credentials, and account lifecycle
├── profile              # Master Career Profile domain module (Milestone 1)
│   ├── controller       # REST API endpoints (/api/v1/profiles)
│   ├── dto              # Request and immutable response DTOs
│   ├── entity           # JPA entities and enums with UUID primary keys
│   ├── mapper           # Decoupled entity-DTO mapping layer
│   ├── repository       # Spring Data JPA repositories
│   └── service          # Transactional business logic & interfaces
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

## Technology Stack

- **Language**: Java 21 (LTS)
- **Framework**: Spring Boot 3.3.5
- **Persistence**: Spring Data JPA / Hibernate 6
- **Database**: PostgreSQL (Production/Dev), H2 In-Memory (Automated Testing)
- **Migrations**: Flyway (`V1__init_schema.sql`)
- **API Documentation**: Springdoc OpenAPI 2.6.0 (Swagger UI 3.0)
- **Security**: Spring Security 6 (Stateless, CSRF disabled for REST)
- **Validation**: Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Email`, `@Size`)
- **Observability**: Spring Boot Actuator (`/actuator/health`, `/actuator/info`)
- **Build Tool**: Apache Maven (with Maven Wrapper `mvnw`)

---

## Database Schema (Milestone 1)

Flyway manages the baseline schema:

- **Table**: `profiles`
  - `id`: UUID Primary Key
  - `user_id`: UUID (Optional link to identity user)
  - `full_name`: VARCHAR(100) NOT NULL
  - `email`: VARCHAR(150) NOT NULL UNIQUE
  - `phone`: VARCHAR(30)
  - `headline`: VARCHAR(200)
  - `current_designation`: VARCHAR(150) NOT NULL
  - `summary`: TEXT
  - `location`: VARCHAR(150)
  - `website_url`: VARCHAR(255)
  - `linkedin_url`: VARCHAR(255)
  - `github_url`: VARCHAR(255)
  - `status`: VARCHAR(20) NOT NULL (DRAFT, ACTIVE, ARCHIVED)
  - `version`: INT NOT NULL (Optimistic locking)
  - `created_at`: TIMESTAMPTZ NOT NULL
  - `updated_at`: TIMESTAMPTZ NOT NULL
- **Indexes**: `idx_profiles_email`, `idx_profiles_user_id`, `idx_profiles_status`

---

## REST API Reference

All responses conform to structured envelope contracts:

- **Success Envelope**:
  ```json
  {
    "success": true,
    "message": "Operation successful",
    "data": { ... },
    "timestamp": "2026-11-02T09:30:00Z"
  }
  ```
- **Error Envelope**:
  ```json
  {
    "timestamp": "2026-11-02T09:30:00Z",
    "status": 400,
    "error": "Validation Failed",
    "message": "Input payload contains validation errors",
    "path": "/api/v1/profiles",
    "validationErrors": {
      "email": "Must be a well-formed email address"
    }
  }
  ```

### Master Profile Endpoints

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/profiles` | Create a new master profile | `201 Created` |
| `GET` | `/api/v1/profiles/{id}` | Retrieve master profile by UUID | `200 OK` / `404 Not Found` |
| `PUT` | `/api/v1/profiles/{id}` | Update profile details | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/v1/profiles/{id}` | Delete profile | `204 No Content` / `404 Not Found` |

### System & Health Endpoints

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/actuator/health` | Application and component health status |
| `GET` | `/swagger-ui.html` | Interactive Swagger UI API documentation |
| `GET` | `/v3/api-docs` | OpenAPI 3.0 JSON specification |

---

## Configuration & Environment Variables

The application can be configured via environment variables or properties:

| Variable | Default Value | Description |
| :--- | :--- | :--- |
| `SERVER_PORT` | `8080` | HTTP port |
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `job_profile_manager` | PostgreSQL database name |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres` | Database password |

---

## Getting Started

### 1. Prerequisites
- **JDK 21** installed and configured (`JAVA_HOME`)
- **PostgreSQL 14+** running locally or in Docker

### 2. Database Setup
Create the PostgreSQL database:
```sql
CREATE DATABASE job_profile_manager;
```

### 3. Build & Run Tests
Execute the automated test suite (uses embedded in-memory H2 in PostgreSQL compatibility mode):
```bash
./mvnw test
# Windows Powershell:
.\mvnw.cmd test
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
# Windows Powershell:
.\mvnw.cmd spring-boot:run
```

Once running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```
Health endpoint:
```
http://localhost:8080/actuator/health
```

---

## Future Roadmap

- **Milestone 1**: Project setup, application foundation & Master Profile CRUD *(Current)*
- **Milestone 2**: Master professional profile management (work experiences, technical skills, projects, education, certifications)
- **Milestone 3**: Resume and profile version management
- **Milestone 4**: AI-powered summary and experience optimisation (factual, non-hallucinatory)
- **Milestone 5**: Job description analysis and ATS keyword matching
- **Milestone 6**: Platform integration adapter architecture (LinkedIn, Naukri, Indeed, Glassdoor, GitHub, Monster, Foundit)
- **Milestone 7**: OAuth connections and authorised profile synchronisation
- **Milestone 8**: React + TypeScript dashboard
- **Milestone 9**: Docker deployment, security hardening and production readiness
