package com.example.bocadilloandroidfinal.modelos

import com.google.firebase.Timestamp
import java.sql.Time
import java.util.Date
//Clase Pedido de un Alumno
data class Pedido(
    val id: String,
    //Unicamente recogemos el id del usuario siendo autogenerado por la bd
    val id_usuario: String,
    val bocadillo: Bocadillo,
    val fecha_hora: Timestamp,
    val estado:Boolean,
    val precio:Double
)

