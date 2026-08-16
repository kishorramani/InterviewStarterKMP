package com.kishorramani.kmpsample.platform

import com.kishorramani.kmpsample.domain.model.PlatformInfo
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UIDevice

actual class HapticFeedback actual constructor() {
    actual fun performHaptic() {
        val generator = UIImpactFeedbackGenerator(style = UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
        generator.prepare()
        generator.impactOccurred()
    }
}

actual class ShareLauncher actual constructor() {
    actual fun share(title: String, url: String) {
        val textToShare = listOf("$title - $url")
        val activityViewController = UIActivityViewController(activityItems = textToShare, applicationActivities = null)
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(activityViewController, animated = true, completion = null)
    }
}

actual class UrlLauncher actual constructor() {
    actual fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        nsUrl?.let {
            if (UIApplication.sharedApplication.canOpenURL(it)) {
                UIApplication.sharedApplication.openURL(it)
            }
        }
    }
}

actual fun getPlatformInfo(): PlatformInfo {
    val device = UIDevice.currentDevice
    return PlatformInfo(
        osName = "iOS",
        osVersion = "${device.systemName} ${device.systemVersion}",
        deviceModel = device.name,
        cpuArchitecture = "ARM64 (Apple Silicon)",
        memoryInfo = "iOS Unified Memory",
        isSimulator = device.model.contains("Simulator")
    )
}
