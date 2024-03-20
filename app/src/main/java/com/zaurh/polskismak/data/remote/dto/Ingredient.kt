package com.zaurh.polskismak.data.remote.dto

data class Ingredient(
    val name: String,
    val imageUrl: String? = null,
    val optional: String? = null,
    val quantity: String? = null,
    val recipe: List<Recipe>? = null,
    val unit: String? = null
)