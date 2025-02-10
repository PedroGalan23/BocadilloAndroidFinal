package com.example.bocadilloandroidfinal.api
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import retrofit2.http.GET

interface ApiServiceBocadillo {
    //Recupera todos los bocadillos
    @GET("bocadillos.json")
    suspend fun getBocadillos(): Map<String, Bocadillo>


}