# Database - `stables_management`

Shared MySQL database for both **stable-management-console** and **stable-management-rest**.

## Files

| File | Description |
|------|-------------|
| `stable_management_dump.sql` | Full export: schema + data (stables, horses, ratings, users) |

## Import

### Option A - MySQL Workbench

1. Start MySQL server.
2. **Server -> Data Import**
3. **Import from Self-Contained File** -> select `stable_management_dump.sql`
4. Start import.

### Option B - Command line

```bash
mysql -u root -p < stable_management_dump.sql
```

## Connection settings (both apps)

| Setting | Value |
|---------|--------|
| URL | `jdbc:mysql://localhost:3306/stables_management` |
| Username | `root` |
| Password | `admin` |

Change these in:

- **Console:** `stable-management-console/src/main/resources/META-INF/persistence.xml` and `config/DbConfig.java`
- **REST:** `stable-management-rest/src/main/resources/application.properties`

## Tables (overview)

| Table | Purpose |
|-------|---------|
| `users` | Login accounts (`username`, `password_hash`, `user_role`) |
| `stables` | Stable name and max capacity |
| `horses` | Horse details, FK to `stables` |
| `ratings` | 1-5 ratings per horse, FK to `horses` |

## Default admin account

The dump includes a single administrator:

| Username | Password | Role |
|----------|----------|------|
| `admin` | `admin` | `ADMIN` |

Password is stored as a **BCrypt hash** in the database - the plain text `admin` is only what you type at login.

- **Console app:** use this account for full admin menu access.
- **REST app:** does not use login; this account is only relevant for the console.

New users created via **Register** in the console app are always assigned role `USER`.

## Empty database (no dump)

If you start without importing:

1. Run either app once - Hibernate will create tables (`update` mode).
2. For console login, insert an admin user manually or register a user (will be `USER` only, not admin).
3. Create stables/horses via menu or REST API.

Importing the dump is recommended for a ready-to-demo dataset.
