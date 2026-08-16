package com.kishorramani.kmpsample.presentation.mvi

import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.model.Category

data class FeedUiState(
    val isLoading: Boolean = true,
    val articles: List<Article> = emptyList(),
    val selectedCategory: Category = Category.ALL,
    val searchQuery: String = "",
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)

sealed interface FeedUiIntent {
    data class SelectCategory(val category: Category) : FeedUiIntent
    data class SearchQueryChanged(val query: String) : FeedUiIntent
    data class ToggleBookmark(val articleId: String) : FeedUiIntent
    data object RefreshFeed : FeedUiIntent
    data object ClearSearch : FeedUiIntent
}

sealed interface FeedUiEffect {
    data class ShowToast(val message: String) : FeedUiEffect
    data class NavigateToDetail(val articleId: String) : FeedUiEffect
}
