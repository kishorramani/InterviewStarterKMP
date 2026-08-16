# 🎯 TechPulse KMP — Technical Interview Study Guide

This guide details the top **10 Kotlin Multiplatform (KMP) & Compose Multiplatform interview questions** with in-depth technical explanations based on the **TechPulse KMP** codebase.

---

### Q1: How does Compose Multiplatform share UI across Android and iOS?
**Answer:**
- On **Android**, Compose Multiplatform leverages native Jetpack Compose, compiling to JVM bytecode and rendering directly to the Android Canvas / Skia pipeline.
- On **iOS**, JetBrains ports Compose using **Skiko (Skia for Kotlin)** to draw UI frames on a native `CAMetalLayer` / `UIKitViewController`.
- In this project (`shared/src/iosMain`), `MainViewController` wraps the shared `App()` composable inside a `ComposeUIViewController`, allowing iOS host apps (`iosApp`) to display shared UI seamlessly.

---

### Q2: Why did you choose Clean Architecture + MVI for this application?
**Answer:**
- **Clean Architecture** isolates business logic (`domain`) from framework details (`data` / `presentation`). The `domain` layer has zero dependencies on Compose, Ktor, or Android/iOS SDKs, making it 100% testable.
- **MVI (Model-View-Intent)** enforces **Unidirectional Data Flow (UDF)**:
  - **State**: Single immutable `UiState` StateFlow per screen (`FeedUiState`, `BookmarksUiState`).
  - **Intent**: Strongly-typed user actions (`FeedUiIntent.ToggleBookmark`, `FeedUiIntent.SelectCategory`).
  - **Effect**: One-time side effects (`FeedUiEffect.ShowToast`, `NavigateToDetail`) via `SharedFlow`.

---

### Q3: How does Room KMP work natively on iOS?
**Answer:**
- `androidx.room` uses Kotlin Multiplatform source sets. On iOS, it uses `sqlite-bundled` (a pre-compiled native C SQLite binary) driven by Kotlin/Native C-Interop (`cinterop`).
- DAOs and Entities are written once in `commonMain` and compile to native SQLite queries across all platform targets.

---

### Q4: How is Dependency Injection handled with Koin Multiplatform?
**Answer:**
- Koin Multiplatform uses lightweight service locator DSLs (`module { single { ... } }`, `viewModelOf(...)`) without code generation or reflection bloat.
- Modules (`networkModule`, `databaseModule`, `repositoryModule`, `useCaseModule`, `viewModelModule`) are initialized via `KoinApplication` in Compose or `initKoin()` on app start.
- Screens resolve dependencies via `koinViewModel()` and `koinInject()`.

---

### Q5: How do you bridge platform-specific APIs in KMP using `expect` / `actual`?
**Answer:**
- In `commonMain`, `expect class HapticFeedback()` defines the interface contract.
- In `androidMain`, `actual class HapticFeedback()` uses Android's `Vibrator` / `VibratorManager`.
- In `iosMain`, `actual class HapticFeedback()` uses iOS's `UIImpactFeedbackGenerator`.
- This decouples shared UI from underlying OS SDKs.

---

### Q6: How does Ktor HTTP Client isolate engine dependencies per target?
**Answer:**
- In `commonMain`, `KtorClientFactory` builds an `HttpClient` using shared plugins (`ContentNegotiation`, `kotlinx.serialization`, `Logging`).
- In `androidMain`, `ktor-client-okhttp` provides the OkHttp networking engine.
- In `iosMain`, `ktor-client-darwin` provides Apple's native `NSURLSession` engine.

---

### Q7: How does Type-Safe Navigation work in Compose Multiplatform?
**Answer:**
- Using `androidx.navigation:navigation-compose` with Kotlin `@Serializable` destinations:
  ```kotlin
  @Serializable object FeedRoute
  @Serializable data class DetailRoute(val articleId: String)
  ```
- Eliminates String-path typos (`"detail/{id}"`), ensures compile-time route validation, and enables deep-link parameter parsing via `backStackEntry.toRoute<DetailRoute>()`.

---

### Q8: How is Offline-First architecture implemented in TechPulse?
**Answer:**
- Local database storage (`LocalDatabase`) acts as the single source of truth.
- `ArticleRepositoryImpl.getArticles()` emits cached articles immediately from local storage via `Flow`.
- Simultaneously, it triggers an async background fetch from Ktor. Fresh articles update the local database, automatically emitting new state to subscribers.

---

### Q9: How do you unit test UseCases and MVI ViewModels in KMP?
**Answer:**
- Using `kotlin.test` and `kotlinx-coroutines-test`.
- `UnconfinedTestDispatcher` replaces `Dispatchers.Main` during tests via `Dispatchers.setMain()`.
- UseCases and ViewModels are tested deterministically against fake repositories and in-memory databases without requiring Android or iOS emulators.

---

### Q10: What are the build benefits of Gradle Version Catalog (`libs.versions.toml`)?
**Answer:**
- Centralizes all multiplatform library versions (`ktor`, `koin`, `room`, `compose`) into a single declarative TOML file.
- Prevents version mismatch bugs across `androidApp`, `iosApp`, and `shared` modules.
- Simplifies plugin aliasing (`alias(libs.plugins.kotlinMultiplatform)`).
