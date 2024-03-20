package com.zaurh.polskismak.data.remote.dto

data class Recipe(
    val name: String,
    val imageUrl: String? = null,
    val optional: String? = null,
    val quantity: String? = null,
    val unit: String?= null
)