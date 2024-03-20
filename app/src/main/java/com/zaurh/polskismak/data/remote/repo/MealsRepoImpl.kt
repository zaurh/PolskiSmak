package com.zaurh.polskismak.data.remote.repo

import com.zaurh.polskismak.common.Resource
import com.zaurh.polskismak.data.local.MealsDatabase
import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.MealsApi
import com.zaurh.polskismak.data.remote.dto.Quotes
import com.zaurh.polskismak.domain.repo.MealsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MealsRepoImpl @Inject constructor(
    private val api: MealsApi,
    db: MealsDatabase
) : MealsRepository {

    private val mealsDao = db.mealsDao()

    override fun getMeals(fetchFromRemote: Boolean): Flow<Resource<List<MealsEntity>>> = flow {
        emit(Resource.Loading())
        val localMealList = mealsDao.getAllMeals().map { it }
        emit(Resource.Loading())

        val fetchingFromLocal = localMealList.isNotEmpty() && !fetchFromRemote

        try {
            if (fetchingFromLocal) {
                emit(Resource.Loading())
            } else {
                val remoteMealList = api.getMeals()
                mealsDao.clearMeals()
                mealsDao.insertMeals(remoteMealList.map { it })
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected Error"))
        } catch (e: IOException) {
            emit(Resource.Error(e.message.toString()))
        }
        val newMealList = mealsDao.getAllMeals().map { it }
        emit(Resource.Success(newMealList))
    }


    override fun getMeal(id: Int): Flow<Resource<MealsEntity>> = flow {
        try {
            emit(Resource.Loading())
            val specificMeal = api.getMeal(id)
            emit(Resource.Success(specificMeal))

        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "${e.message}"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "${e.message}"
                )
            )
        }
    }

    override fun getQuote(): Flow<Resource<Quotes>> = flow {
        try {
            emit(Resource.Loading())
            val randomQuote = api.getQuote()
            emit(Resource.Success(randomQuote))
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "${e.message}"
                )
            )

        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "${e.message}"
                )
            )

        }
    }


}