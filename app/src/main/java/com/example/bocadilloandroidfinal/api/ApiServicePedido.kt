package com.example.bocadilloandroidfinal.api

import com.example.bocadilloandroidfinal.modelos.Pedido
import retrofit2.Response
import retrofit2.http.*

interface ApiServicePedido {

    // Obtiene todos los pedidos almacenados en Firebase
    @GET("pedidos.json")
    suspend fun getPedidos(): Map<String, Pedido>?
    // Respuesta: Devuelve un Map donde la clave (key) es el ID del pedido y el valor es un objeto Pedido.


    // Realiza un nuevo pedido en Firebase
    @POST("pedidos.json")
    suspend fun realizarPedido(@Body pedido: Pedido): Response<Void>
    // Respuesta: Devuelve un objeto Response vacío si el pedido fue realizado con éxito.


    // Cancela un pedido existente en Firebase
    @DELETE("pedidos/{id}.json")
    suspend fun cancelarPedido(@Path("id") id: String): Response<Void>
    // Respuesta: Devuelve un objeto Response vacío  si la cancelación fue exitosa.


    // Actualiza el estado de un pedido existente en Firebase
    @PATCH("pedidos/{id}.json")
    suspend fun actualizarEstadoPedido(@Path("id") id: String, @Body update: Map<String, Boolean>): Response<Void>
    // Respuesta: Devuelve un objeto Response vacío (Void) si la actualización del estado fue exitosa.
    // Ej val response = apiServicePedido.actualizarEstadoPedido("12345", mapOf("estado" to true))
}
