package com.zaurh.polskismak.domain.use_case.favorites_usecase

import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.domain.repo.MealsRepository
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val repository: MealsRepository
) {

    suspend operator fun invoke(favoritesEntity: FavoritesEntity) {
        return repository.addFavorite(favoritesEntity)
    }
}