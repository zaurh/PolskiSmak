package com.zaurh.polskismak.presentation.favorite_screen

import com.zaurh.polskismak.data.local.entities.FavoritesEntity

data class FavoritesState(
    val resultList: List<FavoritesEntity> = emptyList()
)