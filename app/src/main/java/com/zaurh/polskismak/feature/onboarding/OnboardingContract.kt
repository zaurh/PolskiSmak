package com.zaurh.polskismak.feature.onboarding

sealed interface OnboardingIntent {
    data object Finish : OnboardingIntent
}

sealed interface OnboardingEffect {
    data object NavigateToHome : OnboardingEffect
}
