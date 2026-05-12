# Jal-Sanchay Tracker

Jal-Sanchay Tracker is an Android application built with Kotlin and Jetpack Compose to help households track rainwater harvesting in a simple, visual, and measurable way.

The app lets a user:
- complete a household setup
- enter rainfall data
- calculate estimated water saved
- view a live dashboard with tank progress
- review reports and impact
- read water-saving tips

## Project Purpose

Many households practice rainwater harvesting but do not have a clear way to measure how effective it is. This app turns rainfall and roof catchment data into understandable numbers such as litres saved, tank fill level, and household water impact.

## Main Features

- Modern guided flow:
  - Welcome
  - Setup
  - Data Entry
  - Dashboard
  - Report
  - Impact
  - Tips
- Household setup with:
  - name
  - roof area
  - tank capacity
  - roof material
- Rainfall entry and validation
- Estimated water savings calculation
- Animated tank progress visual on dashboard
- Monthly and total water summary
- Impact score in household water days
- Local persistence using Room database
- Reactive UI updates using `Flow`, `StateFlow`, and Jetpack Compose state
- Back navigation between screens

## Tech Stack

- `Kotlin`
- `Jetpack Compose`
- `Material 3`
- `Room Database`
- `Hilt`
- `MVVM`
- `Kotlin Coroutines`
- `StateFlow` / `Flow`

## App Flow

1. **Welcome**  
   Intro screen with branding and entry into the guided app flow.

2. **Setup**  
   User enters household details such as name, roof area, tank capacity, and roof material.

3. **Data Entry**  
   User enters rainfall in millimeters. The app validates the input and stores the entry.

4. **Dashboard**  
   Shows:
   - today's saved water
   - monthly saved water
   - total saved water
   - household impact
   - recent rainfall history
   - water tank fill progress

5. **Report**  
   Shows the current user's summary report derived from stored Room data.

6. **Impact**  
   Converts saved litres into understandable daily household impact values.

7. **Tips**  
   Displays practical water conservation and rainwater harvesting suggestions.

## Water Calculation

The app estimates harvested water using roof area, rainfall, and roof runoff behavior.

Example formula used in the project logic:

```text
Saved Water = Roof Area x Rainfall x Conversion Factor x Runoff Coefficient
```

The roof material helps determine the runoff efficiency used during calculation.

## Architecture

The app follows a simple MVVM structure:

- **UI**
  - Compose screens and reusable composables
- **ViewModel**
  - `DashboardViewModel`
  - handles validation, screen data, and business state
- **Repository**
  - `UserRepository`
  - `RainfallRepository`
- **Local Data**
  - Room entities
  - DAO interfaces
  - database class
- **Dependency Injection**
  - Hilt module for Room and repositories

## Room Database

The app stores data locally in a Room database named:

```text
jal_sanchay_db
```

### Main Tables

- `user_infrastructure`
  - stores setup details for a user
  - name / user id
  - roof area
  - tank capacity
  - roof material

- `rainfall_log`
  - stores historical rainfall entries
  - rainfall in mm
  - calculated saved water
  - timestamps

- `monthly_report_cache`
  - supports monthly report related data handling

- `app_settings`
  - stores internal app-level settings data

## Real-Time Dashboard Updates

One of the key parts of the app is that the dashboard updates automatically after rainfall is saved.

This happens through:

```text
Room Flow -> Repository -> ViewModel StateFlow -> Compose collectAsState -> UI recomposition
```

When a rainfall entry is inserted:

1. Room saves the new record in `rainfall_log`
2. DAO queries using `Flow` emit updated values
3. `DashboardViewModel` receives the latest totals
4. Compose recomposes the dashboard automatically
5. The tank progress, litres, and recent entries refresh live

## Important Source Files

- Main UI:  
  [MainActivity.kt](/C:/Users/HP/Documents/Codex/2026-05-05/files-mentioned-by-the-user-room/app/src/main/java/com/mindmatrix/jalsanchay/MainActivity.kt)

- ViewModel:  
  [DashboardViewModel.kt](/C:/Users/HP/Documents/Codex/2026-05-05/files-mentioned-by-the-user-room/app/src/main/java/com/mindmatrix/jalsanchay/ui/viewmodel/DashboardViewModel.kt)

- App theme:  
  [Theme.kt](/C:/Users/HP/Documents/Codex/2026-05-05/files-mentioned-by-the-user-room/app/src/main/java/com/mindmatrix/jalsanchay/ui/theme/Theme.kt)

- Room database:  
  [JalSanchayDatabase.kt](/C:/Users/HP/Documents/Codex/2026-05-05/files-mentioned-by-the-user-room/app/src/main/java/com/mindmatrix/jalsanchay/data/local/JalSanchayDatabase.kt)

- Rainfall entity:  
  [RainfallLogEntity.kt](/C:/Users/HP/Documents/Codex/2026-05-05/files-mentioned-by-the-user-room/app/src/main/java/com/mindmatrix/jalsanchay/data/local/entity/RainfallLogEntity.kt)

- User setup entity:  
  [UserInfrastructure.kt](/C:/Users/HP/Documents/Codex/2026-05-05/files-mentioned-by-the-user-room/app/src/main/java/com/mindmatrix/jalsanchay/data/local/entity/UserInfrastructure.kt)

## Project Structure

```text
app/
  src/main/java/com/mindmatrix/jalsanchay/
    data/
      local/
      repository/
    di/
    ui/
      theme/
      viewmodel/
    MainActivity.kt
    JalSanchayApplication.kt
```

## How to Open in Android Studio

1. Open **Android Studio**
2. Select **File > Open**
3. Open this folder:

```text
C:\Users\HP\Documents\Codex\2026-05-05\files-mentioned-by-the-user-room
```

4. Wait for Gradle Sync to complete
5. Choose an emulator or connect an Android device
6. Run the app

## Build Notes

- Recommended Gradle wrapper is configured in the project
- If Gradle download times out, retry on a stable connection or use a local Gradle installation in Android Studio settings
- AndroidX must remain enabled in `gradle.properties`

## How to Verify Room Database

To inspect user-entered data:

1. Run the app
2. Add setup and rainfall entries
3. In Android Studio open:

```text
View > Tool Windows > App Inspection
```

4. Open **Database Inspector**
5. Select:

```text
jal_sanchay_db
```

6. View tables such as:

```text
user_infrastructure
rainfall_log
```

## Testing

The project includes a Room DAO instrumented test:

- [RainfallLogDaoTest.kt](/C:/Users/HP/Documents/Codex/2026-05-05/files-mentioned-by-the-user-room/app/src/androidTest/java/com/mindmatrix/jalsanchay/data/local/RainfallLogDaoTest.kt)

## Current Scope

This project is designed as a locally running Android app for internship/demo use. Data is stored on the device using Room and is not synced to a cloud backend.

## Future Improvements

- cloud backup and sync
- charts for rainfall history
- notifications and reminders
- multilingual support
- multi-user authentication
- exportable monthly reports

## Author Notes

This app was developed as an internship-style project focused on:

- Android app development using Kotlin
- Jetpack Compose UI
- Room database integration
- MVVM architecture
- practical sustainability use case design
