# Stable Management - REST API

Spring Boot HTTP API for managing horse stables. Used by the **Android mobile client** and shareable with any HTTP client.

**Base URL:** `http://localhost:8080`

## What it does

- CRUD on **stables** and **horses**
- **Ratings** per horse (1–5, optional comment)
- DTOs for request validation and JSON responses
- Central error handling via `GlobalExceptionHandler`

**Authentication:** none (open endpoints for demo / portfolio).

## Tech stack

- Java 21, Maven
- Spring Boot 4.x (Web, Data JPA, Validation)
- MySQL 8
- Docker (optional, see below)

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

## How to run

### Docker (recommended)

From the repository root:

```bash
docker compose up --build
```

Starts MySQL + this API. See [../README.md](../README.md).

### Local (IDE / Maven)

**Prerequisites:** MySQL with database `stables_management` (see [../database/README.md](../database/README.md)).

```bash
cd stable-management-rest
mvn spring-boot:run
```

Or run `app.StableManagerApplication` from your IDE.

Server listens on port **8080**.

## Configuration

`src/main/resources/application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/stables_management
spring.datasource.username=root
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
```

In Docker, datasource settings are overridden by environment variables in `docker-compose.yml`.

## API reference

All requests with a body must use:

```http
Content-Type: application/json
```

### Stables - `/api/stables`

| Method | Path | Description | Success |
|--------|------|-------------|---------|
| `GET` | `/api/stables` | List all stables | `200` |
| `GET` | `/api/stables/{name}` | Get one stable by name | `200` |
| `POST` | `/api/stables` | Create stable | `201` |
| `PUT` | `/api/stables/{name}` | Update stable capacity | `200` |
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
  "capacity": 10,
  "horseCount": 0,
  "fillPercentage": 0.0
}
```

```bash
curl -X POST http://localhost:8080/api/stables \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"north\",\"capacity\":10}"
```

#### `PUT /api/stables/{name}` - update capacity

```bash
curl -X PUT http://localhost:8080/api/stables/north \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"north\",\"capacity\":15}"
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
| `PUT` | `/api/horses/{id}` | Update horse | `200` |
| `DELETE` | `/api/horses/{id}` | Delete horse | `204` |
| `GET` | `/api/horses/{id}/ratings` | List ratings for horse | `200` |
| `POST` | `/api/horses/{id}/ratings` | Add rating | `201` |
| `PUT` | `/api/horses/{id}/ratings/{ratingId}` | Update rating | `200` |
| `DELETE` | `/api/horses/{id}/ratings/{ratingId}` | Delete rating | `204` |

#### Horse enums (JSON - uppercase)

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

```bash
curl -X POST http://localhost:8080/api/horses \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Bob\",\"breed\":\"Arabian\",\"type\":\"WARMBLOODED\",\"status\":\"HEALTHY\",\"age\":8,\"price\":5000,\"weight\":450,\"stableName\":\"north\"}"
```

#### `POST /api/horses/{id}/ratings` - add rating

**Body:**

```json
{
  "value": 4,
  "comment": "Good temperament"
}
```

`value` must be between **1** and **5**.

```bash
curl -X POST http://localhost:8080/api/horses/1/ratings \
  -H "Content-Type: application/json" \
  -d "{\"value\":4,\"comment\":\"Good temperament\"}"
```

#### `GET /api/horses/{id}/ratings`

```bash
curl http://localhost:8080/api/horses/1/ratings
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

## Response DTO fields

**StableResponseDTO:** `id`, `name`, `capacity`, `horseCount`, `fillPercentage`

**HorseResponseDTO:** `id`, `name`, `breed`, `type`, `status`, `age`, `price`, `weight`, `stableName`, `averageRating`

**RatingResponseDTO:** `id`, `horseId`, `value`, `comment`

## Suggested workflow

1. `POST /api/stables` - create a stable
2. `POST /api/horses` - add a horse (`stableName` from step 1)
3. `POST /api/horses/{id}/ratings` - rate the horse
4. `GET /api/stables/{name}/horses` - see sorted list with `averageRating`
5. `GET /api/stables` - see occupancy (`horseCount`, `fillPercentage`)

## Mobile client

The Android app in [../stable-management-mobile](../stable-management-mobile/) consumes this API via Retrofit.

## Troubleshooting

| Problem | Check |
|---------|--------|
| App won't start | MySQL running; credentials in `application.properties` or Docker env |
| Docker DB empty after failed init | `docker compose down -v` then `up` again; verify dump path in `docker-compose.yml` |
| `404` on create horse | `stableName` must match an existing stable |
| `400` invalid enum | Use exact names: `WARMBLOODED`, not `Warmblooded` |
| `400` validation | `capacity` >= 1 on stable; rating `value` in 1–5 |
| Port in use | Change `server.port` or stop other process on 8080 |
