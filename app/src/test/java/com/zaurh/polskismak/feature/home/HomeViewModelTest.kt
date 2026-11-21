package com.zaurh.polskismak.feature.home

import com.zaurh.polskismak.domain.model.Difficulty
import com.zaurh.polskismak.testing.FakeFavoritesRepository
import com.zaurh.polskismak.testing.MainDispatcherRule
import com.zaurh.polskismak.testing.realMealRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val favorites = FakeFavoritesRepository(initial = setOf(3))
    private val viewModel by lazy { HomeViewModel(realMealRepository(), favorites) }

    @Test
    fun `initial state shows all recipes, a quote and saved favorites`() {
        val state = viewModel.state.value
        assertEquals(12, state.meals.size)
        assertNotNull(state.quote)
        assertEquals(setOf(3), state.favoriteIds)
    }

    @Test
    fun `query and filter combine`() {
        viewModel.onIntent(HomeIntent.FilterSelected(MealFilter.EASY))
        assertTrue(viewModel.state.value.meals.all { it.difficulty == Difficulty.EASY })

        viewModel.onIntent(HomeIntent.QueryChanged("bigos"))
        assertEquals(listOf("Bigos"), viewModel.state.value.meals.map { it.name.en })

        viewModel.onIntent(HomeIntent.ClearQuery)
        viewModel.onIntent(HomeIntent.FilterSelected(MealFilter.ALL))
        assertEquals(12, viewModel.state.value.meals.size)
    }

    @Test
    fun `toggle favorite adds and removes`() {
        viewModel.onIntent(HomeIntent.ToggleFavorite(5))
        assertEquals(setOf(3, 5), viewModel.state.value.favoriteIds)
        viewModel.onIntent(HomeIntent.ToggleFavorite(3))
        assertEquals(setOf(5), favorites.current)
    }

    @Test
    fun `clicking a meal emits navigation effect`() = runTest {
        viewModel.onIntent(HomeIntent.MealClicked(7))
        assertEquals(HomeEffect.NavigateToDetail(7), viewModel.effects.first())
    }

    @Test
    fun `surprise me picks from the visible recipes`() = runTest {
        viewModel.onIntent(HomeIntent.QueryChanged("pierogi"))
        val visibleIds = viewModel.state.value.meals.map { it.id }
        viewModel.onIntent(HomeIntent.SurpriseMe)
        val effect = viewModel.effects.first() as HomeEffect.NavigateToDetail
        assertTrue(effect.mealId in visibleIds)
    }

    @Test
    fun `shuffle quote changes the quote`() {
        val before = viewModel.state.value.quote
        viewModel.onIntent(HomeIntent.ShuffleQuote)
        assertNotEquals(before, viewModel.state.value.quote)
    }
}
