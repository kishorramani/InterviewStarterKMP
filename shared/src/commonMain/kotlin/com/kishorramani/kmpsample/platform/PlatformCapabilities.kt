package com.kishorramani.kmpsample.platform

import com.kishorramani.kmpsample.domain.model.PlatformInfo

expect class HapticFeedback() {
    fun performHaptic()
}

expect class ShareLauncher() {
    fun share(title: String, url: String)
}

expect class UrlLauncher() {
    fun openUrl(url: String)
}

expect fun getPlatformInfo(): PlatformInfo

