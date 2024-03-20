package com.zaurh.polskismak.presentation.detail_screen

import com.zaurh.polskismak.data.local.entities.MealsEntity

data class DetailState(
    val result: MealsEntity? = null,
    val error: String = "",
    val isLoading: Boolean = false
)
