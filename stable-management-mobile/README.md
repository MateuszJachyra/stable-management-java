# Stable Management - Mobile Client

Android app for the Stable Management system. Built with **Kotlin** and **Jetpack Compose**.

## Features

- List stables with capacity and fill percentage
- Stable details: horse list, add/edit/delete horses
- Horse details: view info, manage ratings
- CRUD via bottom-sheet forms

**Authentication:** placeholder login screen (`admin` / `admin`). Not connected to the API yet.

## Tech stack

- Kotlin, Jetpack Compose, Material 3
- Retrofit + Gson
- Koin (DI, ViewModels)
- Navigation Compose
- MVVM (`StateFlow` + `collectAsState`)

## Prerequisites

- Android Studio (recent version with Compose support)
- Android SDK 34
- Backend running on port **8080** (see root [README](../README.md))

## How to run

1. Start the backend from the repo root:

   ```bash
   docker compose up --build
   ```

2. Open this folder in Android Studio.
3. Run on an **Android emulator**.

### API URL

The app is configured for the emulator in `app/src/main/java/.../di/AppModule.kt`:

```kotlin
.baseUrl("http://10.0.2.2:8080")
```

`10.0.2.2` is the emulator's alias for your host machine's `localhost`.

**Physical device:** replace with your computer's LAN IP (e.g. `http://192.168.1.10:8080`). Phone and PC must be on the same network, and port 8080 must be reachable.

## Project structure

```text
app/src/main/java/com/example/stable_management_mobile/
  data/remote/          Retrofit API interfaces + DTOs
  data/repository/      Repository implementations
  domain/               Repository interfaces, enums
  ui/screens/           Compose screens + ViewModels
  navigation/           NavHost, routes
  di/                   Koin modules
```

## Screens

| Screen | Description |
|--------|-------------|
| Login | Placeholder; any navigation to stables after `admin`/`admin` |
| Stables | List all stables, add/edit/delete |
| Stable details | Stable info, horses in stable |
| Horse details | Horse info, ratings list |

## Related docs

- [Root README](../README.md) - Docker quick start
- [REST API](../stable-management-rest/README.md) - endpoint reference
