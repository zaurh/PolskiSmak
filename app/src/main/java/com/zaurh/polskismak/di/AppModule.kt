package com.zaurh.polskismak.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.zaurh.polskismak.data.repository.FavoritesRepositoryImpl
import com.zaurh.polskismak.data.repository.MealRepositoryImpl
import com.zaurh.polskismak.data.repository.SettingsRepositoryImpl
import com.zaurh.polskismak.domain.repository.FavoritesRepository
import com.zaurh.polskismak.domain.repository.MealRepository
import com.zaurh.polskismak.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMealRepository(impl: MealRepositoryImpl): MealRepository

    @Binds
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            // Same file name as v1, so existing settings carry over.
            produceFile = { context.preferencesDataStoreFile("polski_smak") },
        )
}
