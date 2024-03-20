package com.zaurh.polskismak.data.remote.repo

import com.zaurh.polskismak.data.local.MealsDatabase
import com.zaurh.polskismak.data.local.entities.FavoritesEntity
import com.zaurh.polskismak.domain.repo.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavoritesRepoImpl @Inject constructor(
    db: MealsDatabase
) : FavoritesRepository {

    private val favoritesDao = db.favoritesDao()


    override fun getFavorites(): Flow<List<FavoritesEntity>> = flow {
        favoritesDao.allFavorites().collect { result ->
            emit(result.map { it })
        }
    }


    override suspend fun addFavorite(favoritesEntity: FavoritesEntity) {
        favoritesDao.addFavorite(favoritesEntity)
    }

    override suspend fun removeFavorite(favoritesEntity: FavoritesEntity) {
        favoritesDao.removeFavorite(favoritesEntity)
    }

}