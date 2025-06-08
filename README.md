# Saving Account

This project provides a minimal Spring Boot service demonstrating online registration for a banking application.


## Docker

To build and run the service with MySQL locally using Docker Compose:

```bash
  docker compose up --build
```

This command starts both a MySQL container and the Spring Boot application.

```bash
  docker compose down -v
```

This command stop both a MySQL container and the Spring Boot application.

## API documentation

After running the application, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```
