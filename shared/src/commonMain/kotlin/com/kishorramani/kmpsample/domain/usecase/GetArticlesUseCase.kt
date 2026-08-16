package com.kishorramani.kmpsample.domain.usecase

import com.kishorramani.kmpsample.domain.model.Article
import com.kishorramani.kmpsample.domain.model.Category
import com.kishorramani.kmpsample.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class GetArticlesUseCase(
    private val repository: ArticleRepository
) {
    operator fun invoke(category: Category = Category.ALL, forceRefresh: Boolean = false): Flow<List<Article>> {
        return repository.getArticles(category, forceRefresh)
    }
}
