package com.zaurh.polskismak.feature.onboarding;

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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private OnboardingViewModel_Factory(Provider<SettingsRepository> settingsRepositoryProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(settingsRepositoryProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new OnboardingViewModel_Factory(settingsRepositoryProvider);
  }

  public static OnboardingViewModel newInstance(SettingsRepository settingsRepository) {
    return new OnboardingViewModel(settingsRepository);
  }
}
