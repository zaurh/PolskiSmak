package com.zaurh.polskismak.data.remote

import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.dto.QuoteData
import retrofit2.http.GET
import retrofit2.http.Path

//const val BASE_URL = "https://com-zaurh-ktor-rabbits-ox01.onrender.com/"
const val BASE_URL = "https://polskismakapi-0e0707fa1923.herokuapp.com/"

interface MealsApi {
    @GET("/meals")
    suspend fun getMeals(): ApiResponse

    @GET("/quote_random")
    suspend fun getQuote(): ApiResponse

    @GET("meals/{id}")
    suspend fun getMeal(@Path("id") id: Int): ApiResponse
}