package com.kishorramani.kmpsample.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kishorramani.kmpsample.data.local.KeyValueStorage
import com.kishorramani.kmpsample.domain.model.PlatformInfo
import com.kishorramani.kmpsample.domain.repository.PlatformRepository
import com.kishorramani.kmpsample.domain.usecase.GetPlatformMetricsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val platformInfo: PlatformInfo? = null,
    val isDarkMode: Boolean = true,
    val cacheSizeMb: Double = 1.4
)

sealed interface SettingsUiIntent {
    data class ToggleTheme(val isDark: Boolean) : SettingsUiIntent
    data object ClearCache : SettingsUiIntent
    data object RefreshSystemMetrics : SettingsUiIntent
}

sealed interface SettingsUiEffect {
    data class ShowToast(val message: String) : SettingsUiEffect
}

class SettingsViewModel(
    private val getPlatformMetricsUseCase: GetPlatformMetricsUseCase,
    private val platformRepository: PlatformRepository,
    private val keyValueStorage: KeyValueStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            isDarkMode = keyValueStorage.getBoolean(KEY_IS_DARK_MODE, defaultValue = true)
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SettingsUiEffect>()
    val uiEffect: SharedFlow<SettingsUiEffect> = _uiEffect.asSharedFlow()

    init {
        loadPlatformMetrics()
    }

    fun processIntent(intent: SettingsUiIntent) {
        when (intent) {
            is SettingsUiIntent.ToggleTheme -> {
                keyValueStorage.putBoolean(KEY_IS_DARK_MODE, intent.isDark)
                _uiState.update { it.copy(isDarkMode = intent.isDark) }
                platformRepository.triggerHapticFeedback()
            }
            is SettingsUiIntent.ClearCache -> {
                clearCache()
            }
            is SettingsUiIntent.RefreshSystemMetrics -> {
                loadPlatformMetrics()
            }
        }
    }

    private fun loadPlatformMetrics() {
        val info = getPlatformMetricsUseCase()
        _uiState.update { it.copy(platformInfo = info) }
    }

    private fun clearCache() {
        viewModelScope.launch {
            _uiState.update { it.copy(cacheSizeMb = 0.0) }
            platformRepository.triggerHapticFeedback()
            _uiEffect.emit(SettingsUiEffect.ShowToast("Local cache cleared!"))
        }
    }

    companion object {
        private const val KEY_IS_DARK_MODE = "techpulse_key_is_dark_mode"
    }
}
