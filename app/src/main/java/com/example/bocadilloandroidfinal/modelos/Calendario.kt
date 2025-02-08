package com.example.bocadilloandroidfinal.modelos

import com.google.firebase.Timestamp

data class Calendario(
    val id:String,
    val id_bocadillo:String,
    val fecha: Timestamp
)
