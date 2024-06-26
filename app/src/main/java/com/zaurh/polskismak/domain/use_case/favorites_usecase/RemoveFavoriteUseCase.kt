package com.zaurh.polskismak.domain.use_case.favorites_usecase

import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.data.remote.repo.FavoritesRepoImpl
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val repository: FavoritesRepoImpl
) {

    suspend operator fun invoke(favoritesEntity: FavoritesEntity) {
        return repository.removeFavorite(favoritesEntity)
    }
}