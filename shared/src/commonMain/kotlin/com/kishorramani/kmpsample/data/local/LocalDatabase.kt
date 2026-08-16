package com.kishorramani.kmpsample.data.local

import com.kishorramani.kmpsample.data.local.room.AppDatabase
import com.kishorramani.kmpsample.data.local.room.ArticleEntity
import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDatabase(private val database: AppDatabase) {
    private val dao = database.articleDao()

    fun getAllArticles(): Flow<List<Article>> {
        return dao.getAllArticles().map { list -> list.map { it.toDomain() } }
    }

    fun getArticlesByCategory(category: Category): Flow<List<Article>> {
        return if (category == Category.ALL) {
            getAllArticles()
        } else {
            dao.getArticlesByCategory(category.name).map { list -> list.map { it.toDomain() } }
        }
    }

    fun getArticleById(id: String): Flow<Article?> {
        return dao.getArticleById(id).map { it?.toDomain() }
    }

    fun searchArticles(query: String): Flow<List<Article>> {
        return dao.searchArticles(query).map { list -> list.map { it.toDomain() } }
    }

    fun getBookmarkedArticles(): Flow<List<Article>> {
        return dao.getBookmarkedArticles().map { list -> list.map { it.toDomain() } }
    }

    suspend fun saveArticles(articles: List<Article>) {
        val entities = articles.map { ArticleEntity.fromDomain(it) }
        dao.insertArticles(entities)
    }

    suspend fun toggleBookmark(articleId: String): Boolean {
        dao.toggleBookmark(articleId)
        return dao.isBookmarked(articleId) ?: false
    }

    suspend fun clearCache() {
        dao.clearArticles()
    }
}
