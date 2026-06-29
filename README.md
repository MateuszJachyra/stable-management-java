# Stable Management

Portfolio project for managing horse stables: a **Spring Boot REST API**, an **Android mobile client**, and a shared **MySQL** database.

## Projects

| Folder | Description | Stack |
|--------|-------------|--------|
| [stable-management-rest](stable-management-rest/) | HTTP JSON API | Java 21, Spring Boot 4, JPA, MySQL |
| [stable-management-mobile](stable-management-mobile/) | Android client | Kotlin, Jetpack Compose, Retrofit, Koin |
| [database](database/) | Schema + sample data | MySQL 8 dump |
| stable-management-console | Legacy terminal UI (optional) | Java, Maven, JPA |

## Architecture

```text
Android app  --HTTP-->  REST API (:8080)  --JDBC-->  MySQL (:3306)
```

## Quick start (Docker)

**Prerequisites:** [Docker Desktop](https://www.docker.com/products/docker-desktop/)

From the repository root:

```bash
docker compose up --build
```

This starts:

- **MySQL 8** on port `3306` with database `stables_management` (seeded from the SQL dump on first run)
- **REST API** on [http://localhost:8080](http://localhost:8080)

Verify the API:

```bash
curl http://localhost:8080/api/stables
```

Stop containers:

```bash
docker compose down
```

Reset database and re-import the dump:

```bash
docker compose down -v
docker compose up --build
```

### Demo credentials

| Service | User | Password | Notes |
|---------|------|----------|-------|
| MySQL | `root` | `admin` | Used by Docker and local dev |
| Mobile login | `admin` | `admin` | Placeholder UI only; not wired to the API yet |

These are intentional demo values for local development and portfolio review.

## Mobile app

1. Start the backend (`docker compose up` or run the REST app locally).
2. Open `stable-management-mobile` in Android Studio.
3. Run on an emulator - the app calls `http://10.0.2.2:8080` (emulator → host machine).
4. On a physical device, use your PC's LAN IP instead of `10.0.2.2` (change `AppModule.kt`).

See [stable-management-mobile/README.md](stable-management-mobile/README.md) for details.

## Manual setup (without Docker)

### Database

Import the dump:

```bash
mysql -u root -p < database/stables_management_dump.sql
```

Connection: `jdbc:mysql://localhost:3306/stables_management`, user `root`, password `admin`.

See [database/README.md](database/README.md).

### REST API

```bash
cd stable-management-rest
mvn spring-boot:run
```

See [stable-management-rest/README.md](stable-management-rest/README.md) for the full API reference.

## Documentation

- [REST API](stable-management-rest/README.md)
- [Mobile client](stable-management-mobile/README.md)
- [Database](database/README.md)