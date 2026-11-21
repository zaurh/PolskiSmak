package com.zaurh.polskismak.feature.home

import com.zaurh.polskismak.domain.model.Dietary
import com.zaurh.polskismak.domain.model.Difficulty
import com.zaurh.polskismak.domain.model.Meal
import com.zaurh.polskismak.domain.model.Quote

data class HomeState(
    val quote: Quote? = null,
    val query: String = "",
    val filter: MealFilter = MealFilter.ALL,
    val meals: List<Meal> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
)

sealed interface HomeIntent {
    data class QueryChanged(val query: String) : HomeIntent
    data object ClearQuery : HomeIntent
    data class FilterSelected(val filter: MealFilter) : HomeIntent
    data class MealClicked(val mealId: Int) : HomeIntent
    data class ToggleFavorite(val mealId: Int) : HomeIntent
    data object SurpriseMe : HomeIntent
    data object ShuffleQuote : HomeIntent
}

sealed interface HomeEffect {
    data class NavigateToDetail(val mealId: Int) : HomeEffect
}

enum class MealFilter(val predicate: (Meal) -> Boolean) {
    ALL({ true }),
    QUICK({ it.totalTimeMinutes <= 30 }),
    EASY({ it.difficulty == Difficulty.EASY }),
    VEGETARIAN({ it.dietary == Dietary.VEGETARIAN || it.dietary == Dietary.VEGAN }),
}
