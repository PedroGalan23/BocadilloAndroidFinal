package com.example.bocadilloandroidfinal.api
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import retrofit2.http.GET

interface ApiServiceBocadillo {
    @GET("bocadillos.json")
    suspend fun getBocadillos(): Map<String, Bocadillo>


}