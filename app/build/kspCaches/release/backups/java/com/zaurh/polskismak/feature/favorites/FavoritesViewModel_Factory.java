package com.zaurh.polskismak.feature.favorites;

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
public final class FavoritesViewModel_Factory implements Factory<FavoritesViewModel> {
  private final Provider<MealRepository> mealRepositoryProvider;

  private final Provider<FavoritesRepository> favoritesRepositoryProvider;

  private FavoritesViewModel_Factory(Provider<MealRepository> mealRepositoryProvider,
      Provider<FavoritesRepository> favoritesRepositoryProvider) {
    this.mealRepositoryProvider = mealRepositoryProvider;
    this.favoritesRepositoryProvider = favoritesRepositoryProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(mealRepositoryProvider.get(), favoritesRepositoryProvider.get());
  }

  public static FavoritesViewModel_Factory create(Provider<MealRepository> mealRepositoryProvider,
      Provider<FavoritesRepository> favoritesRepositoryProvider) {
    return new FavoritesViewModel_Factory(mealRepositoryProvider, favoritesRepositoryProvider);
  }

  public static FavoritesViewModel newInstance(MealRepository mealRepository,
      FavoritesRepository favoritesRepository) {
    return new FavoritesViewModel(mealRepository, favoritesRepository);
  }
}
