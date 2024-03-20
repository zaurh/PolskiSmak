package com.zaurh.polskismak.domain.repo

import com.zaurh.polskismak.common.Resource
import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.dto.MealsItem
import com.zaurh.polskismak.data.remote.dto.Quotes
import kotlinx.coroutines.flow.Flow

interface MealsRepository {

    fun getMeals(fetchFromRemote: Boolean): Flow<Resource<List<MealsEntity>>>

    fun getMeal(id: Int): Flow<Resource<MealsEntity>>

    fun getQuote(): Flow<Resource<Quotes>>


    fun getFavorites(): Flow<List<FavoritesEntity>>
    suspend fun addFavorite(favoritesEntity: FavoritesEntity)
    suspend fun removeFavorite(favoritesEntity: FavoritesEntity)
}