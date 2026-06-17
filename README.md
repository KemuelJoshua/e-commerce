# Ecommerce API

Spring Boot 4 REST API for authentication, categories, roles, and permissions. The app uses MySQL for persistence, Flyway for schema migrations, and JWT for stateless authentication.

## Stack

- Java 17
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Flyway
- MySQL 8
- Maven Wrapper (`./mvnw`)

## Features

- JWT login flow at `/auth/authenticate`
- Role and permission based access control
- Category management endpoints protected by authorities
- Automatic schema migration on startup
- Seeded admin user, role, and permissions for local development

## Prerequisites

- Java 17
- MySQL 8.x
- Bash or terminal access
- Internet access the first time `./mvnw` downloads Maven dependencies

## Local Setup

### 1. Create the database

```sql
CREATE DATABASE ecommerce;
```

### 2. Configure application properties

Use `src/main/resources/application.properties.example` as your template and update the values for your local MySQL instance.

Current required properties:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=your_mysql_user
spring.datasource.password=your_mysql_password
```

You can either:

1. Update `src/main/resources/application.properties`
2. Override the values at runtime with environment variables

Example runtime override:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce \
SPRING_DATASOURCE_USERNAME=your_mysql_user \
SPRING_DATASOURCE_PASSWORD=your_mysql_password \
./mvnw spring-boot:run
```

### 3. Start the app

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

If Maven is installed globally, this also works:

```bash
mvn spring-boot:run
```

App URL:

```text
http://localhost:8080
```

## First Startup Behavior

On first run the application:

- Executes Flyway migration `src/main/resources/db/migration/V1__users.sql`
- Creates base tables for users, roles, permissions, and join tables
- Seeds these permissions when missing:
  - `CATEGORY_READ`
  - `CATEGORY_CREATE`
  - `ROLE_MANAGE`
  - `PERMISSION_MANAGE`
- Ensures an `ADMIN` role exists with those permissions
- Creates a default admin user if `admin` does not already exist

Default admin credentials:

- Username: `admin`
- Password: `password`

## Authentication

Authenticate with:

```bash
curl -X POST http://localhost:8080/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

Successful responses follow this shape:

```json
{
  "status": "success",
  "payload": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "roles": [
        "ADMIN"
      ]
    }
  },
  "error": null
}
```

Use returned token in protected requests:

```bash
curl http://localhost:8080/categories/all \
  -H "Authorization: Bearer YOUR_JWT"
```

## Access Rules

- `/auth/**` is public
- `/roles/**` requires role `ADMIN`
- `/permissions/**` requires role `ADMIN`
- `GET /categories/**` requires authority `CATEGORY_READ`
- `POST /categories/**` requires authority `CATEGORY_CREATE`
- Any other route requires authentication

## Main Endpoints

### Auth

- `POST /auth/authenticate`

### Categories

- `GET /categories/all`
- `POST /categories/create`

### Roles

- `GET /roles/all`
- `POST /roles/create`
- `POST /roles/{roleName}/permissions`
- `POST /roles/assign`

### Permissions

- `GET /permissions/all`
- `POST /permissions/create`

## Running Tests

```bash
./mvnw test
```

## Troubleshooting

- Verify MySQL is running before starting the app.
- Make sure the `ecommerce` database exists.
- Confirm datasource credentials match your local MySQL account.
- Check Java with `java -version`.
- If `./mvnw` cannot download dependencies, use a network-enabled environment or a local Maven installation.
