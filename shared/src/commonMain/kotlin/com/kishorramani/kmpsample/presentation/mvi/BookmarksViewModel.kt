package com.kishorramani.kmpsample.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.usecase.GetBookmarkedArticlesUseCase
import com.kishorramani.kmpsample.domain.usecase.ToggleBookmarkUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookmarksUiState(
    val isLoading: Boolean = true,
    val bookmarkedArticles: List<Article> = emptyList(),
    val searchQuery: String = ""
)

sealed interface BookmarksUiIntent {
    data class RemoveBookmark(val articleId: String) : BookmarksUiIntent
    data class SearchQueryChanged(val query: String) : BookmarksUiIntent
}

sealed interface BookmarksUiEffect {
    data class ShowToast(val message: String) : BookmarksUiEffect
}

class BookmarksViewModel(
    private val getBookmarkedArticlesUseCase: GetBookmarkedArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookmarksUiState())
    val uiState: StateFlow<BookmarksUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<BookmarksUiEffect>()
    val uiEffect: SharedFlow<BookmarksUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadBookmarks()
    }

    fun processIntent(intent: BookmarksUiIntent) {
        when (intent) {
            is BookmarksUiIntent.RemoveBookmark -> {
                removeBookmark(intent.articleId)
            }
            is BookmarksUiIntent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
            }
        }
    }

    private fun loadBookmarks() {
        viewModelScope.launch {
            getBookmarkedArticlesUseCase().collect { list ->
                _uiState.update { it.copy(isLoading = false, bookmarkedArticles = list) }
            }
        }
    }

    private fun removeBookmark(articleId: String) {
        viewModelScope.launch {
            toggleBookmarkUseCase(articleId)
            _uiEffect.emit(BookmarksUiEffect.ShowToast("Removed from bookmarks"))
        }
    }
}
