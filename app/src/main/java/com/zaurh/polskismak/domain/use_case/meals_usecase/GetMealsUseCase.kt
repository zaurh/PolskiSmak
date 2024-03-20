package com.zaurh.polskismak.domain.use_case.meals_usecase

import com.zaurh.polskismak.common.Resource
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.domain.repo.MealsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMealsUseCase @Inject constructor(
    private val repository: MealsRepository
) {

    operator fun invoke(fetchingFromRemote: Boolean): Flow<Resource<List<MealsEntity>>> {
        return repository.getMeals(fetchingFromRemote)
    }
}