package com.zaurh.polskismak.data.repository;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import com.zaurh.polskismak.data.local.LegacyFavoritesReader;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class FavoritesRepositoryImpl_Factory implements Factory<FavoritesRepositoryImpl> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  private final Provider<LegacyFavoritesReader> legacyReaderProvider;

  private FavoritesRepositoryImpl_Factory(Provider<DataStore<Preferences>> dataStoreProvider,
      Provider<LegacyFavoritesReader> legacyReaderProvider) {
    this.dataStoreProvider = dataStoreProvider;
    this.legacyReaderProvider = legacyReaderProvider;
  }

  @Override
  public FavoritesRepositoryImpl get() {
    return newInstance(dataStoreProvider.get(), legacyReaderProvider.get());
  }

  public static FavoritesRepositoryImpl_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider,
      Provider<LegacyFavoritesReader> legacyReaderProvider) {
    return new FavoritesRepositoryImpl_Factory(dataStoreProvider, legacyReaderProvider);
  }

  public static FavoritesRepositoryImpl newInstance(DataStore<Preferences> dataStore,
      LegacyFavoritesReader legacyReader) {
    return new FavoritesRepositoryImpl(dataStore, legacyReader);
  }
}
