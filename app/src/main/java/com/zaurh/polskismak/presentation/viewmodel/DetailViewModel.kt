package com.zaurh.polskismak.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zaurh.polskismak.common.Resource
import com.zaurh.polskismak.domain.use_case.detail_usecase.GetMealUseCase
import com.zaurh.polskismak.presentation.state.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMealUseCase: GetMealUseCase,
) : ViewModel() {

    private val _detailState = mutableStateOf(DetailState())
    val detailState: State<DetailState> = _detailState


    fun getMeal(id: Int) {
        viewModelScope.launch {
            getMealUseCase(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _detailState.value = detailState.value.copy(
                            result = result.data,
                            isLoading = false
                        )
                    }

                    is Resource.Loading -> {
                        _detailState.value = detailState.value.copy(
                            result = null,
                            isLoading = true
                        )
                    }

                    is Resource.Error -> {
                        _detailState.value = detailState.value.copy(
                            error = "There is a network problem. Please try again",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}