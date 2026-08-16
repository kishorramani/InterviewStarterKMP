@file:OptIn(
    kotlin.experimental.ExperimentalNativeApi::class,
    kotlinx.cinterop.ExperimentalForeignApi::class
)

package com.kishorramani.kmpsample

import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.writeToFile
import platform.Foundation.create
import kotlin.native.setUnhandledExceptionHook

fun MainViewController() = ComposeUIViewController {
    setUnhandledExceptionHook { throwable ->
        val stackTraceText = throwable.stackTraceToString()
        println("KMP_CRASH: $stackTraceText")
        try {
            val documentDirectory = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).first() as String
            val logFilePath = "$documentDirectory/error_log.txt"
            val nsString = NSString.create(string = stackTraceText)
            nsString.writeToFile(path = logFilePath, atomically = true, encoding = NSUTF8StringEncoding, error = null)
        } catch (e: Exception) {
            println("Failed to write crash log to file: ${e.message}")
        }
    }
    App()
}