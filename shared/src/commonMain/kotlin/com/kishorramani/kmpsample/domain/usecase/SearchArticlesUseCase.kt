package com.kishorramani.kmpsample.domain.usecase

import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class SearchArticlesUseCase(
    private val repository: ArticleRepository
) {
    operator fun invoke(query: String): Flow<List<Article>> {
        return repository.searchArticles(query)
    }
}
