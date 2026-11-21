package com.zaurh.polskismak.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.polskismak.domain.model.ThemeMode
import com.zaurh.polskismak.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface AppState {
    data object Loading : AppState
    data class Ready(val themeMode: ThemeMode, val onboardingCompleted: Boolean) : AppState
}

/** App-wide state read before the first frame: theme and whether to show onboarding. */
@HiltViewModel
class AppViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
) : ViewModel() {

    val state: StateFlow<AppState> = combine(
        settingsRepository.themeMode,
        settingsRepository.onboardingCompleted,
    ) { theme, onboarded -> AppState.Ready(theme, onboarded) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppState.Loading)
}
