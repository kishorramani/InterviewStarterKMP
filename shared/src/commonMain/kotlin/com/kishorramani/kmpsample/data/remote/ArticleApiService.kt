package com.kishorramani.kmpsample.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.delay

class ArticleApiService(
    private val client: HttpClient
) {
    suspend fun fetchArticles(): List<ArticleDto> {
        return try {
            // Attempt remote fetch (example endpoint)
            val response: List<ArticleDto> = client.get("https://raw.githubusercontent.com/kishorramani/kmpsample/main/articles.json").body()
            response
        } catch (e: Exception) {
            // Fallback to mock data engine so app functions offline & online out-of-the-box
            delay(400) // Simulate slight network latency
            getMockArticles()
        }
    }

    private fun getMockArticles(): List<ArticleDto> {
        return listOf(
            ArticleDto(
                id = "kmp-compose-1",
                title = "Compose Multiplatform 1.8 Released with Native iOS Performance Boosts",
                summary = "JetBrains announces major optimizations for Compose Multiplatform on iOS, including improved Skiko rendering pipeline and interop improvements.",
                content = "Compose Multiplatform has reached a major milestone with version 1.8. This release features enhanced memory efficiency, faster UI frame rates on iOS devices, and seamless integration with SwiftUI components via UIViewControllerRepresentable. Developers can now build shared UI apps with near-zero overhead.",
                url = "https://kotlinlang.org/docs/multiplatform.html",
                imageUrl = "https://picsum.photos/seed/kmp1/800/450",
                categoryTag = "mobile",
                author = "JetBrains Team",
                publishedAt = "2026-08-12T10:00:00Z",
                readTimeMinutes = 5
            ),
            ArticleDto(
                id = "room-kmp-2",
                title = "Building Offline-First Apps with Room KMP & SQLite Bundled",
                summary = "Google's official Room Database for Kotlin Multiplatform allows full database persistence across Android, iOS, and Desktop.",
                content = "Offline-first architecture is essential for modern mobile applications. With androidx.room expanding to Kotlin Multiplatform, developers can write DAOs and entities once in common code and compile directly to native SQLite engines on Android and iOS.",
                url = "https://developer.android.com/kotlin/multiplatform",
                imageUrl = "https://picsum.photos/seed/room2/800/450",
                categoryTag = "mobile",
                author = "Kishor Ramani",
                publishedAt = "2026-08-11T14:30:00Z",
                readTimeMinutes = 7
            ),
            ArticleDto(
                id = "ai-llm-3",
                title = "On-Device AI Inference with Kotlin & Gemini Nano",
                summary = "Exploring local LLM capabilities on Android & iOS devices using Kotlin native bindings and hardware acceleration.",
                content = "On-device AI provides low-latency inference while safeguarding user privacy. Learn how to integrate small language models into Kotlin Multiplatform applications using ONNX Runtime and hardware NPUs on modern smartphones.",
                url = "https://ai.google.dev",
                imageUrl = "https://picsum.photos/seed/ai3/800/450",
                categoryTag = "ai",
                author = "DeepMind Research",
                publishedAt = "2026-08-10T09:15:00Z",
                readTimeMinutes = 6
            ),
            ArticleDto(
                id = "koin-di-4",
                title = "Clean Architecture & Koin 4.0 in Kotlin Multiplatform",
                summary = "Designing decoupled domain layers, UseCases, and MVI ViewModels using Koin Multiplatform dependency injection.",
                content = "Dependency injection is a core pillar of clean architecture. Koin 4.0 introduces improved ViewModel scope management and lightweight initialization for Compose Multiplatform applications without reflection or code generation bloat.",
                url = "https://insert-koin.io",
                imageUrl = "https://picsum.photos/seed/koin4/800/450",
                categoryTag = "mobile",
                author = "Koin Community",
                publishedAt = "2026-08-09T16:45:00Z",
                readTimeMinutes = 4
            ),
            ArticleDto(
                id = "web-wasm-5",
                title = "Kotlin/Wasm: Compiling Web Applications with WebAssembly",
                summary = "How Kotlin/Wasm enables fast, near-native performance for web applications written completely in Kotlin.",
                content = "WebAssembly (Wasm) is revolutionizing client-side web development. Kotlin/Wasm allows developers to target the browser directly with Compose Multiplatform, opening new possibilities for cross-platform desktop & web software.",
                url = "https://kotlinlang.org/wasm",
                imageUrl = "https://picsum.photos/seed/wasm5/800/450",
                categoryTag = "web",
                author = "WebAssembly Group",
                publishedAt = "2026-08-08T11:20:00Z",
                readTimeMinutes = 8
            ),
            ArticleDto(
                id = "devops-k8s-6",
                title = "Modern Microservices with Kotlin, Ktor & Kubernetes",
                summary = "Architecting resilient, scalable cloud backends using Ktor server framework and automated CI/CD pipelines.",
                content = "Ktor is not just for client applications—it is an asynchronous HTTP framework built for microservices. Explore how Kotlin's lightweight coroutines achieve high throughput under heavy backend traffic.",
                url = "https://ktor.io",
                imageUrl = "https://picsum.photos/seed/ktor6/800/450",
                categoryTag = "devops",
                author = "Cloud Native Foundation",
                publishedAt = "2026-08-07T18:00:00Z",
                readTimeMinutes = 5
            )
        )
    }
}
