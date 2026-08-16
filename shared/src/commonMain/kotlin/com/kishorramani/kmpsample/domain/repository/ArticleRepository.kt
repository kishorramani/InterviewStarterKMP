package com.kishorramani.kmpsample.domain.repository

import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getArticles(category: Category = Category.ALL, forceRefresh: Boolean = false): Flow<List<Article>>
    fun getArticleById(id: String): Flow<Article?>
    fun searchArticles(query: String): Flow<List<Article>>
    fun getBookmarkedArticles(): Flow<List<Article>>
    suspend fun toggleBookmark(articleId: String): Boolean
    suspend fun refreshArticles()
}
