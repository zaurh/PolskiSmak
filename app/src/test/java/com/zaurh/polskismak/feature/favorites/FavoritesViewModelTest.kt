package com.zaurh.polskismak.feature.favorites

import com.zaurh.polskismak.domain.model.LocalizedText
import com.zaurh.polskismak.testing.FakeFavoritesRepository
import com.zaurh.polskismak.testing.MainDispatcherRule
import com.zaurh.polskismak.testing.realMealRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class FavoritesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `empty favorites`() {
        val vm = FavoritesViewModel(realMealRepository(), FakeFavoritesRepository())
        assertFalse(vm.state.value.hasAnyFavorites)
    }

    @Test
    fun `remove then undo`() = runTest {
        val repo = FakeFavoritesRepository(setOf(1, 3))
        val vm = FavoritesViewModel(realMealRepository(), repo)
        assertEquals(listOf(1, 3), vm.state.value.favorites.map { it.id })

        vm.onIntent(FavoritesIntent.RemoveFavorite(1))
        assertEquals(listOf(3), vm.state.value.favorites.map { it.id })
        assertEquals(FavoritesEffect.ShowRemoved(1, LocalizedText("Pierogi")), vm.effects.first())

        vm.onIntent(FavoritesIntent.UndoRemove(1))
        assertEquals(setOf(1, 3), repo.current)
    }

    @Test
    fun `search filters favorites but keeps hasAnyFavorites`() {
        val vm = FavoritesViewModel(realMealRepository(), FakeFavoritesRepository(setOf(1, 3)))
        vm.onIntent(FavoritesIntent.QueryChanged("zzz"))
        assertTrue(vm.state.value.favorites.isEmpty())
        assertTrue(vm.state.value.hasAnyFavorites)
    }
}
