package com.zaurh.polskismak.feature.settings

import androidx.lifecycle.viewModelScope
import com.zaurh.polskismak.BuildConfig
import com.zaurh.polskismak.core.mvi.MviViewModel
import com.zaurh.polskismak.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : MviViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    SettingsState(versionName = BuildConfig.VERSION_NAME)
) {

    init {
        settingsRepository.themeMode
            .onEach { mode -> setState { copy(themeMode = mode) } }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ThemeSelected -> viewModelScope.launch { settingsRepository.setThemeMode(intent.mode) }
            SettingsIntent.LanguageClicked -> sendEffect(SettingsEffect.OpenLanguageSettings)
            SettingsIntent.AboutClicked -> setState { copy(showAbout = true) }
            SettingsIntent.AboutDismissed -> setState { copy(showAbout = false) }
            SettingsIntent.ContactClicked -> sendEffect(SettingsEffect.SendEmail(CONTACT_EMAIL, "Polski Smak"))
            SettingsIntent.RateClicked -> sendEffect(SettingsEffect.OpenStoreListing)
        }
    }

    private companion object {
        const val CONTACT_EMAIL = "zaurway@gmail.com"
    }
}
