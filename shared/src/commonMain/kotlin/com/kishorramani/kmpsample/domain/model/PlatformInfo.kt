package com.kishorramani.kmpsample.domain.model

data class PlatformInfo(
    val osName: String,
    val osVersion: String,
    val deviceModel: String,
    val cpuArchitecture: String,
    val memoryInfo: String,
    val isSimulator: Boolean,
    val kotlinVersion: String = "2.1.0",
    val composeVersion: String = "1.7.3"
)
