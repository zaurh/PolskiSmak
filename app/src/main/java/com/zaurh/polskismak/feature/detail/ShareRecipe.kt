package com.zaurh.polskismak.feature.detail

import android.content.Context
import android.content.Intent
import com.zaurh.polskismak.R
import com.zaurh.polskismak.core.ui.formatQuantity
import com.zaurh.polskismak.core.ui.localized
import com.zaurh.polskismak.domain.model.LocalizedText
import com.zaurh.polskismak.domain.model.Meal

internal fun Context.shareRecipe(meal: Meal) {
    fun LocalizedText.text() = localized(this@shareRecipe)
    fun line(name: LocalizedText, note: LocalizedText?, amount: String?) = buildString {
        append(name.text())
        amount?.let { append(" – ").append(it) }
        note?.let { append(" (").append(it.text()).append(")") }
    }

    val name = meal.name.text()
    val text = buildString {
        appendLine(name)
        appendLine(getString(R.string.minutes_short, meal.totalTimeMinutes))
        appendLine()
        appendLine(getString(R.string.share_ingredients_header))
        meal.ingredients.forEach { ingredient ->
            if (ingredient.items.isEmpty()) {
                appendLine("• " + line(ingredient.name, ingredient.note, resources.formatQuantity(ingredient.quantity, ingredient.unit)))
            } else {
                appendLine("${ingredient.name.text()}:")
                ingredient.items.forEach { item ->
                    appendLine("  • " + line(item.name, item.note, resources.formatQuantity(item.quantity, item.unit)))
                }
            }
        }
        appendLine()
        appendLine(getString(R.string.share_steps_header))
        meal.instructions.forEach { appendLine("${it.number}. ${it.step.text()}") }
        appendLine()
        append(getString(R.string.share_footer))
    }
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, name)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(send, getString(R.string.share_chooser_title)))
}
