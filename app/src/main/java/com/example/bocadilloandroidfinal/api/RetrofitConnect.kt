package com.example.bocadilloandroidfinal.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConnect{
    const val URL_BASE = "https://bocadillofinal-default-rtdb.europe-west1.firebasedatabase.app/"
    val apiBocadillo: ApiServiceBocadillo by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceBocadillo::class.java)
    }

    val apiAlergenos: ApiServiceAlergeno by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceAlergeno::class.java)
    }
    val apiUsuario: ApiServiceUsuario by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceUsuario::class.java)
    }
    val apiPedido: ApiServicePedido by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServicePedido::class.java)
    }
}