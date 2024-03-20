package com.zaurh.polskismak.data.remote

import com.zaurh.polskismak.data.local.entities.MealsEntity
import com.zaurh.polskismak.data.remote.dto.Quotes
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://com-zaurh-ktor-rabbits-ox01.onrender.com/"

interface MealsApi {
    @GET("/all")
    suspend fun getMeals(): List<MealsEntity>

    @GET("/quotes")
    suspend fun getQuote(): Quotes

    @GET("/{id}")
    suspend fun getMeal(@Path("id") id: Int): MealsEntity
}