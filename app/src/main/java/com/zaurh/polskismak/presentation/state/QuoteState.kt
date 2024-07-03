package com.zaurh.polskismak.presentation.state

import com.zaurh.polskismak.data.remote.dto.QuoteData

data class QuoteState(
    val result: QuoteData? = null,
    val error: String = "",
    val isLoading: Boolean = false
)
