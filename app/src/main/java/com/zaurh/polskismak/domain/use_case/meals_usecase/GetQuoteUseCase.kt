package com.zaurh.polskismak.domain.use_case.meals_usecase

import com.zaurh.polskismak.common.Resource
import com.zaurh.polskismak.data.remote.dto.QuoteData
import com.zaurh.polskismak.domain.repo.MealsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuoteUseCase @Inject constructor(
    private val repository: MealsRepository
) {

    operator fun invoke(): Flow<Resource<QuoteData>> {
        return repository.getQuote()
    }
}