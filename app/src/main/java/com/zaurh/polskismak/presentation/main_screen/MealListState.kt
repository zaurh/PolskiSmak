package com.zaurh.polskismak.presentation.main_screen

import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.dto.MealsItem

data class MealListState(
    val resultList: List<MealsEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
