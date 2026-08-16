package com.kishorramani.kmpsample.data.repository

import com.kishorramani.kmpsample.data.local.LocalDatabase
import com.kishorramani.kmpsample.data.remote.ArticleApiService
import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.model.Category
import com.kishorramani.kmpsample.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class ArticleRepositoryImpl(
    private val apiService: ArticleApiService,
    private val localDatabase: LocalDatabase
) : ArticleRepository {

    override fun getArticles(category: Category, forceRefresh: Boolean): Flow<List<Article>> {
        return localDatabase.getArticlesByCategory(category)
            .onStart {
                // Background sync from remote API to Local Database
                try {
                    refreshArticles()
                } catch (e: Exception) {
                    // Suppress network errors; local data serves seamlessly
                }
            }
    }

    override fun getArticleById(id: String): Flow<Article?> {
        return localDatabase.getArticleById(id)
    }

    override fun searchArticles(query: String): Flow<List<Article>> {
        return localDatabase.searchArticles(query)
    }

    override fun getBookmarkedArticles(): Flow<List<Article>> {
        return localDatabase.getBookmarkedArticles()
    }

    override suspend fun toggleBookmark(articleId: String): Boolean {
        return localDatabase.toggleBookmark(articleId)
    }

    override suspend fun refreshArticles() {
        val dtos = apiService.fetchArticles()
        val domainArticles = dtos.map { it.toDomain() }
        localDatabase.saveArticles(domainArticles)
    }
}
