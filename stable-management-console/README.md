# Stable Management - Console Application

Terminal-based horse stable manager with **MySQL persistence**, **JPA/Hibernate**, **user login**, and **role-based menus** (admin vs regular user).

## What it does

- Manage **stables** (create, delete, list, occupancy).
- Manage **horses** inside stables (add, remove, list sorted by average rating).
- Add **ratings** (1-5) to horses.
- **Export / import** stable data via CSV.
- **Authenticate** users against the `users` table (BCrypt password hashing).

## Tech stack

- Java 25, Maven
- Hibernate 6 (JPA) - stables, horses, ratings
- JDBC - user authentication
- MySQL 8
- BCrypt (jBCrypt) - password hashing

## Project structure

```text
src/main/java/
  Main.java              Entry point
  app/Session.java       Logged-in user for current run
  ui/                    AuthMenu, AdminMenu, UserMenu
  service/               StableService, UserService, CsvService
  repository/            JPA repositories + JDBC UserRepository
  model/                 Stable, Horse, Rating, User (entities)
  config/                DbConfig, JpaConfig, JpaExecutor
  enumeration/           HorseType, HorseStatus, UserRole
  exception/             Custom exceptions
```

## Prerequisites

1. MySQL running with database `stables_management` (see [../database/README.md](../database/README.md)).
2. Import `../database/stable_management_dump.sql` for sample data and admin user.
3. JDBC settings match your MySQL install (`persistence.xml`, `DbConfig.java` - default `root` / `admin`).

## How to run

```bash
cd stable-management-console
mvn compile
```

Run **`Main`** from your IDE (recommended), or:

```bash
mvn exec:java "-Dexec.mainClass=Main"
```

On startup the app checks the database connection, then shows the **login / register** menu.

## Login & accounts

### Pre-seeded admin (from database dump)

| Username | Password | Role | Access |
|----------|----------|------|--------|
| `admin` | `admin` | `ADMIN` | Full menu - create/delete stables, horses, CSV, ratings |

### Register new user

- Menu option **2. register**
- Password must be at least **5 characters**
- New accounts always get role **`USER`** (cannot self-promote to admin)

### User role (`USER`)

| # | Action |
|---|--------|
| 1 | List stables |
| 2 | List all horses (sorted by rating) |
| 3 | Add rating to horse |
| 4 | Export stable to CSV |
| 0 | Exit |

### Admin role (`ADMIN`)

| # | Action |
|---|--------|
| 1 | List stables |
| 2 | Create stable |
| 3 | Delete stable (prompts to force-delete if not empty) |
| 4 | Add horse |
| 5 | Remove horse |
| 6 | List all horses (sorted) |
| 7 | Add rating to horse |
| 8 | Export stable to CSV |
| 9 | Import stable from CSV |
| 0 | Exit |

## Horse enums

Use these exact values when prompted for type / status:

**HorseType:** `COLDBLOODED`, `WARMBLOODED`

**HorseStatus:** `HEALTHY`, `SICK`, `TRAINING`, `SOLD`

## CSV format

See `example.csv` in this folder.

```text
Line 1: stable name
Line 2: max capacity (integer)
Line 3+: horses (semicolon-separated)
Name;Breed;Type;Status;Age;Price;Weight;[ratings]
```

Example:

```text
stajnia15
20
Dob;Thoroughbred;COLDBLOODED;TRAINING;12;21000.0;500.0;[5, 4, 3]
```

- **Export** writes `{stableName}.csv` in the working directory.
- **Import** asks for a file path (e.g. `example.csv`).

## Configuration

| File | Purpose |
|------|---------|
| `src/main/resources/META-INF/persistence.xml` | JPA / Hibernate + MySQL URL |
| `src/main/java/config/DbConfig.java` | JDBC connection for `UserRepository` |

Default database URL: `jdbc:mysql://localhost:3306/stables_management`

## Troubleshooting

| Problem | Check |
|---------|--------|
| `Failed to connect to database` | MySQL running, database exists, credentials in `DbConfig` / `persistence.xml` |
| `Invalid username or password` | Import dump for `admin` user, or register new user |
| Hibernate SQL noise in console | Already reduced via `Logger` in `Main.java` |