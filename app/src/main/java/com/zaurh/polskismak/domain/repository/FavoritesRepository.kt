package com.zaurh.polskismak.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    val favoriteIds: Flow<Set<Int>>
    suspend fun setFavorite(mealId: Int, favorite: Boolean)
}
