package com.zaurh.polskismak.feature.favorites

import androidx.lifecycle.viewModelScope
import com.zaurh.polskismak.core.mvi.MviViewModel
import com.zaurh.polskismak.domain.model.Meal
import com.zaurh.polskismak.domain.repository.FavoritesRepository
import com.zaurh.polskismak.domain.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val favoritesRepository: FavoritesRepository,
) : MviViewModel<FavoritesState, FavoritesIntent, FavoritesEffect>(FavoritesState()) {

    private var allFavorites: List<Meal> = emptyList()

    init {
        favoritesRepository.favoriteIds
            .onEach { ids ->
                allFavorites = mealRepository.getMeals().filter { it.id in ids }
                applyQuery(currentState.query)
            }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: FavoritesIntent) {
        when (intent) {
            is FavoritesIntent.QueryChanged -> applyQuery(intent.query)
            FavoritesIntent.ClearQuery -> applyQuery("")
            is FavoritesIntent.MealClicked -> sendEffect(FavoritesEffect.NavigateToDetail(intent.mealId))
            is FavoritesIntent.RemoveFavorite -> {
                val meal = mealRepository.getMeal(intent.mealId) ?: return
                viewModelScope.launch { favoritesRepository.setFavorite(meal.id, false) }
                sendEffect(FavoritesEffect.ShowRemoved(meal.id, meal.name))
            }
            is FavoritesIntent.UndoRemove ->
                viewModelScope.launch { favoritesRepository.setFavorite(intent.mealId, true) }
            FavoritesIntent.BrowseRecipes -> sendEffect(FavoritesEffect.NavigateToHome)
        }
    }

    private fun applyQuery(query: String) {
        setState {
            copy(
                query = query,
                favorites = allFavorites.filter { it.matches(query) },
                hasAnyFavorites = allFavorites.isNotEmpty(),
            )
        }
    }
}
