package com.zaurh.polskismak.feature.detail;

import androidx.lifecycle.SavedStateHandle;
import com.zaurh.polskismak.domain.repository.FavoritesRepository;
import com.zaurh.polskismak.domain.repository.MealRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<MealRepository> mealRepositoryProvider;

  private final Provider<FavoritesRepository> favoritesRepositoryProvider;

  private DetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MealRepository> mealRepositoryProvider,
      Provider<FavoritesRepository> favoritesRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.mealRepositoryProvider = mealRepositoryProvider;
    this.favoritesRepositoryProvider = favoritesRepositoryProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), mealRepositoryProvider.get(), favoritesRepositoryProvider.get());
  }

  public static DetailViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<MealRepository> mealRepositoryProvider,
      Provider<FavoritesRepository> favoritesRepositoryProvider) {
    return new DetailViewModel_Factory(savedStateHandleProvider, mealRepositoryProvider, favoritesRepositoryProvider);
  }

  public static DetailViewModel newInstance(SavedStateHandle savedStateHandle,
      MealRepository mealRepository, FavoritesRepository favoritesRepository) {
    return new DetailViewModel(savedStateHandle, mealRepository, favoritesRepository);
  }
}
