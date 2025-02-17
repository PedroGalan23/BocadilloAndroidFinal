package com.example.bocadilloandroidfinal.api

import com.example.bocadilloandroidfinal.modelos.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceUsuario {
    // Obtener todos los usuarios
    @GET("usuarios.json")
    suspend fun getUsuarios(): Map<String, Usuario>?

    // Obtener un usuario por ID (Firebase no permite búsqueda por email directamente)
    @GET("usuarios/{id}.json")
    suspend fun getUsuario(@Path("id") id: String): Response<Usuario>

    // Guardar un nuevo usuario
    @POST("usuarios.json")
    suspend fun createUsuario(@Body usuario: Usuario): Response<Usuario>

    // Actualizar solo algunos campos del usuario (PATCH)
    @PUT("usuarios/{id}.json")  // 🔥 Permite actualizar un usuario
    suspend fun updateUsuario(@Path("id") id: String, @Body usuario: Map<String, String?>)


}
