package com.zaurh.polskismak.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreSettings(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Settings")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LANGUAGE = booleanPreferencesKey("language")
    }

    val getDarkMode: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE] ?: false
        }

    suspend fun saveDarkMode(switched: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = switched
        }
    }

    val getLanguage: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE] ?: false
        }

    suspend fun saveLanguage(switched: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = switched
        }
    }
}