package com.zaurh.polskismak.data

import com.zaurh.polskismak.data.source.BundledMeals
import com.zaurh.polskismak.data.source.BundledQuotes
import com.zaurh.polskismak.domain.model.LocalizedText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class TranslationsTest {

    private val meals = BundledMeals.all

    @Test
    fun `every description and step is translated to Polish`() {
        meals.forEach { meal ->
            assertNotEquals("${meal.name.en} description", meal.description.en, meal.description.pl)
            meal.instructions.forEach { step ->
                assertNotEquals("${meal.name.en} step ${step.number}", step.step.en, step.step.pl)
            }
        }
    }

    @Test
    fun `every ingredient name and note has a Polish version`() {
        val texts = meals.flatMap { meal ->
            meal.ingredients.flatMap { ingredient ->
                listOfNotNull(ingredient.name, ingredient.note) +
                    ingredient.items.flatMap { listOfNotNull(it.name, it.note) }
            }
        }
        texts.forEach { assertTrue("blank Polish for ${it.en}", it.pl.isNotBlank()) }
        // "Butter" must not stay English in Polish mode.
        assertTrue(texts.any { it.en == "Butter" && it.pl == "Masło" })
    }

    @Test
    fun `quotes are translated`() {
        BundledQuotes.all.forEach { assertNotEquals(it.text.en, it.text.pl) }
    }

    @Test
    fun `resolve picks the language of the locale`() {
        val text = LocalizedText(en = "Poland", pl = "Polska")
        assertEquals("Polska", text.resolve(Locale.forLanguageTag("pl-PL")))
        assertEquals("Poland", text.resolve(Locale.ENGLISH))
        assertEquals("Poland", text.resolve(Locale.GERMAN)) // unsupported -> English
    }
}
