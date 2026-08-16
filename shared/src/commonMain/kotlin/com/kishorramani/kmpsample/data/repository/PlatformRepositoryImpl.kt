package com.kishorramani.kmpsample.data.repository

import com.kishorramani.kmpsample.domain.model.PlatformInfo
import com.kishorramani.kmpsample.domain.repository.PlatformRepository
import com.kishorramani.kmpsample.platform.HapticFeedback
import com.kishorramani.kmpsample.platform.ShareLauncher
import com.kishorramani.kmpsample.platform.UrlLauncher
import com.kishorramani.kmpsample.platform.getPlatformInfo

class PlatformRepositoryImpl(
    private val hapticFeedback: HapticFeedback,
    private val shareLauncher: ShareLauncher,
    private val urlLauncher: UrlLauncher
) : PlatformRepository {

    override fun getPlatformInfo(): PlatformInfo {
        return com.kishorramani.kmpsample.platform.getPlatformInfo()
    }

    override fun triggerHapticFeedback() {
        hapticFeedback.performHaptic()
    }

    override fun shareArticle(title: String, url: String) {
        shareLauncher.share(title, url)
    }

    override fun openUrl(url: String) {
        urlLauncher.openUrl(url)
    }
}
