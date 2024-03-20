package com.zaurh.polskismak.domain.repo

import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavorites(): Flow<List<FavoritesEntity>>
    suspend fun addFavorite(favoritesEntity: FavoritesEntity)
    suspend fun removeFavorite(favoritesEntity: FavoritesEntity)
}