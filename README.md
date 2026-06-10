# Stable Management - Java

Two related applications for managing horse stables: a **console app** with login and role-based menus, and a **REST API** built with Spring Boot. Both use the same MySQL database (`stables_management`).

## Projects

| Folder | Description | Stack |
|--------|-------------|--------|
| [stable-management-console](stable-management-console/) | Terminal UI - login, admin/user menus, CRUD, CSV import/export | Java, Maven, JPA/Hibernate, JDBC, MySQL |
| [stable-management-rest](stable-management-rest/) | HTTP JSON API - same domain, no authentication | Spring Boot, Spring Data JPA, DTOs, MySQL |

## Prerequisites

- **Java 25** (or adjust `pom.xml` compiler version to match your JDK)
- **Maven 3.9+**
- **MySQL 8** (MySQL Workbench or CLI)
- **Postman** or **curl** (for REST app)

## Database setup

Both apps connect to:

- **Database:** `stables_management`
- **Host:** `localhost:3306`
- **User / password:** `root` / `admin` (see config files in each project)

### Import sample data

A full dump is provided at [`database/stable_management_dump.sql`](database/stable_management_dump.sql). It includes tables, sample stables/horses/ratings, and a pre-seeded admin user.

**MySQL Workbench:** Server -> Data Import -> Import from Self-Contained File -> select `database/stable_management_dump.sql`

**Command line:**

```bash
mysql -u root -p < database/stable_management_dump.sql
```

See [database/README.md](database/README.md) for details.

> **Note:** Hibernate `ddl-auto=update` (REST) and `hbm2ddl.auto=update` (console) can alter schema on startup. Importing the dump first gives you consistent sample data.

## Quick start

### Console app

```bash
cd stable-management-console
mvn compile
```

Run the `Main` class from your IDE, or:

```bash
mvn exec:java "-Dexec.mainClass=Main"
```

(Requires [exec-maven-plugin](https://www.mojohaus.org/exec-maven-plugin/) if not configured - IDE run is simplest.)

**Login:** `admin` / `admin` (from database dump)

### REST API

```bash
cd stable-management-rest
mvn spring-boot:run
```

API base URL: **http://localhost:8080**

Full endpoint list and Postman examples: [stable-management-rest/README.md](stable-management-rest/README.md)

## Architecture overview

```text
Console app                          REST app
-----------                          --------
AuthMenu / AdminMenu / UserMenu  ->  StableController / HorseController
        ↓                                    ↓
   StableService (JPA)              StableService (@Transactional)
        ↓                                    ↓
   Repositories + JDBC (users)       Spring Data JpaRepository
        ↓                                    ↓
              MySQL - stables_management
```

- **Console:** users authenticated via JDBC + BCrypt; new registrations get `USER` role only.
- **REST:** no API authentication (open endpoints for demo purposes).

Example / portfolio code - use freely for learning and demonstration.
