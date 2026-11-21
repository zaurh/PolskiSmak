package com.zaurh.polskismak.core.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import com.zaurh.polskismak.domain.model.LocalizedText

/** Text for the current app language; recomposes when the language changes. */
@Composable
@ReadOnlyComposable
fun LocalizedText.localized(): String = resolve(LocalConfiguration.current.locales[0])

fun LocalizedText.localized(context: Context): String = resolve(context.resources.configuration.locales[0])
