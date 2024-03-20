package com.zaurh.polskismak.presentation

import com.zaurh.polskismak.data.remote.dto.Quotes

data class QuoteState(
    val result: Quotes? = null,
    val error: String = "",
    val isLoading: Boolean = false
)
