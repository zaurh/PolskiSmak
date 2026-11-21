package com.zaurh.polskismak.feature.settings

import com.zaurh.polskismak.domain.model.ThemeMode

data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val versionName: String = "",
    val showAbout: Boolean = false,
)

sealed interface SettingsIntent {
    data class ThemeSelected(val mode: ThemeMode) : SettingsIntent
    data object LanguageClicked : SettingsIntent
    data object AboutClicked : SettingsIntent
    data object AboutDismissed : SettingsIntent
    data object ContactClicked : SettingsIntent
    data object RateClicked : SettingsIntent
}

sealed interface SettingsEffect {
    data object OpenLanguageSettings : SettingsEffect
    data class SendEmail(val address: String, val subject: String) : SettingsEffect
    data object OpenStoreListing : SettingsEffect
}
