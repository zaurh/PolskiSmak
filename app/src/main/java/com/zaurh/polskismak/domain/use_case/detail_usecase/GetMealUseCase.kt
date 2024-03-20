package com.zaurh.polskismak.domain.use_case.detail_usecase

import com.zaurh.polskismak.common.Resource
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.dto.MealsItem
import com.zaurh.polskismak.domain.repo.MealsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMealUseCase @Inject constructor(
    private val repository: MealsRepository
) {

    operator fun invoke(id: Int): Flow<Resource<MealsEntity>> {
        return repository.getMeal(id)
    }
}