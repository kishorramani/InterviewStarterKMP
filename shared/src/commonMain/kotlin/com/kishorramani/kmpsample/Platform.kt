package com.kishorramani.kmpsample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform