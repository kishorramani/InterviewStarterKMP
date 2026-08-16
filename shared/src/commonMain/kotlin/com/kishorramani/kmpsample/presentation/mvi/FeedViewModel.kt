package com.kishorramani.kmpsample.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kishorramani.kmpsample.domain.model.Category
import com.kishorramani.kmpsample.domain.usecase.GetArticlesUseCase
import com.kishorramani.kmpsample.domain.usecase.SearchArticlesUseCase
import com.kishorramani.kmpsample.domain.usecase.ToggleBookmarkUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getArticlesUseCase: GetArticlesUseCase,
    private val searchArticlesUseCase: SearchArticlesUseCase,
    private val toggleBookmarkUseCase: ToggleBookmarkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<FeedUiEffect>()
    val uiEffect: SharedFlow<FeedUiEffect> = _uiEffect.asSharedFlow()

    private var loadJob: Job? = null

    init {
        loadArticles(Category.ALL)
    }

    fun processIntent(intent: FeedUiIntent) {
        when (intent) {
            is FeedUiIntent.SelectCategory -> {
                _uiState.update { it.copy(selectedCategory = intent.category, searchQuery = "") }
                loadArticles(intent.category)
            }
            is FeedUiIntent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
                searchArticles(intent.query)
            }
            is FeedUiIntent.ToggleBookmark -> {
                toggleBookmark(intent.articleId)
            }
            is FeedUiIntent.RefreshFeed -> {
                refreshFeed()
            }
            is FeedUiIntent.ClearSearch -> {
                _uiState.update { it.copy(searchQuery = "") }
                loadArticles(_uiState.value.selectedCategory)
            }
        }
    }

    private fun loadArticles(category: Category) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getArticlesUseCase(category)
                .onStart { _uiState.update { it.copy(isLoading = true, errorMessage = null) } }
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to load articles") }
                }
                .collect { list ->
                    _uiState.update { it.copy(isLoading = false, articles = list) }
                }
        }
    }

    private fun searchArticles(query: String) {
        if (query.isBlank()) {
            loadArticles(_uiState.value.selectedCategory)
            return
        }
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            searchArticlesUseCase(query)
                .collect { list ->
                    _uiState.update { it.copy(articles = list) }
                }
        }
    }

    private fun toggleBookmark(articleId: String) {
        viewModelScope.launch {
            val isBookmarked = toggleBookmarkUseCase(articleId)
            _uiState.update { currentState ->
                val updatedArticles = currentState.articles.map { article ->
                    if (article.id == articleId) article.copy(isBookmarked = isBookmarked)
                    else article
                }
                currentState.copy(articles = updatedArticles)
            }
            val msg = if (isBookmarked) "Article saved to bookmarks" else "Article removed from bookmarks"
            _uiEffect.emit(FeedUiEffect.ShowToast(msg))
        }
    }

    private fun refreshFeed() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            getArticlesUseCase(_uiState.value.selectedCategory, forceRefresh = true)
                .collect { list ->
                    _uiState.update { it.copy(isRefreshing = false, articles = list) }
                }
        }
    }
}
