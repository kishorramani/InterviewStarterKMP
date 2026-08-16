package com.kishorramani.kmpsample.di

import com.kishorramani.kmpsample.data.local.LocalDatabase
import com.kishorramani.kmpsample.data.remote.ArticleApiService
import com.kishorramani.kmpsample.data.remote.KtorClientFactory
import com.kishorramani.kmpsample.data.repository.ArticleRepositoryImpl
import com.kishorramani.kmpsample.data.repository.PlatformRepositoryImpl
import com.kishorramani.kmpsample.domain.repository.ArticleRepository
import com.kishorramani.kmpsample.domain.repository.PlatformRepository
import com.kishorramani.kmpsample.domain.usecase.GetArticlesUseCase
import com.kishorramani.kmpsample.domain.usecase.GetBookmarkedArticlesUseCase
import com.kishorramani.kmpsample.domain.usecase.GetPlatformMetricsUseCase
import com.kishorramani.kmpsample.domain.usecase.SearchArticlesUseCase
import com.kishorramani.kmpsample.domain.usecase.ToggleBookmarkUseCase
import com.kishorramani.kmpsample.platform.HapticFeedback
import com.kishorramani.kmpsample.platform.ShareLauncher
import com.kishorramani.kmpsample.platform.UrlLauncher
import com.kishorramani.kmpsample.presentation.mvi.BookmarksViewModel
import com.kishorramani.kmpsample.presentation.mvi.FeedViewModel
import com.kishorramani.kmpsample.presentation.mvi.SettingsViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val networkModule = module {
    single { KtorClientFactory.create() }
    singleOf(::ArticleApiService)
}

val databaseModule = module {
    single { com.kishorramani.kmpsample.data.local.KeyValueStorage() }
    single { com.kishorramani.kmpsample.data.local.room.getRoomDatabase() }
    single { LocalDatabase(get()) }
}

val platformModule = module {
    single { HapticFeedback() }
    single { ShareLauncher() }
    single { UrlLauncher() }
    singleOf(::PlatformRepositoryImpl) bind PlatformRepository::class
}

val repositoryModule = module {
    singleOf(::ArticleRepositoryImpl) bind ArticleRepository::class
}

val useCaseModule = module {
    singleOf(::GetArticlesUseCase)
    singleOf(::SearchArticlesUseCase)
    singleOf(::ToggleBookmarkUseCase)
    singleOf(::GetBookmarkedArticlesUseCase)
    singleOf(::GetPlatformMetricsUseCase)
}

val viewModelModule = module {
    viewModelOf(::FeedViewModel)
    viewModelOf(::BookmarksViewModel)
    viewModelOf(::SettingsViewModel)
}

val appModules = listOf(
    networkModule,
    databaseModule,
    platformModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModules)
    }
