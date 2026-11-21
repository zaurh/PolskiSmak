package com.zaurh.polskismak.domain.model

import java.util.Locale

/**
 * Recipe text in every language the app supports. The UI picks the variant for the
 * current app language, so switching language updates content without reloading data.
 */
data class LocalizedText(
    val en: String,
    val pl: String = en,
) {
    fun resolve(locale: Locale): String = if (locale.language == "pl") pl else en

    val variants: List<String> get() = listOf(en, pl)
}
