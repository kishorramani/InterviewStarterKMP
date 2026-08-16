package com.kishorramani.kmpsample.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kishorramani.kmpsample.presentation.mvi.FeedUiEffect
import com.kishorramani.kmpsample.presentation.mvi.FeedUiIntent
import com.kishorramani.kmpsample.presentation.mvi.FeedViewModel
import com.kishorramani.kmpsample.presentation.ui.components.ArticleCard
import com.kishorramani.kmpsample.presentation.ui.components.FilterChipRow
import com.kishorramani.kmpsample.presentation.ui.components.SearchBar
import com.kishorramani.kmpsample.presentation.ui.components.ShimmerLoader
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel,
    onArticleClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.uiEffect.collectLatest { effect ->
            when (effect) {
                is FeedUiEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is FeedUiEffect.NavigateToDetail -> {
                    onArticleClick(effect.articleId)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TechPulse",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChanged = { viewModel.processIntent(FeedUiIntent.SearchQueryChanged(it)) },
                onClearClicked = { viewModel.processIntent(FeedUiIntent.ClearSearch) }
            )

            FilterChipRow(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = { viewModel.processIntent(FeedUiIntent.SelectCategory(it)) }
            )

            if (uiState.isLoading) {
                ShimmerLoader()
            } else if (uiState.articles.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No articles found.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.articles, key = { it.id }) { article ->
                        ArticleCard(
                            article = article,
                            onClick = { onArticleClick(article.id) },
                            onBookmarkToggle = {
                                viewModel.processIntent(FeedUiIntent.ToggleBookmark(article.id))
                            }
                        )
                    }
                }
            }
        }
    }
}
