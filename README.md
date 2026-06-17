# Run Locally Without Docker

This project is a Spring Boot application that uses MySQL and Flyway.

## Prerequisites

- Java 17
- MySQL 8.x
- Bash or terminal access
- Optional: Maven installed globally

## 1. Create the local database

Open MySQL and create the database used by the app:

```sql
CREATE DATABASE ecommerce;
```

## 2. Configure database credentials

The app currently reads its database settings from `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=password
```

You have 2 options:

1. Use a local MySQL user that matches those values.
2. Change the values in `src/main/resources/application.properties` to match your machine.

If you do not want to edit the file, you can also override them at runtime:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/ecommerce \
SPRING_DATASOURCE_USERNAME=your_mysql_user \
SPRING_DATASOURCE_PASSWORD=your_mysql_password \
./mvnw spring-boot:run
```

## 3. Start the application

From the project root:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

If you already have Maven installed globally, this also works:

```bash
mvn spring-boot:run
```

The app will start on:

```text
http://localhost:8080
```

## 4. What happens on first run

- Flyway runs the migration in `src/main/resources/db/migration/V1__users.sql`
- A default admin account is created automatically if it does not exist yet

Default admin credentials:

- Username: `admin`
- Password: `password`

## 5. Quick login test

Once the app is running, test authentication:

```bash
curl -X POST http://localhost:8080/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

Example success response:

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

## Troubleshooting

If startup fails:

- Make sure MySQL is running.
- Make sure the `ecommerce` database exists.
- Make sure the username and password in `application.properties` match your local MySQL account.
- Make sure Java 17 is installed by running `java -version`.
- If `./mvnw` cannot download Maven, use a machine with internet access or run the project with a locally installed Maven version.
