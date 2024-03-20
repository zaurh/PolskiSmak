package com.zaurh.polskismak.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.data.local.entities.MealsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Upsert
    suspend fun addFavorite(favoritesEntity: FavoritesEntity)

    @Delete
    suspend fun removeFavorite(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM favorites")
    fun allFavorites(): Flow<List<FavoritesEntity>>

}