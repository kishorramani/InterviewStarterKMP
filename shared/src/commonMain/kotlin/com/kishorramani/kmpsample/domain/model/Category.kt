package com.kishorramani.kmpsample.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Category(val displayName: String, val tag: String) {
    ALL("All Topics", "all"),
    MOBILE("Mobile & KMP", "mobile"),
    AI_ML("AI & ML", "ai"),
    WEB("Web & Frontend", "web"),
    DEVOPS("Cloud & DevOps", "devops");

    companion object {
        fun fromTag(tag: String): Category {
            return entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) } ?: ALL
        }
    }
}
