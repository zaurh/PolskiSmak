package com.zaurh.polskismak.data.remote.dto

data class MealData(
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