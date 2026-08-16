package com.kishorramani.kmpsample.data.remote

import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("url") val url: String,
    @SerialName("cover_image") val coverImage: String? = null,
    @SerialName("social_image") val socialImage: String? = null,
    @SerialName("user") val user: UserDto,
    @SerialName("tag_list") val tagList: List<String> = emptyList(),
    @SerialName("published_at") val publishedAt: String,
    @SerialName("reading_time_minutes") val readingTimeMinutes: Int
) {
    @Serializable
    data class UserDto(
        @SerialName("name") val name: String,
        @SerialName("profile_image") val profileImage: String? = null
    )

    fun toDomain(isBookmarked: Boolean = false): Article {
        val mappedCategory = getCategoryFromTags(tagList)
        val imageUrl = coverImage ?: socialImage ?: ""
        
        return Article(
            id = id.toString(),
            title = title,
            summary = description,
            content = description,
            url = url,
            imageUrl = imageUrl,
            category = mappedCategory,
            author = user.name,
            publishedAt = publishedAt,
            readTimeMinutes = readingTimeMinutes,
            tags = tagList,
            isBookmarked = isBookmarked
        )
    }

    private fun getCategoryFromTags(tags: List<String>): Category {
        val allTags = tags.map { it.lowercase() }
        return when {
            allTags.any { it in listOf("kmp", "kotlin", "android", "ios", "flutter", "swift", "mobile", "multiplatform") } -> Category.MOBILE
            allTags.any { it in listOf("ai", "ml", "openai", "gemini", "python", "machinelearning", "deeplearning", "llm", "chatgpt") } -> Category.AI_ML
            allTags.any { it in listOf("web", "frontend", "javascript", "typescript", "react", "css", "html", "vue", "angular", "nextjs", "node") } -> Category.WEB
            allTags.any { it in listOf("devops", "docker", "kubernetes", "cloud", "aws", "gcp", "ci", "cd", "githubactions", "security", "linux", "git") } -> Category.DEVOPS
            else -> Category.ALL
        }
    }
}
