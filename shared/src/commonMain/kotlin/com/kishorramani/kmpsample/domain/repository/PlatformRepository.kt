package com.kishorramani.kmpsample.domain.repository

import com.kishorramani.kmpsample.domain.model.PlatformInfo

interface PlatformRepository {
    fun getPlatformInfo(): PlatformInfo
    fun triggerHapticFeedback()
    fun shareArticle(title: String, url: String)
    fun openUrl(url: String)
}
