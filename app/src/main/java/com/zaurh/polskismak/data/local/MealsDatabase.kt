package com.zaurh.polskismak.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zaurh.polskismak.data.local.dao.FavoritesDao
import com.zaurh.polskismak.data.local.dao.MealsDao
import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.data.local.entities.MealsEntity

@Database(entities = [MealsEntity::class, FavoritesEntity::class], version = 11)
@TypeConverters(Converters::class)

abstract class MealsDatabase : RoomDatabase() {
    abstract fun mealsDao(): MealsDao
    abstract fun favoritesDao(): FavoritesDao
}