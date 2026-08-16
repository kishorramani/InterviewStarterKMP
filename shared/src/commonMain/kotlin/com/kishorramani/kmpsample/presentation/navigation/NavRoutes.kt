package com.kishorramani.kmpsample.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object Feed : NavRoute

    @Serializable
    data class Detail(val articleId: String) : NavRoute

    @Serializable
    data object Bookmarks : NavRoute

    @Serializable
    data object Settings : NavRoute
}
