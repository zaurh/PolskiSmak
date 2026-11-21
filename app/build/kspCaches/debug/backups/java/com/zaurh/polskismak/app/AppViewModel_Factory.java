package com.zaurh.polskismak.app;

import com.zaurh.polskismak.domain.repository.SettingsRepository;
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
public final class AppViewModel_Factory implements Factory<AppViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private AppViewModel_Factory(Provider<SettingsRepository> settingsRepositoryProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public AppViewModel get() {
    return newInstance(settingsRepositoryProvider.get());
  }

  public static AppViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new AppViewModel_Factory(settingsRepositoryProvider);
  }

  public static AppViewModel newInstance(SettingsRepository settingsRepository) {
    return new AppViewModel(settingsRepository);
  }
}
