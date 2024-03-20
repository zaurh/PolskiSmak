package com.zaurh.polskismak.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.zaurh.polskismak.data.local.Converters
import com.zaurh.polskismak.data.local.MealsDatabase
import com.zaurh.polskismak.data.local.dao.MealsDao
import com.zaurh.polskismak.data.remote.BASE_URL
import com.zaurh.polskismak.data.remote.MealsApi
import com.zaurh.polskismak.data.remote.repo.MealsRepoImpl
import com.zaurh.polskismak.data_store.StoreSettings
import com.zaurh.polskismak.domain.repo.MealsRepository
import com.zaurh.polskismak.domain.use_case.favorites_usecase.AddFavoriteUseCase
import com.zaurh.polskismak.domain.use_case.favorites_usecase.AllFavoriteUseCases
import com.zaurh.polskismak.domain.use_case.favorites_usecase.GetFavoritesUseCase
import com.zaurh.polskismak.domain.use_case.favorites_usecase.RemoveFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Retrofit
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = StoreSettings(context = context)

    @Singleton
    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): MealsApi {
        return retrofit.create(MealsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFavoriteUseCases(
        addFavoriteUseCase: AddFavoriteUseCase,
        getFavoritesUseCase: GetFavoritesUseCase,
        removeFavoriteUseCase: RemoveFavoriteUseCase
    ): AllFavoriteUseCases = AllFavoriteUseCases(
        addFavoriteUseCase = addFavoriteUseCase,
        getFavoritesUseCase = getFavoritesUseCase,
        removeFavoriteUseCase = removeFavoriteUseCase
    )

    @Singleton
    @Provides
    fun provideRepository(api: MealsApi, db: MealsDatabase): MealsRepository = MealsRepoImpl(
        api = api,
        db = db
    )

    @Provides
    fun provideMealsDao(database: MealsDatabase): MealsDao {
        return database.mealsDao()
    }

    //Room
    @Singleton
    @Provides
    fun provideRoomDatabase(app: Application): MealsDatabase {
        return Room.databaseBuilder(
            app,
            MealsDatabase::class.java,
            "meals"
        ).fallbackToDestructiveMigration()
            .addTypeConverter(Converters())

            .build()
    }

}
