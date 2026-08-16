# TechPulse KMP — System Architecture

**TechPulse KMP** is an **Offline-First Tech & Developer Aggregator** built to showcase enterprise-grade **Kotlin Multiplatform (KMP)** and **Compose Multiplatform** architecture.

---

## 🏗️ Architecture Diagram

```mermaid
graph TD
    subgraph Presentation Layer [Presentation Layer (Compose Multiplatform & MVI)]
        UI[Compose UI Screens<br/>Feed, Detail, Bookmarks, Inspector]
        VM[ViewModels<br/>FeedViewModel, BookmarksViewModel, SettingsViewModel]
        MVI[MVI State Engine<br/>UiState, UiIntent, UiEffect]
        UI -->|Emits Intent| VM
        VM -->|Updates| MVI
        MVI -->|Renders| UI
    end

    subgraph Domain Layer [Domain Layer (Pure Kotlin Core)]
        UC[UseCases<br/>GetArticles, SearchArticles, ToggleBookmark, GetMetrics]
        DM[Domain Models<br/>Article, Category, PlatformInfo]
        RI[Repository Interfaces<br/>ArticleRepository, PlatformRepository]
        VM -->|Executes| UC
        UC -->|Calls| RI
        RI -->|Returns| DM
    end

    subgraph Data Layer [Data Layer (Offline-First Single Source of Truth)]
        RImpl[ArticleRepositoryImpl]
        DB[Local Database<br/>Room KMP / SQLite Store]
        API[Ktor API Service<br/>Remote HTTP Client + Mock Fallback]
        RI -->|Implemented By| RImpl
        RImpl -->|Reads / Writes| DB
        RImpl -->|Syncs Async| API
    end

    subgraph Platform Abstraction [Platform Layer (Expect / Actual Bridge)]
        EXP[expect class PlatformCapabilities]
        AND[actual Android<br/>Context, Vibrator, Intent Share/Url]
        IOS[actual iOS<br/>UIKit, UIImpactFeedback, UIActivityViewController]
        EXP <--> AND
        EXP <--> IOS
    end

    RImpl -->|Uses| EXP
```

---

## 🧩 Architectural Principles

### 1. Clean Architecture (3 Layers)
- **`domain`**: Contains pure Kotlin entities, repository contracts, and single-responsibility UseCases. No platform or UI dependencies.
- **`data`**: Manages remote networking (Ktor) and local persistence (Room KMP). Maps raw network/database DTOs into immutable Domain Entities.
- **`presentation`**: Houses Compose Multiplatform UI components, design system tokens, and ViewModels.

### 2. MVI (Model-View-Intent)
- **Single Source of Truth**: Immutable `UiState` holds all data required to render the screen.
- **Unidirectional Data Flow**:
  - UI emits user **Intents** (`SelectCategory`, `SearchQueryChanged`, `ToggleBookmark`).
  - ViewModels process Intents inside coroutines and update `UiState`.
  - One-time events (`ShowToast`, `NavigateToDetail`) are emitted via `UiEffect` SharedFlow.

### 3. Offline-First Synchronization
- Local storage acts as the single source of truth.
- On launch, articles emit immediately from local storage (zero load lag).
- Ktor fetches fresh news in the background and updates local storage reactively.

### 4. Expect / Actual Platform Isolation
- Platform capabilities (haptic feedback, system URL launcher, share sheet, platform hardware metrics) are declared as `expect` contracts in `commonMain` and implemented natively in `androidMain` and `iosMain`.
