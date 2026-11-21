package com.zaurh.polskismak.domain.model

import androidx.annotation.DrawableRes

data class Meal(
    val id: Int,
    val name: LocalizedText,
    val description: LocalizedText,
    val dietary: Dietary,
    val origin: LocalizedText,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
    @param:DrawableRes val image: Int,
    val prepTimeMinutes: Int = 0,
    val cookTimeMinutes: Int = 0,
    val difficulty: Difficulty = Difficulty.MODERATE,
    val tags: List<String> = emptyList(),
) {
    val totalTimeMinutes: Int get() = prepTimeMinutes + cookTimeMinutes

    /** Number of individual ingredients, counting the items inside grouped ingredients. */
    val ingredientCount: Int get() = ingredients.sumOf { if (it.items.isEmpty()) 1 else it.items.size }

    fun matches(query: String): Boolean {
        val q = query.trim()
        if (q.isEmpty()) return true
        val needle = q.withoutDiacritics()
        return name.variants.any { it.withoutDiacritics().contains(needle, ignoreCase = true) } ||
            tags.any { it.contains(needle, ignoreCase = true) }
    }
}

/**
 * A single ingredient (with [quantity]/[unit]) or a group such as "Dough" that is made from [items].
 */
data class Ingredient(
    val name: LocalizedText,
    @param:DrawableRes val image: Int,
    val unit: IngredientUnit? = null,
    val quantity: String? = null,
    val items: List<IngredientItem> = emptyList(),
    val note: LocalizedText? = null,
)

data class IngredientItem(
    val name: LocalizedText,
    val unit: IngredientUnit,
    @param:DrawableRes val image: Int,
    val quantity: String,
    val note: LocalizedText? = null,
)

data class Instruction(
    val number: Int,
    val step: LocalizedText,
)

enum class Difficulty { EASY, MODERATE, HARD }

enum class Dietary { VEGAN, VEGETARIAN, MEAT_BASED }

enum class IngredientUnit { GRAMS, KILOGRAMS, MILLILITERS, LITERS, TEASPOONS, TABLESPOONS, CUPS, PIECES }

/** Lets "golabki" match "Gołąbki". */
private fun String.withoutDiacritics(): String = buildString(length) {
    for (c in this@withoutDiacritics) {
        append(
            when (c) {
                'ą' -> 'a'; 'ć' -> 'c'; 'ę' -> 'e'; 'ł' -> 'l'; 'ń' -> 'n'
                'ó' -> 'o'; 'ś' -> 's'; 'ź', 'ż' -> 'z'
                'Ą' -> 'A'; 'Ć' -> 'C'; 'Ę' -> 'E'; 'Ł' -> 'L'; 'Ń' -> 'N'
                'Ó' -> 'O'; 'Ś' -> 'S'; 'Ź', 'Ż' -> 'Z'
                else -> c
            }
        )
    }
}
