package com.zaurh.polskismak.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

internal object PreferenceKeys {
    /** Same name as v1 so returning users skip onboarding. */
    val ONBOARDING_COMPLETED = booleanPreferencesKey("on_boarding_completed")

    /** v1 stored only a light/dark boolean; read once to seed [THEME_MODE]. */
    val LEGACY_DARK_MODE = booleanPreferencesKey("dark_mode")
    val THEME_MODE = stringPreferencesKey("theme_mode")

    val FAVORITE_IDS = stringSetPreferencesKey("favorite_meal_ids")
    val LEGACY_FAVORITES_MIGRATED = booleanPreferencesKey("legacy_favorites_migrated")
}
