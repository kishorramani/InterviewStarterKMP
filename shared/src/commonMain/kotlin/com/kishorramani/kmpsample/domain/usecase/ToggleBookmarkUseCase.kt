package com.kishorramani.kmpsample.domain.usecase

import com.kishorramani.kmpsample.domain.repository.ArticleRepository
import com.kishorramani.kmpsample.domain.repository.PlatformRepository

class ToggleBookmarkUseCase(
    private val articleRepository: ArticleRepository,
    private val platformRepository: PlatformRepository
) {
    suspend operator fun invoke(articleId: String): Boolean {
        val result = articleRepository.toggleBookmark(articleId)
        if (result) {
            platformRepository.triggerHapticFeedback()
        }
        return result
    }
}
