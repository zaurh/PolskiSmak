package com.zaurh.polskismak.core.ui

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.zaurh.polskismak.R
import com.zaurh.polskismak.domain.model.Dietary
import com.zaurh.polskismak.domain.model.Difficulty
import com.zaurh.polskismak.domain.model.IngredientUnit

@get:StringRes
val Difficulty.labelRes: Int
    get() = when (this) {
        Difficulty.EASY -> R.string.difficulty_easy
        Difficulty.MODERATE -> R.string.difficulty_moderate
        Difficulty.HARD -> R.string.difficulty_hard
    }

@get:StringRes
val Dietary.labelRes: Int
    get() = when (this) {
        Dietary.VEGAN -> R.string.dietary_vegan
        Dietary.VEGETARIAN -> R.string.dietary_vegetarian
        Dietary.MEAT_BASED -> R.string.dietary_meat
    }

/** "1/2" + CUPS -> "½ cup", "Add pepper to taste" + null -> "Add pepper to taste". */
fun Resources.formatQuantity(quantity: String?, unit: IngredientUnit?): String? {
    if (quantity.isNullOrBlank()) return null
    val amount = prettyFraction(quantity.trim())
    if (unit == null) return amount
    val singular = isSingular(quantity.trim())
    val label = getString(
        when (unit) {
            IngredientUnit.GRAMS -> R.string.unit_grams
            IngredientUnit.KILOGRAMS -> R.string.unit_kilograms
            IngredientUnit.MILLILITERS -> R.string.unit_milliliters
            IngredientUnit.LITERS -> R.string.unit_liters
            IngredientUnit.TEASPOONS -> R.string.unit_teaspoons
            IngredientUnit.TABLESPOONS -> R.string.unit_tablespoons
            IngredientUnit.CUPS -> if (singular) R.string.unit_cup else R.string.unit_cups
            IngredientUnit.PIECES -> if (singular) R.string.unit_piece else R.string.unit_pieces
        }
    )
    return "$amount $label"
}

@Composable
@ReadOnlyComposable
fun quantityText(quantity: String?, unit: IngredientUnit?): String? =
    LocalContext.current.resources.formatQuantity(quantity, unit)

private val fractions = mapOf(
    "1/2" to "½", "1/3" to "⅓", "2/3" to "⅔", "1/4" to "¼", "3/4" to "¾", "1/8" to "⅛",
)

private fun prettyFraction(value: String): String =
    value.split(" ").joinToString(" ") { fractions[it] ?: it }

private fun isSingular(value: String): Boolean {
    if (value.contains(' ')) return false // e.g. "1 1/2"
    val number = if ('/' in value) {
        val (n, d) = value.split('/').map { it.toDoubleOrNull() ?: return false }
        if (d == 0.0) return false else n / d
    } else {
        value.replace(',', '.').toDoubleOrNull() ?: return false
    }
    return number <= 1.0
}
