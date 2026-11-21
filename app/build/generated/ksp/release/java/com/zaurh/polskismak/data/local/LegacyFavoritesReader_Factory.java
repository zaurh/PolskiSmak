package com.zaurh.polskismak.data.local;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class LegacyFavoritesReader_Factory implements Factory<LegacyFavoritesReader> {
  private final Provider<Context> contextProvider;

  private LegacyFavoritesReader_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LegacyFavoritesReader get() {
    return newInstance(contextProvider.get());
  }

  public static LegacyFavoritesReader_Factory create(Provider<Context> contextProvider) {
    return new LegacyFavoritesReader_Factory(contextProvider);
  }

  public static LegacyFavoritesReader newInstance(Context context) {
    return new LegacyFavoritesReader(context);
  }
}
