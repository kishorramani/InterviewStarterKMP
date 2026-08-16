package com.kishorramani.kmpsample.domain.usecase

import com.kishorramani.kmpsample.domain.model.PlatformInfo
import com.kishorramani.kmpsample.domain.repository.PlatformRepository

class GetPlatformMetricsUseCase(
    private val repository: PlatformRepository
) {
    operator fun invoke(): PlatformInfo {
        return repository.getPlatformInfo()
    }
}
