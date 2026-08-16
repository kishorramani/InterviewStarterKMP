package com.kishorramani.kmpsample.domain.usecase

import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class GetBookmarkedArticlesUseCase(
    private val repository: ArticleRepository
) {
    operator fun invoke(): Flow<List<Article>> {
        return repository.getBookmarkedArticles()
    }
}
