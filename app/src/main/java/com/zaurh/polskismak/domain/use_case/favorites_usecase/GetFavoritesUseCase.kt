package com.zaurh.polskismak.domain.use_case.favorites_usecase

import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.data.remote.dto.MealsItem
import com.zaurh.polskismak.data.remote.repo.MealsRepoImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repoImpl: MealsRepoImpl
) {

    operator fun invoke(): Flow<List<FavoritesEntity>> {
        return repoImpl.getFavorites()
    }
}