package com.example.bocadilloandroidfinal.api

import com.example.bocadilloandroidfinal.modelos.Alergeno
import retrofit2.http.GET

interface ApiServiceAlergeno {

    @GET("alergenos.json")
    suspend fun getAlergenos(): Map<String, Alergeno>

}