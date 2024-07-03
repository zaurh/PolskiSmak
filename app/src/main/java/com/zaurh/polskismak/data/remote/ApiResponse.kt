package com.zaurh.polskismak.data.remote

import com.zaurh.polskismak.data.remote.dto.MealData
import com.zaurh.polskismak.data.remote.dto.QuoteData

data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val mealList: List<MealData>? = null,
    val meal: MealData? = null,
    val quote: QuoteData? = null,
)