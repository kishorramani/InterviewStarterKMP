package com.kishorramani.kmpsample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kishorramani.kmpsample.di.appModules
import com.kishorramani.kmpsample.domain.repository.PlatformRepository
import com.kishorramani.kmpsample.presentation.mvi.BookmarksViewModel
import com.kishorramani.kmpsample.presentation.mvi.FeedUiIntent
import com.kishorramani.kmpsample.presentation.mvi.FeedViewModel
import com.kishorramani.kmpsample.presentation.mvi.SettingsViewModel
import com.kishorramani.kmpsample.presentation.navigation.NavRoute
import com.kishorramani.kmpsample.presentation.theme.TechPulseTheme
import com.kishorramani.kmpsample.presentation.ui.screens.BookmarksScreen
import com.kishorramani.kmpsample.presentation.ui.screens.DetailScreen
import com.kishorramani.kmpsample.presentation.ui.screens.FeedScreen
import com.kishorramani.kmpsample.presentation.ui.screens.SettingsScreen
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinApplication(application = { modules(appModules) }) {
        val settingsViewModel: SettingsViewModel = koinViewModel()
        val settingsState by settingsViewModel.uiState.collectAsState()

        TechPulseTheme(darkTheme = settingsState.isDarkMode) {
            val navController = rememberNavController()
            var currentTab by remember { mutableStateOf<NavRoute>(NavRoute.Feed) }

            val platformRepository: PlatformRepository = koinInject()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentTab is NavRoute.Feed,
                            onClick = {
                                currentTab = NavRoute.Feed
                                navController.navigate(NavRoute.Feed) {
                                    popUpTo(NavRoute.Feed) { inclusive = true }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab is NavRoute.Feed) Icons.Filled.Home else Icons.Outlined.Home,
                                    contentDescription = "Feed"
                                )
                            },
                            label = { Text("Feed") }
                        )

                        NavigationBarItem(
                            selected = currentTab is NavRoute.Bookmarks,
                            onClick = {
                                currentTab = NavRoute.Bookmarks
                                navController.navigate(NavRoute.Bookmarks) {
                                    popUpTo(NavRoute.Feed)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab is NavRoute.Bookmarks) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Bookmarks"
                                )
                            },
                            label = { Text("Saved") }
                        )

                        NavigationBarItem(
                            selected = currentTab is NavRoute.Settings,
                            onClick = {
                                currentTab = NavRoute.Settings
                                navController.navigate(NavRoute.Settings) {
                                    popUpTo(NavRoute.Feed)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab is NavRoute.Settings) Icons.Filled.Settings else Icons.Outlined.Settings,
                                    contentDescription = "Settings"
                                )
                            },
                            label = { Text("Inspector") }
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = NavRoute.Feed,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<NavRoute.Feed> {
                        val feedViewModel: FeedViewModel = koinViewModel()
                        FeedScreen(
                            viewModel = feedViewModel,
                            onArticleClick = { articleId ->
                                navController.navigate(NavRoute.Detail(articleId))
                            }
                        )
                    }

                    composable<NavRoute.Detail> { backStackEntry ->
                        val route: NavRoute.Detail = backStackEntry.toRoute()
                        val feedViewModel: FeedViewModel = koinViewModel()
                        val feedState by feedViewModel.uiState.collectAsState()
                        val article = feedState.articles.firstOrNull { it.id == route.articleId }

                        DetailScreen(
                            article = article,
                            platformRepository = platformRepository,
                            onBackClick = { navController.popBackStack() },
                            onBookmarkToggle = { articleId ->
                                feedViewModel.processIntent(FeedUiIntent.ToggleBookmark(articleId))
                            }
                        )
                    }

                    composable<NavRoute.Bookmarks> {
                        val bookmarksViewModel: BookmarksViewModel = koinViewModel()
                        BookmarksScreen(
                            viewModel = bookmarksViewModel,
                            onArticleClick = { articleId ->
                                navController.navigate(NavRoute.Detail(articleId))
                            }
                        )
                    }

                    composable<NavRoute.Settings> {
                        SettingsScreen(
                            viewModel = settingsViewModel
                        )
                    }
                }
            }
        }
    }
}