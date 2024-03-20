package com.zaurh.polskismak.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zaurh.polskismak.data.remote.dto.Ingredient
import com.zaurh.polskismak.data.remote.dto.Instructions

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    val roomId: Int = 1,
    val cookTimeMinutes: Int,
    val description: String,
    val dietary: String,
    val difficulty: String,
    val imageUrl: String,
    val ingredients: List<Ingredient>,
    val instructions: List<Instructions>,
    val mealId: Int,
    val name: String,
    val origin: String,
    val prepTimeMinutes: Int,
    val tags: List<String>
)