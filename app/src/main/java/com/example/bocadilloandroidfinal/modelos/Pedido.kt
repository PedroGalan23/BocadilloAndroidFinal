package com.example.bocadilloandroidfinal.modelos

import com.google.firebase.Timestamp
import java.sql.Time
import java.util.Date

data class Pedido(
    val id: String,
    val id_usuario: String,
    val bocadillo: Bocadillo,
    val fecha_hora: Timestamp,
    val estado:Boolean,
    val precio:Double
)

