package com.example.bocadilloandroidfinal.api

import com.example.bocadilloandroidfinal.modelos.Pedido
import retrofit2.Response
import retrofit2.http.*

interface ApiServicePedido {

    @GET("pedidos.json")
    suspend fun getPedidos(): Map<String, Pedido>?

    @POST("pedidos.json")
    suspend fun realizarPedido(@Body pedido: Pedido): Response<Void>

    @DELETE("pedidos/{id}.json")
    suspend fun cancelarPedido(@Path("id") id: String): Response<Void>
}
