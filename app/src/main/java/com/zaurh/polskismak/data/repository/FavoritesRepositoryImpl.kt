package com.zaurh.polskismak.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.zaurh.polskismak.data.local.LegacyFavoritesReader
import com.zaurh.polskismak.data.local.PreferenceKeys
import com.zaurh.polskismak.domain.repository.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val legacyReader: LegacyFavoritesReader,
) : FavoritesRepository {

    private val migrationMutex = Mutex()

    override val favoriteIds: Flow<Set<Int>> = dataStore.data
        .map { prefs -> prefs[PreferenceKeys.FAVORITE_IDS].orEmpty().mapNotNull(String::toIntOrNull).toSet() }
        .distinctUntilChanged()
        .onStart { migrateLegacyFavoritesIfNeeded() }

    override suspend fun setFavorite(mealId: Int, favorite: Boolean) {
        dataStore.edit { prefs ->
            val current = prefs[PreferenceKeys.FAVORITE_IDS].orEmpty()
            prefs[PreferenceKeys.FAVORITE_IDS] =
                if (favorite) current + mealId.toString() else current - mealId.toString()
        }
    }

    private suspend fun migrateLegacyFavoritesIfNeeded() = migrationMutex.withLock {
        if (dataStore.data.first()[PreferenceKeys.LEGACY_FAVORITES_MIGRATED] == true) return@withLock
        val legacyIds = withContext(Dispatchers.IO) { legacyReader.readAndDelete() }
        dataStore.edit { prefs ->
            prefs[PreferenceKeys.FAVORITE_IDS] =
                prefs[PreferenceKeys.FAVORITE_IDS].orEmpty() + legacyIds.map(Int::toString)
            prefs[PreferenceKeys.LEGACY_FAVORITES_MIGRATED] = true
        }
    }
}
