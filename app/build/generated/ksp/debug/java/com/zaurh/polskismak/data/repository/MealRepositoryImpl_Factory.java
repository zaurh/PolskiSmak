package com.zaurh.polskismak.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MealRepositoryImpl_Factory implements Factory<MealRepositoryImpl> {
  @Override
  public MealRepositoryImpl get() {
    return newInstance();
  }

  public static MealRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MealRepositoryImpl newInstance() {
    return new MealRepositoryImpl();
  }

  private static final class InstanceHolder {
    static final MealRepositoryImpl_Factory INSTANCE = new MealRepositoryImpl_Factory();
  }
}
