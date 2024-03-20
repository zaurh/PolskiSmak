package com.zaurh.polskismak.presentation.state

import com.zaurh.polskismak.data.local.entities.MealsEntity

data class MealListState(
    val resultList: List<MealsEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
