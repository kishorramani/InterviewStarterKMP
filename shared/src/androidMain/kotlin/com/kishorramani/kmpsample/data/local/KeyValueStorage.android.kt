package com.kishorramani.kmpsample.data.local

import android.content.Context
import com.kishorramani.kmpsample.platform.AndroidContextProvider

actual class KeyValueStorage actual constructor() {
    private val prefs by lazy {
        AndroidContextProvider.context?.getSharedPreferences("techpulse_prefs", Context.MODE_PRIVATE)
    }

    actual fun getString(key: String): String? {
        return prefs?.getString(key, null)
    }

    actual fun putString(key: String, value: String) {
        prefs?.edit()?.putString(key, value)?.apply()
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs?.getBoolean(key, defaultValue) ?: defaultValue
    }

    actual fun putBoolean(key: String, value: Boolean) {
        prefs?.edit()?.putBoolean(key, value)?.apply()
    }

    actual fun remove(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }

    actual fun clear() {
        prefs?.edit()?.clear()?.apply()
    }
}
