package com.zaurh.polskismak.domain.repository

import com.zaurh.polskismak.domain.model.Meal
import com.zaurh.polskismak.domain.model.Quote

interface MealRepository {
    fun getMeals(): List<Meal>
    fun getMeal(id: Int): Meal?
    fun getRandomQuote(exclude: Quote? = null): Quote
}
