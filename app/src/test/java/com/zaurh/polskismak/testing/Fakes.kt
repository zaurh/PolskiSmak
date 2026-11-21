package com.zaurh.polskismak.testing

import com.zaurh.polskismak.data.repository.MealRepositoryImpl
import com.zaurh.polskismak.domain.repository.FavoritesRepository
import com.zaurh.polskismak.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeFavoritesRepository(initial: Set<Int> = emptySet()) : FavoritesRepository {
    private val ids = MutableStateFlow(initial)
    override val favoriteIds: Flow<Set<Int>> = ids
    val current: Set<Int> get() = ids.value
    override suspend fun setFavorite(mealId: Int, favorite: Boolean) {
        ids.value = if (favorite) ids.value + mealId else ids.value - mealId
    }
}

/** The real bundled data is pure Kotlin, so tests use it directly. */
fun realMealRepository(): MealRepository = MealRepositoryImpl()
