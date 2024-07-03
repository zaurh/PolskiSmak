package com.zaurh.polskismak.domain.repo

import com.zaurh.polskismak.common.Resource
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.dto.QuoteData
import kotlinx.coroutines.flow.Flow

interface MealsRepository {

    fun getMeals(fetchFromRemote: Boolean): Flow<Resource<List<MealsEntity>>>

    fun getMeal(id: Int): Flow<Resource<MealsEntity>>

    fun getQuote(): Flow<Resource<QuoteData>>

}