package com.example.bocadilloandroidfinal.api

import com.example.bocadilloandroidfinal.modelos.Bocadillo
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceBocadillo {
    @GET("bocadillos.json")
    suspend fun getBocadillos(): Map<String, Bocadillo>

    @POST("bocadillos.json")
    suspend fun createBocadillo(@Body bocadillo: Bocadillo): Response<Bocadillo>

    @PUT("bocadillos/{id}.json")
    suspend fun updateBocadillo(@Path("id") id: String, @Body bocadillo: Bocadillo): Response<Unit>

    @DELETE("bocadillos/{id}.json")
    suspend fun deleteBocadillo(@Path("id") id: String): Response<Unit>
}
