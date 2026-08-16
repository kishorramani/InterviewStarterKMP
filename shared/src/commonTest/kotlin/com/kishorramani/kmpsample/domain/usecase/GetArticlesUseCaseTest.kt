package com.kishorramani.kmpsample.domain.usecase

import com.kishorramani.kmpsample.data.local.LocalDatabase
import com.kishorramani.kmpsample.data.remote.ArticleApiService
import com.kishorramani.kmpsample.data.remote.KtorClientFactory
import com.kishorramani.kmpsample.data.repository.ArticleRepositoryImpl
import com.kishorramani.kmpsample.domain.model.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetArticlesUseCaseTest {

    @Test
    fun testGetArticlesReturnsListOfArticles() = runTest {
        val client = KtorClientFactory.create()
        val apiService = ArticleApiService(client)
        val localDatabase = LocalDatabase()
        val repository = ArticleRepositoryImpl(apiService, localDatabase)
        val getArticlesUseCase = GetArticlesUseCase(repository)

        val articles = getArticlesUseCase(Category.ALL).first()
        assertTrue(articles.isNotEmpty(), "Articles list should not be empty")
    }

    @Test
    fun testCategoryFiltering() = runTest {
        val client = KtorClientFactory.create()
        val apiService = ArticleApiService(client)
        val localDatabase = LocalDatabase()
        val repository = ArticleRepositoryImpl(apiService, localDatabase)
        val getArticlesUseCase = GetArticlesUseCase(repository)

        val mobileArticles = getArticlesUseCase(Category.MOBILE).first()
        assertTrue(mobileArticles.all { it.category == Category.MOBILE }, "All articles should belong to MOBILE category")
    }
}
