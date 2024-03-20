package com.zaurh.polskismak.presentation.components

import androidx.annotation.DrawableRes
import com.zaurh.polskismak.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.logo,
        title = "Welcome!",
        description = "Swipe left to continue."
    )

    object Second : OnBoardingPage(
        image = R.drawable.onboarding1,
        title = "Find recipes!",
        description = "Meet with delicious Polish meals and learn how to cook them."
    )

    object Third : OnBoardingPage(
        image = R.drawable.onboarding2,
        title = "Enjoy your meal!",
        description = "Click finish to continue."
    )
}