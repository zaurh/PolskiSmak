package com.zaurh.polskismak.feature.detail

import androidx.lifecycle.SavedStateHandle
import com.zaurh.polskismak.navigation.DetailRoute
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

class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val favorites = FakeFavoritesRepository()

    private fun viewModel(id: Int) = DetailViewModel(
        SavedStateHandle(mapOf(DetailRoute.ARG_ID to id)),
        realMealRepository(),
        favorites,
    )

    @Test
    fun `loads the requested recipe`() {
        val state = viewModel(1).state.value
        assertEquals("Pierogi", state.meal?.name?.en)
        assertFalse(state.notFound)
    }

    @Test
    fun `unknown id shows not found`() {
        val state = viewModel(999).state.value
        assertTrue(state.notFound)
    }

    @Test
    fun `steps can be checked off and unchecked`() {
        val vm = viewModel(1)
        vm.onIntent(DetailIntent.StepToggled(2))
        vm.onIntent(DetailIntent.StepToggled(4))
        assertEquals(setOf(2, 4), vm.state.value.completedSteps)
        vm.onIntent(DetailIntent.StepToggled(2))
        assertEquals(setOf(4), vm.state.value.completedSteps)
    }

    @Test
    fun `favorite toggle is persisted and reflected`() {
        val vm = viewModel(4)
        vm.onIntent(DetailIntent.ToggleFavorite)
        assertTrue(vm.state.value.isFavorite)
        assertEquals(setOf(4), favorites.current)
    }

    @Test
    fun `tab selection and share effect`() = runTest {
        val vm = viewModel(2)
        vm.onIntent(DetailIntent.TabSelected(DetailTab.STEPS))
        assertEquals(DetailTab.STEPS, vm.state.value.selectedTab)
        vm.onIntent(DetailIntent.ShareClicked)
        assertEquals(DetailEffect.ShareRecipe(vm.state.value.meal!!), vm.effects.first())
    }
}
