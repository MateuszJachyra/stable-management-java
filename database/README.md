# Database - `stables_management`

Shared MySQL database for the REST API and mobile client.

## Files

| File | Description |
|------|-------------|
| `stables_management_dump.sql` | Schema + sample data (stables, horses, ratings, users) |

## Docker (recommended)

When you run `docker compose up` from the repository root, MySQL automatically:

1. Creates database `stables_management`
2. Runs `stables_management_dump.sql` on **first startup only** (empty volume)

To reset and re-import:

```bash
docker compose down -v
docker compose up --build
```

Connection from the REST container: `jdbc:mysql://db:3306/stables_management`

Connection from your host (IDE, Workbench): `localhost:3306`

| Setting | Value |
|---------|--------|
| Database | `stables_management` |
| Username | `root` |
| Password | `admin` |

## Manual import

### MySQL Workbench

1. Start MySQL server.
2. **Server → Data Import**
3. **Import from Self-Contained File** → select `stables_management_dump.sql`
4. Start import.

### Command line

```bash
mysql -u root -p < stables_management_dump.sql
```

## Configuration in apps

| App | Config file |
|-----|-------------|
| REST (local) | `stable-management-rest/src/main/resources/application.properties` |
| REST (Docker) | Environment variables in `docker-compose.yml` |

## Tables

| Table | Purpose |
|-------|---------|
| `users` | Login accounts (`username`, `password_hash`, `user_role`) |
| `stables` | Stable name and max capacity |
| `horses` | Horse details, FK to `stables` |
| `ratings` | 1–5 ratings per horse, optional comment, FK to `horses` |

## Sample data

The dump includes three stables, several horses, one rating, and an admin user:

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin` | `ADMIN` |

Password is stored as a BCrypt hash. Plain `admin` is what you type at login.

The REST API does not use authentication. The user record is for the legacy console app and future auth work.

## Empty database

If you start without importing:

1. Run the REST app once - Hibernate `ddl-auto=update` creates tables.
2. Create stables and horses via the API or mobile app.

Importing the dump is recommended for a ready-to-demo dataset.
