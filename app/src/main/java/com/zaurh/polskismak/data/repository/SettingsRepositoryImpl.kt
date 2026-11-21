package com.zaurh.polskismak.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.zaurh.polskismak.data.local.PreferenceKeys
import com.zaurh.polskismak.domain.model.ThemeMode
import com.zaurh.polskismak.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    override val themeMode: Flow<ThemeMode> = dataStore.data
        .map { prefs ->
            prefs[PreferenceKeys.THEME_MODE]?.let { stored ->
                ThemeMode.entries.firstOrNull { it.name == stored }
            } ?: when (prefs[PreferenceKeys.LEGACY_DARK_MODE]) {
                true -> ThemeMode.DARK
                false -> ThemeMode.LIGHT
                null -> ThemeMode.SYSTEM
            }
        }
        .distinctUntilChanged()

    override val onboardingCompleted: Flow<Boolean> = dataStore.data
        .map { it[PreferenceKeys.ONBOARDING_COMPLETED] ?: false }
        .distinctUntilChanged()

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[PreferenceKeys.THEME_MODE] = mode.name }
    }

    override suspend fun setOnboardingCompleted() {
        dataStore.edit { it[PreferenceKeys.ONBOARDING_COMPLETED] = true }
    }
}
