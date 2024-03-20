package com.zaurh.polskismak.data.remote

import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.data.local.entities.MealsEntity


fun MealsEntity.toFavoritesEntity() = FavoritesEntity(
    cookTimeMinutes = cookTimeMinutes,
    description = description,
    dietary = dietary,
    difficulty = difficulty,
    imageUrl = imageUrl,
    ingredients = ingredients,
    instructions = instructions,
    mealId = mealId,
    name = name,
    origin = origin,
    prepTimeMinutes = prepTimeMinutes,
    tags = tags
)

fun FavoritesEntity.toMealsEntity() = MealsEntity(
    cookTimeMinutes = cookTimeMinutes,
    description = description,
    dietary = dietary,
    difficulty = difficulty,
    imageUrl = imageUrl,
    ingredients = ingredients,
    instructions = instructions,
    mealId = mealId,
    name = name,
    origin = origin,
    prepTimeMinutes = prepTimeMinutes,
    tags = tags
)