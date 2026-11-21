package com.zaurh.polskismak.data.repository

import com.zaurh.polskismak.data.source.BundledMeals
import com.zaurh.polskismak.data.source.BundledQuotes
import com.zaurh.polskismak.domain.model.Meal
import com.zaurh.polskismak.domain.model.Quote
import com.zaurh.polskismak.domain.repository.MealRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealRepositoryImpl @Inject constructor() : MealRepository {

    private val mealsById: Map<Int, Meal> by lazy { BundledMeals.all.associateBy { it.id } }

    override fun getMeals(): List<Meal> = BundledMeals.all

    override fun getMeal(id: Int): Meal? = mealsById[id]

    override fun getRandomQuote(exclude: Quote?): Quote =
        BundledQuotes.all.filterNot { it == exclude }.random()
}
