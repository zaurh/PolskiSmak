package com.zaurh.polskismak.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

private val base = Typography()

/** Serif headlines give the app a cookbook feel; body text stays on the system sans-serif. */
internal val AppTypography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = FontFamily.Serif),
    displayMedium = base.displayMedium.copy(fontFamily = FontFamily.Serif),
    displaySmall = base.displaySmall.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold),
    headlineLarge = base.headlineLarge.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold),
    headlineMedium = base.headlineMedium.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold),
    headlineSmall = base.headlineSmall.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold),
    titleLarge = base.titleLarge.copy(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold),
    titleMedium = base.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    titleSmall = base.titleSmall.copy(fontWeight = FontWeight.SemiBold),
    bodyLarge = base.bodyLarge,
    bodyMedium = base.bodyMedium,
    bodySmall = base.bodySmall,
    labelLarge = base.labelLarge.copy(fontWeight = FontWeight.SemiBold),
    labelMedium = base.labelMedium,
    labelSmall = base.labelSmall,
)
