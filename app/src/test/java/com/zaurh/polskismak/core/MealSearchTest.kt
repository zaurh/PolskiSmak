package com.zaurh.polskismak.core

import com.zaurh.polskismak.testing.realMealRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MealSearchTest {

    private val meals = realMealRepository().getMeals()

    @Test
    fun `search ignores Polish diacritics`() {
        val result = meals.filter { it.matches("golabki") }.map { it.name.en }
        assertEquals(listOf("Gołąbki"), result)
    }

    @Test
    fun `search matches tags and is case insensitive`() {
        assertTrue(meals.filter { it.matches("KOTLET") }.size >= 2)
    }

    @Test
    fun `search works with both the English and Polish names`() {
        assertEquals(listOf(4), meals.filter { it.matches("kotlet schabowy") }.map { it.id })
        assertEquals(listOf(11), meals.filter { it.matches("placki ziemniaczane") }.map { it.id })
    }

    @Test
    fun `blank query matches everything`() {
        assertEquals(meals.size, meals.count { it.matches("  ") })
    }
}
