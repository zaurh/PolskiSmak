package com.zaurh.polskismak.data

import com.zaurh.polskismak.data.source.BundledMeals
import com.zaurh.polskismak.data.source.BundledQuotes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BundledMealsTest {

    private val meals = BundledMeals.all

    @Test
    fun `all 12 recipes from the old backend are bundled`() {
        assertEquals(12, meals.size)
        assertEquals((1..12).toList(), meals.map { it.id })
    }

    @Test
    fun `ids are unique so favorites from v1 still resolve`() {
        assertEquals(meals.size, meals.map { it.id }.toSet().size)
    }

    @Test
    fun `every recipe has ingredients and sequential steps`() {
        meals.forEach { meal ->
            assertTrue("${meal.name} has no ingredients", meal.ingredients.isNotEmpty())
            assertEquals("${meal.name} steps", (1..meal.instructions.size).toList(), meal.instructions.map { it.number })
            assertTrue("${meal.name} has no time", meal.totalTimeMinutes > 0)
        }
    }

    @Test
    fun `quotes are bundled`() {
        assertTrue(BundledQuotes.all.isNotEmpty())
    }
}
