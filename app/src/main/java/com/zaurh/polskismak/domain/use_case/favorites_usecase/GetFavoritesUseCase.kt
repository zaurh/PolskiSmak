package com.zaurh.polskismak.domain.use_case.favorites_usecase

import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.data.remote.repo.FavoritesRepoImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoritesRepoImpl
) {

    operator fun invoke(): Flow<List<FavoritesEntity>> {
        return repository.getFavorites()
    }
}