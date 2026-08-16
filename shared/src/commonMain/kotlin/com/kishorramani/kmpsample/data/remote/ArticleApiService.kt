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
            val response: List<ArticleDto> = client.get("https://dev.to/api/articles").body()
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
                id = 1000000001L,
                title = "Compose Multiplatform 1.8 Released with Native iOS Performance Boosts",
                description = "Compose Multiplatform has reached a major milestone with version 1.8. This release features enhanced memory efficiency, faster UI frame rates on iOS devices, and seamless integration with SwiftUI components via UIViewControllerRepresentable.",
                url = "https://kotlinlang.org/docs/multiplatform.html",
                coverImage = "https://picsum.photos/seed/kmp1/800/450",
                socialImage = "https://picsum.photos/seed/kmp1/800/450",
                user = ArticleDto.UserDto(name = "JetBrains Team"),
                tagList = listOf("kmp", "kotlin", "compose", "mobile"),
                publishedAt = "2026-08-12T10:00:00Z",
                readingTimeMinutes = 5
            ),
            ArticleDto(
                id = 1000000002L,
                title = "Building Offline-First Apps with Room KMP & SQLite Bundled",
                description = "Offline-first architecture is essential for modern mobile applications. With androidx.room expanding to Kotlin Multiplatform, developers can write DAOs and entities once in common code and compile directly to native SQLite engines.",
                url = "https://developer.android.com/kotlin/multiplatform",
                coverImage = "https://picsum.photos/seed/room2/800/450",
                socialImage = "https://picsum.photos/seed/room2/800/450",
                user = ArticleDto.UserDto(name = "Kishor Ramani"),
                tagList = listOf("room", "sqlite", "mobile"),
                publishedAt = "2026-08-11T14:30:00Z",
                readingTimeMinutes = 7
            ),
            ArticleDto(
                id = 1000000003L,
                title = "On-Device AI Inference with Kotlin & Gemini Nano",
                description = "On-device AI provides low-latency inference while safeguarding user privacy. Learn how to integrate small language models into Kotlin Multiplatform applications using ONNX Runtime and hardware NPUs.",
                url = "https://ai.google.dev",
                coverImage = "https://picsum.photos/seed/ai3/800/450",
                socialImage = "https://picsum.photos/seed/ai3/800/450",
                user = ArticleDto.UserDto(name = "DeepMind Research"),
                tagList = listOf("ai", "gemini", "python"),
                publishedAt = "2026-08-10T09:15:00Z",
                readingTimeMinutes = 6
            ),
            ArticleDto(
                id = 1000000004L,
                title = "Clean Architecture & Koin 4.0 in Kotlin Multiplatform",
                description = "Dependency injection is a core pillar of clean architecture. Koin 4.0 introduces improved ViewModel scope management and lightweight initialization for Compose Multiplatform applications without reflection or code generation bloat.",
                url = "https://insert-koin.io",
                coverImage = "https://picsum.photos/seed/koin4/800/450",
                socialImage = "https://picsum.photos/seed/koin4/800/450",
                user = ArticleDto.UserDto(name = "Koin Community"),
                tagList = listOf("kmp", "koin", "mobile"),
                publishedAt = "2026-08-09T16:45:00Z",
                readingTimeMinutes = 4
            ),
            ArticleDto(
                id = 1000000005L,
                title = "Kotlin/Wasm: Compiling Web Applications with WebAssembly",
                description = "WebAssembly (Wasm) is revolutionizing client-side web development. Kotlin/Wasm allows developers to target the browser directly with Compose Multiplatform, opening new possibilities for cross-platform desktop & web software.",
                url = "https://kotlinlang.org/wasm",
                coverImage = "https://picsum.photos/seed/wasm5/800/450",
                socialImage = "https://picsum.photos/seed/wasm5/800/450",
                user = ArticleDto.UserDto(name = "WebAssembly Group"),
                tagList = listOf("web", "wasm", "javascript"),
                publishedAt = "2026-08-08T11:20:00Z",
                readingTimeMinutes = 8
            ),
            ArticleDto(
                id = 1000000006L,
                title = "Modern Microservices with Kotlin, Ktor & Kubernetes",
                description = "Ktor is not just for client applications—it is an asynchronous HTTP framework built for microservices. Explore how Kotlin's lightweight coroutines achieve high throughput under heavy backend traffic.",
                url = "https://ktor.io",
                coverImage = "https://picsum.photos/seed/ktor6/800/450",
                socialImage = "https://picsum.photos/seed/ktor6/800/450",
                user = ArticleDto.UserDto(name = "Cloud Native Foundation"),
                tagList = listOf("devops", "kubernetes", "cloud"),
                publishedAt = "2026-08-07T18:00:00Z",
                readingTimeMinutes = 5
            )
        )
    }
}
