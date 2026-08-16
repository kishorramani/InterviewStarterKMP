package com.kishorramani.kmpsample.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.kishorramani.kmpsample.domain.model.PlatformInfo

object AndroidContextProvider {
    var context: Context? = null
}

actual class HapticFeedback actual constructor() {
    private val context: Context? get() = AndroidContextProvider.context

    actual fun performHaptic() {
        context?.let { ctx ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = ctx.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(50)
                }
            }
        }
    }
}

actual class ShareLauncher actual constructor() {
    private val context: Context? get() = AndroidContextProvider.context

    actual fun share(title: String, url: String) {
        context?.let { ctx ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "$title - $url")
                type = "text/plain"
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share TechPulse Article").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            ctx.startActivity(shareIntent)
        }
    }
}

actual class UrlLauncher actual constructor() {
    private val context: Context? get() = AndroidContextProvider.context

    actual fun openUrl(url: String) {
        context?.let { ctx ->
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                ctx.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

actual fun getPlatformInfo(): PlatformInfo {
    val runtime = Runtime.getRuntime()
    val totalRamMb = (runtime.maxMemory() / (1024 * 1024)).toString() + " MB"
    val arch = System.getProperty("os.arch") ?: "Unknown"

    return PlatformInfo(
        osName = "Android",
        osVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})",
        deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
        cpuArchitecture = arch,
        memoryInfo = "Max Memory: $totalRamMb",
        isSimulator = Build.FINGERPRINT.contains("generic") || Build.MODEL.contains("google_sdk")
    )
}
