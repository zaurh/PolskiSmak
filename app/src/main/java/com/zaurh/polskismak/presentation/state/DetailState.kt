package com.zaurh.polskismak.presentation.state

import com.zaurh.polskismak.data.local.entities.MealsEntity

data class DetailState(
    val result: MealsEntity? = null,
    val error: String = "",
    val isLoading: Boolean = false
)
