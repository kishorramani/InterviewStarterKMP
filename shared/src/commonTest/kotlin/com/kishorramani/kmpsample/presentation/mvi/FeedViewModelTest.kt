package com.kishorramani.kmpsample.presentation.mvi

import com.kishorramani.kmpsample.data.local.LocalDatabase
import com.kishorramani.kmpsample.data.remote.ArticleApiService
import com.kishorramani.kmpsample.data.remote.KtorClientFactory
import com.kishorramani.kmpsample.data.repository.ArticleRepositoryImpl
import com.kishorramani.kmpsample.domain.model.Category
import com.kishorramani.kmpsample.domain.usecase.GetArticlesUseCase
import com.kishorramani.kmpsample.domain.usecase.SearchArticlesUseCase
import com.kishorramani.kmpsample.domain.usecase.ToggleBookmarkUseCase
import com.kishorramani.kmpsample.platform.HapticFeedback
import com.kishorramani.kmpsample.platform.ShareLauncher
import com.kishorramani.kmpsample.platform.UrlLauncher
import com.kishorramani.kmpsample.data.repository.PlatformRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSelectCategoryIntentUpdatesState() = runTest {
        val client = KtorClientFactory.create()
        val apiService = ArticleApiService(client)
        val localDb = LocalDatabase(com.kishorramani.kmpsample.data.local.room.getRoomDatabase(inMemory = true))
        val articleRepo = ArticleRepositoryImpl(apiService, localDb)
        val platformRepo = PlatformRepositoryImpl(HapticFeedback(), ShareLauncher(), UrlLauncher())

        val getArticles = GetArticlesUseCase(articleRepo)
        val searchArticles = SearchArticlesUseCase(articleRepo)
        val toggleBookmark = ToggleBookmarkUseCase(articleRepo, platformRepo)

        val viewModel = FeedViewModel(getArticles, searchArticles, toggleBookmark)

        viewModel.processIntent(FeedUiIntent.SelectCategory(Category.AI_ML))
        assertEquals(Category.AI_ML, viewModel.uiState.value.selectedCategory)
    }
}
