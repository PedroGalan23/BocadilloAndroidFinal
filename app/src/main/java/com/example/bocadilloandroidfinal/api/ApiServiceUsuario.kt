package com.example.bocadilloandroidfinal.api

import com.example.bocadilloandroidfinal.modelos.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ApiServiceUsuario {
    // Obtener todos los usuarios almacenados en Firebase
    @GET("usuarios.json")
    suspend fun getUsuarios(): Map<String, Usuario>
    // Respuesta: Devuelve un Map donde la clave (key) es el ID del usuario y el valor es un objeto Usuario.
    // val usuarios = apiServiceUsuario.getUsuarios()

    // Obtener un usuario específico por su ID Lo debería utilizar en Autentificación pero prefiero comparar con Corrutina
    @GET("usuarios/{id}.json")
    suspend fun getUsuario(@Path("id") id: String): Response<Usuario>
    // Respuesta: Devuelve un objeto Response que contiene el objeto Usuario asociado al ID especificado.
    // val response = apiServiceUsuario.getUsuario("P7912HBDHEB889")

    // Guardar un nuevo usuario en Firebase
    @POST("usuarios.json")
    suspend fun createUsuario(@Body usuario: Usuario): Response<Usuario>
    // Respuesta: Devuelve un objeto Response que contiene el usuario recién creado.
    // val response = apiServiceUsuario.createUsuario(nuevoUsuario)

    // Actualizar parcialmente un usuario existente en Firebase
    @PUT("usuarios/{id}.json")
    suspend fun updateUsuario(@Path("id") id: String, @Body usuario: Map<String, String?>)
    // Respuesta: No devuelve un objeto explícito, actualiza parcialmente el usuario.
    // val response = apiServiceUsuario.updateUsuario("P7912HBDHEB889", mapOf("nombre" to "Nuevo Nombre"))

    // Eliminar un usuario existente en Firebase
    @DELETE("usuarios/{id}.json")
    suspend fun deleteUsuario(@Path("id") id: String): Response<Unit>
    // Respuesta: Devuelve un objeto Response vacío (Unit) si la eliminación fue exitosa.
    // val response = apiServiceUsuario.deleteUsuario("P7912HBDHEB889")
}

