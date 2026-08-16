package com.kishorramani.kmpsample.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val url: String,
    val imageUrl: String,
    val category: Category,
    val author: String,
    val publishedAt: String,
    val readTimeMinutes: Int,
    val tags: List<String> = emptyList(),
    val isBookmarked: Boolean = false
)
