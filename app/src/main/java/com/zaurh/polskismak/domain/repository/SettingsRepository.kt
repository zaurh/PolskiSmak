package com.zaurh.polskismak.domain.repository

import com.zaurh.polskismak.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val themeMode: Flow<ThemeMode>
    val onboardingCompleted: Flow<Boolean>
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setOnboardingCompleted()
}
