package com.zaurh.polskismak.feature.detail

import com.zaurh.polskismak.domain.model.Meal

data class DetailState(
    val meal: Meal? = null,
    val notFound: Boolean = false,
    val isFavorite: Boolean = false,
    val selectedTab: DetailTab = DetailTab.INGREDIENTS,
    val completedSteps: Set<Int> = emptySet(),
)

enum class DetailTab { INGREDIENTS, STEPS }

sealed interface DetailIntent {
    data object BackClicked : DetailIntent
    data object ToggleFavorite : DetailIntent
    data object ShareClicked : DetailIntent
    data class TabSelected(val tab: DetailTab) : DetailIntent
    data class StepToggled(val stepNumber: Int) : DetailIntent
}

sealed interface DetailEffect {
    data object NavigateBack : DetailEffect
    data class ShareRecipe(val meal: Meal) : DetailEffect
}
