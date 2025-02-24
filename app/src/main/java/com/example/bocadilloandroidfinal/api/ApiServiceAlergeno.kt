package com.example.bocadilloandroidfinal.api

import com.example.bocadilloandroidfinal.modelos.Alergeno
import retrofit2.http.GET

interface ApiServiceAlergeno {
    //Recogemos el total de Alergenos de FireBase
    @GET("alergenos.json")
    suspend fun getAlergenos(): Map<String, Alergeno>
    //Key es el ID , Valor el Obtejo Alergeno
}