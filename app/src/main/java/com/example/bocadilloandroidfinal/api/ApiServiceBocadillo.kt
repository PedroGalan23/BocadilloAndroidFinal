package com.example.bocadilloandroidfinal.api

import com.example.bocadilloandroidfinal.modelos.Bocadillo
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceBocadillo {

    // Recogemos todos los Bocadillos desde Firebase Realtime Database
    @GET("bocadillos.json")
    suspend fun getBocadillos(): Map<String, Bocadillo>
    // Respuesta: Devuelve un Map donde la clave (key) es el ID del bocadillo y el valor es un objeto Bocadillo.


    // Crea un nuevo bocadillo en Firebase
    @POST("bocadillos.json")
    suspend fun createBocadillo(@Body bocadillo: Bocadillo): Response<Bocadillo>
    // Respuesta: Devuelve un objeto Response que contiene el bocadillo recién creado.


    // Actualiza un bocadillo existente en Firebase
    @PUT("bocadillos/{id}.json")
    suspend fun updateBocadillo(@Path("id") id: String, @Body bocadillo: Bocadillo): Response<Unit>
    // Respuesta: Devuelve un objeto Response vacío si la actualización fue exitosa.


    // Elimina un bocadillo existente en Firebase
    @DELETE("bocadillos/{id}.json")
    suspend fun deleteBocadillo(@Path("id") id: String): Response<Unit>
    // Respuesta: Devuelve un objeto Response vacío si la eliminación fue exitosa.

}
