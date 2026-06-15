# Stable Management - REST API

Spring Boot HTTP API for managing horse stables. Same MySQL database as the console app.

**Base URL:** `http://localhost:8080`

## What it does

- CRUD operations on **stables** and **horses** (by stable name or horse id).
- **Ratings** per horse (1-5).
- **DTOs** for request validation and safe JSON responses (no entity cycles).
- Central **error handling** via `GlobalExceptionHandler`.

## Tech stack

- Java 25, Maven
- Spring Boot 4.x (Web, Data JPA, Validation)
- MySQL 8
- Hibernate (via Spring Data JPA)

## Project structure

```text
src/main/java/
  app/StableManagerApplication.java   Spring Boot entry point
  controller/                         REST endpoints
  service/StableService.java          Business logic (@Transactional)
  repository/                         Spring Data JpaRepository
  model/                              JPA entities
  dto/                                Request/response DTOs
  exception/GlobalExceptionHandler.java
```

## Prerequisites

1. MySQL with database `stables_management` (see [../database/README.md](../database/README.md)).
2. Optional: import `../database/stable_management_dump.sql` for sample data.

## How to run

```bash
cd stable-management-rest
mvn spring-boot:run
```

Or run `app.StableManagerApplication` from your IDE.

Server starts on port **8080** (configurable in `application.properties`).

## Configuration

`src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/stables_management
spring.datasource.username=root
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
```

## Authentication

**None.** All endpoints are open. User accounts (`admin` / `admin`) exist in the database for the **console app only**.

## API reference

All requests with a body must use header:

```http
Content-Type: application/json
```

### Stables - `/api/stables`

| Method | Path | Description | Success |
|--------|------|-------------|---------|
| `GET` | `/api/stables` | List all stables | `200` |
| `GET` | `/api/stables/{name}` | Get one stable by name | `200` |
| `POST` | `/api/stables` | Create stable | `201` |
| `DELETE` | `/api/stables/{name}` | Delete empty stable | `204` |
| `DELETE` | `/api/stables/{name}?force=true` | Delete stable and all horses | `204` |
| `GET` | `/api/stables/{name}/horses` | Horses in stable, sorted by avg rating | `200` |

#### `POST /api/stables` - create stable

**Body:**

```json
{
  "name": "north",
  "capacity": 10
}
```

**Response `201`:**

```json
{
  "id": 1,
  "name": "north",
  "maxCapacity": 10,
  "horseCount": 0,
  "fillPercentage": 0.0
}
```

**curl:**

```bash
curl -X POST http://localhost:8080/api/stables \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"north\",\"capacity\":10}"
```

#### `GET /api/stables`

```bash
curl http://localhost:8080/api/stables
```

#### `GET /api/stables/{name}/horses`

```bash
curl http://localhost:8080/api/stables/north/horses
```

#### `DELETE /api/stables/{name}`

```bash
# Fails with 400 if stable has horses
curl -X DELETE http://localhost:8080/api/stables/north

# Deletes stable and all horses inside
curl -X DELETE "http://localhost:8080/api/stables/north?force=true"
```

---

### Horses - `/api/horses`

| Method | Path | Description | Success |
|--------|------|-------------|---------|
| `GET` | `/api/horses` | List all horses | `200` |
| `GET` | `/api/horses/{id}` | Get horse by id | `200` |
| `POST` | `/api/horses` | Add horse to stable | `201` |
| `DELETE` | `/api/horses/{id}` | Delete horse | `204` |
| `GET` | `/api/horses/{id}/rating` | List all ratings for horse | `200` |
| `POST` | `/api/horses/rating` | Add rating to horse | `201` |

#### Horse enums (JSON values - uppercase)

| Field | Allowed values |
|-------|----------------|
| `type` | `COLDBLOODED`, `WARMBLOODED` |
| `status` | `HEALTHY`, `SICK`, `TRAINING`, `SOLD` |

#### `POST /api/horses` - add horse

**Body:**

```json
{
  "name": "Bob",
  "breed": "Arabian",
  "type": "WARMBLOODED",
  "status": "HEALTHY",
  "age": 8,
  "price": 5000,
  "weight": 450,
  "stableName": "north"
}
```

**Response `201`:**

```json
{
  "id": 1,
  "name": "Bob",
  "breed": "Arabian",
  "type": "WARMBLOODED",
  "status": "HEALTHY",
  "age": 8,
  "price": 5000.0,
  "weight": 450.0,
  "stableName": "north",
  "averageRating": 0.0
}
```

**curl:**

```bash
curl -X POST http://localhost:8080/api/horses \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Bob\",\"breed\":\"Arabian\",\"type\":\"WARMBLOODED\",\"status\":\"HEALTHY\",\"age\":8,\"price\":5000,\"weight\":450,\"stableName\":\"north\"}"
```

#### `POST /api/horses/rating` - add rating

**Body:**

```json
{
  "horseId": 1,
  "value": 4
}
```

`value` must be between **1** and **5**.

**Response `201`:**

```json
{
  "horseId": 1,
  "rating": 4
}
```

**curl:**

```bash
curl -X POST http://localhost:8080/api/horses/rating \
  -H "Content-Type: application/json" \
  -d "{\"horseId\":1,\"value\":4}"
```

#### `GET /api/horses/{id}/rating`

```bash
curl http://localhost:8080/api/horses/1/rating
```

---

## Error responses

Handled by `GlobalExceptionHandler`:

| Situation | Status | Example body |
|-----------|--------|----------------|
| Not found | `404` | `{"error":"Stable not found"}` |
| Bad request / business rule | `400` | `{"error":"Stable not empty"}` |
| Validation failed (`@Valid`) | `400` | `{"errors":{"name":"Stable name is required"}}` |
| Invalid JSON or enum | `400` | `{"error":"Invalid JSON or enum value"}` |

## Suggested Postman workflow

1. `POST /api/stables` - create a stable.
2. `POST /api/horses` - add a horse (use `stableName` from step 1).
3. `POST /api/horses/rating` - rate the horse (`horseId` from step 2 response).
4. `GET /api/stables/{name}/horses` - see sorted list with `averageRating`.
5. `GET /api/stables` - see occupancy (`horseCount`, `fillPercentage`).

## Response DTO fields

**StableResponseDTO:** `id`, `name`, `maxCapacity`, `horseCount`, `fillPercentage`

**HorseResponseDTO:** `id`, `name`, `breed`, `type`, `status`, `age`, `price`, `weight`, `stableName`, `averageRating`

**RatingResponseDTO:** `horseId`, `rating`

## Relationship to console app

Uses the same `stables_management` database. Data created via Postman appears in the console app and vice versa.

Console app enforces **admin/user** login; this REST API does not.

## Troubleshooting

| Problem | Check |
|---------|--------|
| App won't start | MySQL running, `username`/`password` in `application.properties` |
| `404` on create horse | `stableName` must match an existing stable |
| `400` invalid enum | Use exact enum names: `WARMBLOODED`, not `Warmblooded` |
| `400` validation | `capacity` >= 1 on stable; `horseId` required on rating |
| Port in use | Change `server.port` in `application.properties` |
