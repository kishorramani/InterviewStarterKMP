package com.kishorramani.kmpsample.data.local

import platform.Foundation.NSUserDefaults

actual class KeyValueStorage actual constructor() {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getString(key: String): String? {
        return defaults.stringForKey(key)
    }

    actual fun putString(key: String, value: String) {
        defaults.setObject(value, forKey = key)
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (defaults.objectForKey(key) != null) {
            defaults.boolForKey(key)
        } else {
            defaultValue
        }
    }

    actual fun putBoolean(key: String, value: Boolean) {
        defaults.setBool(value, forKey = key)
    }

    actual fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }

    actual fun clear() {
        val dictionary = defaults.dictionaryRepresentation()
        for (key in dictionary.keys) {
            if (key is String) {
                defaults.removeObjectForKey(key)
            }
        }
    }
}
