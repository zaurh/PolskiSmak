package com.zaurh.polskismak.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.domain.use_case.favorites_usecase.AllFavoriteUseCases
import com.zaurh.polskismak.presentation.state.FavoritesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val allFavoriteUseCases: AllFavoriteUseCases
) : ViewModel() {

    private val _favoritesState = mutableStateOf(FavoritesState())
    val favoritesState: State<FavoritesState> = _favoritesState

    private var initialMealList = listOf<FavoritesEntity>()
    private var isSearchStarting = true

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            allFavoriteUseCases.getFavoritesUseCase().collect { result ->
                _favoritesState.value = favoritesState.value.copy(
                    resultList = result
                )
            }
        }
    }

    fun addToFavorite(favoritesEntity: FavoritesEntity) {
        viewModelScope.launch {
            allFavoriteUseCases.addFavoriteUseCase(favoritesEntity)
        }
    }

    fun removeFavorite(favoritesEntity: FavoritesEntity) {
        viewModelScope.launch {
            allFavoriteUseCases.removeFavoriteUseCase(favoritesEntity)
        }
    }

    fun searchFavorites(query: String) {
        val listToSearch = if (isSearchStarting) {
            _favoritesState.value.resultList
        } else {
            initialMealList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                _favoritesState.value = _favoritesState.value.copy(
                    resultList = initialMealList
                )
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.name.contains(
                    query.trim(),
                    ignoreCase = true
                ) || it.tags.any { tag ->
                    tag.contains(
                        query.trim(),
                        ignoreCase = true
                    )
                }
            }
            if (isSearchStarting) {
                initialMealList = _favoritesState.value.resultList
                isSearchStarting = false
            }
            _favoritesState.value = favoritesState.value.copy(
                resultList = results
            )
        }
    }

    fun clearSearch() {
        _favoritesState.value = favoritesState.value.copy(
            resultList = initialMealList
        )
        isSearchStarting = true
    }
}