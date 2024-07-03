package com.zaurh.polskismak.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.polskismak.common.Resource
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.dto.QuoteData
import com.zaurh.polskismak.domain.use_case.meals_usecase.GetMealsUseCase
import com.zaurh.polskismak.domain.use_case.meals_usecase.GetQuoteUseCase
import com.zaurh.polskismak.presentation.state.MealListState
import com.zaurh.polskismak.presentation.state.QuoteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMealsUseCase: GetMealsUseCase,
    private val getQuoteUseCase: GetQuoteUseCase,
) : ViewModel() {

    private val _mealListState = mutableStateOf(MealListState())
    val mealListState: State<MealListState> = _mealListState

    private val _quoteState = mutableStateOf(QuoteState())
    val quoteState: State<QuoteState> = _quoteState

    private var initialMealList = listOf<MealsEntity>()
    private var isSearchStarting = true

    var isRefreshing = mutableStateOf(false)

    init {
        getMeals(false)
        getQuote()
    }

    fun refreshRecipes() {
        getQuote()
        getMeals(fetchingFromRemote = true)
    }


    private fun getMeals(fetchingFromRemote: Boolean) {
        viewModelScope.launch {
            getMealsUseCase(fetchingFromRemote).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _mealListState.value = mealListState.value.copy(
                            resultList = result.data ?: listOf(),
                            isLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        _mealListState.value = mealListState.value.copy(
                            resultList = result.data ?: listOf(),
                            isLoading = true
                        )
                    }

                    is Resource.Error -> {
                        _mealListState.value = mealListState.value.copy(
                            resultList = result.data ?: listOf(),
                            error = "Error occurred"
                        )
                    }
                }
            }
        }
    }


    private fun getQuote() {
        viewModelScope.launch {
            getQuoteUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _quoteState.value = quoteState.value.copy(
                            result = result.data,
                            isLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        _quoteState.value = quoteState.value.copy(
                            result = null,
                            isLoading = true
                        )
                    }

                    is Resource.Error -> {
                        _quoteState.value = quoteState.value.copy(
                            result = QuoteData(
                                "There is no sincerer love than the love of food.",
                                "George Bernard Shaw"
                            ),
                            error = "There is a network problem. Please try again",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }


    fun searchMeals(query: String) {
        val listToSearch = if (isSearchStarting) {
            _mealListState.value.resultList
        } else {
            initialMealList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                _mealListState.value = _mealListState.value.copy(
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
                initialMealList = mealListState.value.resultList
                isSearchStarting = false
            }
            _mealListState.value = mealListState.value.copy(
                resultList = results
            )
        }
    }

    fun clearSearch() {
        _mealListState.value = mealListState.value.copy(
            resultList = initialMealList
        )
        isSearchStarting = true
    }
}