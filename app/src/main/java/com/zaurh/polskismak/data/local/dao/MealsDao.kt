package com.zaurh.polskismak.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zaurh.polskismak.data.local.entities.MealsEntity

@Dao
interface MealsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(recipes: List<MealsEntity>)

    @Query("Delete from mealList")
    suspend fun clearMeals()

    @Query("Select * from mealList")
    suspend fun getAllMeals(): List<MealsEntity>
}