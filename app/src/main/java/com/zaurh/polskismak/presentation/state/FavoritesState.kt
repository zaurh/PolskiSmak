package com.zaurh.polskismak.presentation.state

import com.zaurh.polskismak.data.local.entities.FavoritesEntity

data class FavoritesState(
    val resultList: List<FavoritesEntity> = emptyList()
)