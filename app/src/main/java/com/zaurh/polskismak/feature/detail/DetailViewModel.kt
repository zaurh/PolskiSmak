package com.zaurh.polskismak.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zaurh.polskismak.core.mvi.MviViewModel
import com.zaurh.polskismak.domain.repository.FavoritesRepository
import com.zaurh.polskismak.domain.repository.MealRepository
import com.zaurh.polskismak.navigation.DetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    mealRepository: MealRepository,
    private val favoritesRepository: FavoritesRepository,
) : MviViewModel<DetailState, DetailIntent, DetailEffect>(DetailState()) {

    private val mealId: Int = checkNotNull(savedStateHandle.get<Int>(DetailRoute.ARG_ID))

    init {
        val meal = mealRepository.getMeal(mealId)
        setState { copy(meal = meal, notFound = meal == null) }
        favoritesRepository.favoriteIds
            .onEach { ids -> setState { copy(isFavorite = mealId in ids) } }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: DetailIntent) {
        when (intent) {
            DetailIntent.BackClicked -> sendEffect(DetailEffect.NavigateBack)
            DetailIntent.ToggleFavorite -> {
                val favorite = !currentState.isFavorite
                viewModelScope.launch { favoritesRepository.setFavorite(mealId, favorite) }
            }
            DetailIntent.ShareClicked -> currentState.meal?.let { sendEffect(DetailEffect.ShareRecipe(it)) }
            is DetailIntent.TabSelected -> setState { copy(selectedTab = intent.tab) }
            is DetailIntent.StepToggled -> setState {
                val steps = completedSteps
                copy(completedSteps = if (intent.stepNumber in steps) steps - intent.stepNumber else steps + intent.stepNumber)
            }
        }
    }
}
