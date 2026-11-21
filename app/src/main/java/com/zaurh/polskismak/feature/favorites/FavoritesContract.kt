package com.zaurh.polskismak.feature.favorites

import com.zaurh.polskismak.domain.model.LocalizedText
import com.zaurh.polskismak.domain.model.Meal

data class FavoritesState(
    val query: String = "",
    val favorites: List<Meal> = emptyList(),
    val hasAnyFavorites: Boolean = false,
)

sealed interface FavoritesIntent {
    data class QueryChanged(val query: String) : FavoritesIntent
    data object ClearQuery : FavoritesIntent
    data class MealClicked(val mealId: Int) : FavoritesIntent
    data class RemoveFavorite(val mealId: Int) : FavoritesIntent
    data class UndoRemove(val mealId: Int) : FavoritesIntent
    data object BrowseRecipes : FavoritesIntent
}

sealed interface FavoritesEffect {
    data class NavigateToDetail(val mealId: Int) : FavoritesEffect
    data class ShowRemoved(val mealId: Int, val mealName: LocalizedText) : FavoritesEffect
    data object NavigateToHome : FavoritesEffect
}
