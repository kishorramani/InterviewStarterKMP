# 🎯 MNC Interview Questions & Answers Guide (KMP, Compose Multiplatform & Room)

This guide covers top technical questions asked by MNC interviewers for Kotlin Multiplatform (KMP), Compose Multiplatform, Room KMP, MVI Architecture, and Koin DI based on the TechPulse codebase.

---

## 🏛️ Section 1: KMP & Shared Architecture

### Q1: What is Kotlin Multiplatform (KMP) and how does it differ from Flutter or React Native?
**Answer**:
- **Logic Sharing vs UI Rendering**: KMP allows sharing Kotlin business logic (networking, database, ViewModels, UseCases) while optionally allowing native UI or Shared UI (Compose Multiplatform). Unlike Flutter (which draws everything on a custom Skia canvas) or React Native (which uses JS bridge to native views), KMP compiles directly to native binaries (LLVM native binary `.framework` for iOS, standard DEX/JVM byte-code for Android).
- **No Bridge**: KMP has zero bridge overhead—iOS interacts with shared Kotlin code via direct Objective-C/Swift interop.

---

### Q2: How does the `expect` / `actual` mechanism work in KMP?
**Answer**:
- **`expect`**: Declared in `commonMain` to define a contract (class, object, or function) that platform code must satisfy.
- **`actual`**: Implemented in platform source sets (`androidMain`, `iosMain`) using platform-native APIs.
- *Example*: In our project, `KeyValueStorage` uses `expect` in `commonMain`, with `actual` using `SharedPreferences` in `androidMain` and `NSUserDefaults` in `iosMain`.

---

## 💾 Section 2: Room KMP & Local Data Persistence

### Q3: How does Room KMP work on both Android and iOS?
**Answer**:
1. **Core Runtime (`androidx.room:room-runtime`)**: Provides `@Entity`, `@Dao`, and `@Database` annotations in `commonMain`.
2. **Platform Builders**:
   - On Android: `Room.databaseBuilder(context, name = "techpulse_room.db")`.
   - On iOS: `Room.databaseBuilder(name = dbFilePath, factory = { AppDatabaseConstructor.initialize() }).setDriver(BundledSQLiteDriver())`.
3. **SQLite Engine (`BundledSQLiteDriver`)**: On iOS, SQLite is bundled directly into the app binary via C-interop so it doesn't depend on iOS system SQLite variations.
4. **Code Generation (KSP)**: KSP runs for `kspAndroid`, `kspIosArm64`, and `kspIosSimulatorArm64` to generate `AppDatabase_Impl` for each target.

---

### Q4: Why do we use `@ConstructedBy(AppDatabaseConstructor::class)` in Room KMP?
**Answer**:
In KMP, reflection cannot instantiate classes at runtime on iOS. `@ConstructedBy` tells Room KMP to use a generated factory (`AppDatabaseConstructor.initialize()`) to instantiate `AppDatabase_Impl` without reflection.

---

### Q5: How do you choose between Room KMP and KeyValueStorage?
**Answer**:
- **Room KMP**: Used for relational, structured, queryable data (e.g., cached articles, bookmarks with search/filter queries).
- **KeyValueStorage (`expect`/`actual`)**: Used for lightweight key-value preferences (e.g., `isDarkMode` toggle, user auth tokens).

---

## 🎨 Section 3: Jetpack Compose & MVI Architecture

### Q6: Explain the MVI (Model-View-Intent) pattern in Compose Multiplatform.
**Answer**:
- **Model (`UiState`)**: Immutable data class holding the current screen state (`articles`, `isLoading`, `isDarkMode`).
- **View (Compose UI)**: Renders UI based strictly on `UiState` and dispatches `UiIntent` on user actions.
- **Intent (`UiIntent`)**: Sealed interface representing user actions (`ToggleBookmark`, `SearchQueryChanged`, `ToggleTheme`).
- **Effect (`UiEffect`)**: Single-shot side effects (`ShowToast`, `NavigateToDetail`) emitted via `SharedFlow`.

---

### Q7: How do you handle real-time dynamic dark/light theme switching in Compose Multiplatform?
**Answer**:
1. `SettingsViewModel` observes `KeyValueStorage` for `isDarkMode`.
2. `App.kt` collects `settingsViewModel.uiState.collectAsState()`.
3. `TechPulseTheme(darkTheme = settingsState.isDarkMode)` wraps the root `MaterialTheme`. Toggling the switch updates `isDarkMode` in `KeyValueStorage` and immediately re-themes all composables on screen.

---

## 💉 Section 4: Koin Dependency Injection & Networking

### Q8: How does Koin DI work in KMP across Android and iOS?
**Answer**:
- Koin modules (`networkModule`, `databaseModule`, `repositoryModule`, `viewModelModule`) are declared in `commonMain`.
- On Android, `initKoin` is called in `Application` or `MainActivity`.
- On iOS, `initKoin` is called in Swift `AppDelegate` or `@main App`.

---

### Q9: How do you configure Ktor HTTP client for KMP?
**Answer**:
Ktor uses platform engines:
- `androidMain`: `HttpClient(OkHttp)` engine for Android connection pooling.
- `iosMain`: `HttpClient(Darwin)` engine for iOS `NSURLSession`.
- `commonMain`: Uses `ContentNegotiation` plugin with `kotlinx.serialization` for JSON parsing.
