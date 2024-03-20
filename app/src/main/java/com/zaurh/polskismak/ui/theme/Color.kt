package com.zaurh.polskismak.ui.theme

import androidx.compose.ui.graphics.Color

sealed class ThemeColors(
    val background: Color,
    val surface: Color,
    val title: Color,
    val text: Color,
    val icon: Color
) {
    object Night : ThemeColors(
        background = Color(0xFF272727),
        surface = Color(0xFF212121),
        title = Color(0xFFFFFFFF),
        text = Color.Gray,
        icon = Color(0xFFFEC30D),
    )

    object Day : ThemeColors(
        background = Color(0xFFEBF6FF),
        surface = Color(0xFFEBFFF9),
        title = Color(0xFF000000),
        text = Color.Gray,
        icon = Color(0xFFFF9900)
    )
}