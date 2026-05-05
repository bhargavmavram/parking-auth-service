# Parking Auth Service

Authentication service for the parking system.

## Responsibilities

- Register users
- Assign roles: `ADMIN`, `USER`, `MANAGER`, `EMPLOYEE`
- Authenticate username/password credentials
- Issue OAuth2-style Bearer JWT access tokens

## Run

```bash
mvn spring-boot:run
```

Service URL: `http://localhost:8082`

## Endpoints

| Method | Path | Description |
| --- | --- | --- |
| GET | `/auth/status` | Service status endpoint. |
| POST | `/auth/register` | Register a user and assign roles. Defaults to `USER` when roles are omitted. |
| POST | `/auth/login` | Login and return a Bearer JWT. |
| GET | `/actuator/health` | Actuator health endpoint. |

## Register Example

```json
{
  "username": "adminuser",
  "email": "admin@example.com",
  "password": "password123",
  "roles": ["ADMIN", "USER"]
}
```

## Login Example

```json
{
  "username": "adminuser",
  "password": "password123"
}
```

## Swagger / OpenAPI

| Resource | URL |
| --- | --- |
| Swagger UI | `http://localhost:8082/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8082/v3/api-docs` |
