package com.zaurh.polskismak.feature.home

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
class HomeViewModel @Inject constructor(
    private val mealRepository: MealRepository,
    private val favoritesRepository: FavoritesRepository,
) : MviViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    private val allMeals: List<Meal> = mealRepository.getMeals()

    init {
        setState { copy(quote = mealRepository.getRandomQuote(), meals = allMeals) }
        favoritesRepository.favoriteIds
            .onEach { ids -> setState { copy(favoriteIds = ids) } }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.QueryChanged -> updateResults(query = intent.query)
            HomeIntent.ClearQuery -> updateResults(query = "")
            is HomeIntent.FilterSelected -> updateResults(filter = intent.filter)
            is HomeIntent.MealClicked -> sendEffect(HomeEffect.NavigateToDetail(intent.mealId))
            is HomeIntent.ToggleFavorite -> toggleFavorite(intent.mealId)
            HomeIntent.SurpriseMe -> surpriseMe()
            HomeIntent.ShuffleQuote -> setState { copy(quote = mealRepository.getRandomQuote(exclude = quote)) }
        }
    }

    private fun updateResults(query: String = currentState.query, filter: MealFilter = currentState.filter) {
        setState {
            copy(
                query = query,
                filter = filter,
                meals = allMeals.filter { filter.predicate(it) && it.matches(query) },
            )
        }
    }

    private fun toggleFavorite(mealId: Int) {
        val isFavorite = mealId in currentState.favoriteIds
        viewModelScope.launch { favoritesRepository.setFavorite(mealId, !isFavorite) }
    }

    private fun surpriseMe() {
        val pool = currentState.meals.ifEmpty { allMeals }
        if (pool.isNotEmpty()) sendEffect(HomeEffect.NavigateToDetail(pool.random().id))
    }
}
