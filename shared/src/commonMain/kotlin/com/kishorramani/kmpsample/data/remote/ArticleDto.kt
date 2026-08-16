package com.kishorramani.kmpsample.data.remote

import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("summary") val summary: String,
    @SerialName("content") val content: String,
    @SerialName("url") val url: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("category") val categoryTag: String,
    @SerialName("author") val author: String,
    @SerialName("publishedAt") val publishedAt: String,
    @SerialName("readTimeMinutes") val readTimeMinutes: Int
) {
    fun toDomain(isBookmarked: Boolean = false): Article {
        return Article(
            id = id,
            title = title,
            summary = summary,
            content = content,
            url = url,
            imageUrl = imageUrl,
            category = Category.fromTag(categoryTag),
            author = author,
            publishedAt = publishedAt,
            readTimeMinutes = readTimeMinutes,
            isBookmarked = isBookmarked
        )
    }
}
