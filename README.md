# 🚀 KMP Sample — Cross-Platform Developer Aggregator App

**KMP Sample** is an interview-ready, enterprise-grade Kotlin Multiplatform (KMP) & Compose Multiplatform application designed to showcase modern mobile architecture across **Android** and **iOS**.

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple.svg?style=flat&logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.11.1-blue.svg?style=flat&logo=jetpackcompose)
![Room KMP](https://img.shields.io/badge/Room_KMP-2.7.0--alpha13-green.svg?style=flat)
![Koin](https://img.shields.io/badge/Koin-4.2.2-orange.svg?style=flat)
![Ktor](https://img.shields.io/badge/Ktor-3.1.0-red.svg?style=flat)

---

## ✨ Features & Platform Capabilities

- 📰 **Tech News Feed**: Real-time articles fetched dynamically from the public **Dev.to articles API** with category filtering (*All, AI & ML, Mobile, Web, DevOps*) and real tags.
- 🖼️ **Image Loading**: Cross-platform image loading and caching using **Coil 3** dynamically rendering cover banners on article list cards and details view.
- 💾 **Offline-First Room KMP Database**: Persistent local storage using official **Room KMP (`androidx.room`)** backed by **Bundled SQLite (`androidx.sqlite`)** with automatic destructive migration support for schema modifications.
- 🔍 **Live Search**: Instant article search across titles, summaries, and authors.
- 💾 **Bookmarks**: Reactive bookmarking with instant UI state updates powered by Kotlin Coroutine `Flow`s.
- 🌓 **Dynamic Light / Dark Mode**: Theme toggle with state persisted across app restarts via cross-platform **KeyValueStorage** (`SharedPreferences` on Android, `NSUserDefaults` on iOS).
- ⚙️ **Platform Inspector**: Real-time system metrics screen displaying OS version, CPU Architecture, Device Model, Kotlin version, and Compose version.
- ⚡ **Platform Capabilities (`expect`/`actual`)**: Cross-platform Haptic Feedback, Share Launcher, and External Browser URL Launcher.

---

## 🏛️ Clean Architecture + MVI Pattern

The application strictly follows **Clean Architecture** combined with the **MVI (Model-View-Intent)** presentation pattern:

```text
               ┌──────────────────────────────────────────────┐
               │              Compose UI (View)               │
               └──────────────────────┬───────────────────────┘
                                      │ Dispatches UiIntent
                                      ▼
               ┌──────────────────────────────────────────────┐
               │             ViewModel (MVI Core)             │
               └──────────────────────┬───────────────────────┘
                                      │ Observes / Invokes
                                      ▼
               ┌──────────────────────────────────────────────┐
               │                Use Cases (Domain)            │
               └──────────────────────┬───────────────────────┘
                                      │ Implements
                                      ▼
               ┌──────────────────────────────────────────────┐
               │              Repository (Data)               │
               └──────────────┬────────────────┬──────────────┘
                              │                │
                              ▼                ▼
                     ┌────────────────┐┌───────────────┐
                     │ Ktor (Remote)  ││ Room (Local)  │
                     └────────────────┘└───────────────┘
```

### **MVI Layers**:
- **UiState**: Immutable state data class representing screen state (`FeedUiState`, `SettingsUiState`, `BookmarksUiState`).
- **UiIntent**: Sealed interface representing user actions (`ToggleBookmark`, `SearchQueryChanged`, `ToggleTheme`).
- **UiEffect**: Single-shot events emitted via `SharedFlow` (`ShowToast`, `NavigateToDetail`).

---

## 🛠️ Technology Stack & Libraries

| Technology / Library | Purpose |
|---|---|
| **Kotlin Multiplatform (KMP)** | Core cross-platform code sharing engine |
| **Compose Multiplatform** | Shared declarative Material 3 UI for Android & iOS |
| **Room KMP (`androidx.room`)** | Cross-platform SQLite database ORM |
| **SQLite Bundled (`androidx.sqlite`)** | C-interop native SQLite engine |
| **KSP (`com.google.devtools.ksp`)** | Annotation processing for Room DB code generation |
| **KeyValueStorage (`expect`/`actual`)** | `SharedPreferences` (Android) & `NSUserDefaults` (iOS) |
| **Koin (`koin-core`, `koin-compose`)** | Cross-platform Dependency Injection |
| **Ktor (`ktor-client-core`)** | Networking client (`OkHttp` for Android, `Darwin` for iOS) |
| **Kotlinx Serialization** | JSON parsing & serialization |
| **Coil 3 (`coil-compose`)** | Cross-platform network image loading & disk caching |
| **Navigation Compose Multiplatform** | Shared type-safe screen navigation |

---

## 📁 Project Structure

```text
KMPSample/
├── androidApp/                        # Android Application Module
│   └── src/main/kotlin/               # MainActivity & Android Application entry
├── iosApp/                            # iOS Application Xcode Project
│   └── iosApp/                        # SwiftUI App wrapper calling shared Compose UI
└── shared/                            # Shared Kotlin Multiplatform Module
    └── src/
        ├── commonMain/
        │   └── kotlin/com/kishorramani/kmpsample/
        │       ├── data/
        │       │   ├── local/          # Local Database & KeyValueStorage
        │       │   │   └── room/       # AppDatabase, ArticleDao, ArticleEntity
        │       │   ├── remote/         # Ktor Client & API Service
        │       │   └── repository/     # Repository Implementations
        │       ├── domain/
        │       │   ├── model/          # Core Domain Models & Enums
        │       │   ├── repository/     # Repository Interfaces
        │       │   └── usecase/        # Business Logic Use Cases
        │       ├── presentation/
        │       │   ├── mvi/            # ViewModels, UiStates & UiIntents
        │       │   ├── navigation/     # NavHost & NavRoutes
        │       │   ├── theme/          # MaterialTheme & Color System
        │       │   └── ui/             # Screens & Reusable Composables
        │       └── di/                 # Koin Dependency Injection Modules
        ├── androidMain/                # Android actual implementations & DB Builder
        └── iosMain/                    # iOS actual implementations & DB Builder
```

---

## 🚀 How to Run

### **Android App**:
Run the following Gradle command from terminal:
```bash
./gradlew :androidApp:installDebug
```
Or open the project in **Android Studio (Ladybug / Iguana+)** and select `androidApp` run configuration.

### **iOS App**:
1. Build the shared module framework:
   ```bash
   ./gradlew :shared:embedAndSignAppleFrameworkForXcode
   ```
2. Open `iosApp/iosApp.xcworkspace` in **Xcode**.
3. Select an iOS Simulator or connected iPhone device and hit **Run (Cmd + R)**.

---

## 📚 Interview Guides
- 👔 **Senior Android Lead Guide**: [`SENIOR_ANDROID_LEAD_INTERVIEW_GUIDE.md`](./SENIOR_ANDROID_LEAD_INTERVIEW_GUIDE.md) *(Architecture, Performance, System Design, STAR Behavioral, Leadership & HR Q&A)*
- 🎓 **KMP & Compose Core Guide**: [`KMP_COMPOSE_INTERVIEW_GUIDE.md`](./KMP_COMPOSE_INTERVIEW_GUIDE.md) *(Technical Q&A for KMP, Room, Koin, Ktor & MVI)*