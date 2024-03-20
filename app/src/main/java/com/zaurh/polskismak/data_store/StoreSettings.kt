package com.zaurh.polskismak.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("polski_smak")


class StoreSettings(private val context: Context) {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
    }

    private val dataStore = context.dataStore

    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val onBoardingState = preferences[PreferencesKey.onBoardingKey] ?: false
                onBoardingState
            }
    }


    companion object {
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