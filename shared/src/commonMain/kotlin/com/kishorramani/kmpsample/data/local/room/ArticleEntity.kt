package com.kishorramani.kmpsample.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.model.Category

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val summary: String,
    val content: String,
    val url: String,
    val imageUrl: String,
    val category: String,
    val author: String,
    val publishedAt: String,
    val readTimeMinutes: Int,
    val isBookmarked: Boolean = false
) {
    fun toDomain(): Article {
        return Article(
            id = id,
            title = title,
            summary = summary,
            content = content,
            url = url,
            imageUrl = imageUrl,
            category = Category.entries.firstOrNull { it.name == category } ?: Category.ALL,
            author = author,
            publishedAt = publishedAt,
            readTimeMinutes = readTimeMinutes,
            isBookmarked = isBookmarked
        )
    }

    companion object {
        fun fromDomain(article: Article): ArticleEntity {
            return ArticleEntity(
                id = article.id,
                title = article.title,
                summary = article.summary,
                content = article.content,
                url = article.url,
                imageUrl = article.imageUrl,
                category = article.category.name,
                author = article.author,
                publishedAt = article.publishedAt,
                readTimeMinutes = article.readTimeMinutes,
                isBookmarked = article.isBookmarked
            )
        }
    }
}
