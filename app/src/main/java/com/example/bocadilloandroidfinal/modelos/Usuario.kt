package com.example.bocadilloandroidfinal.modelos

data class Usuario(
    val id: String,
    val login:String,
    val nombre:String,
    val apellidos:String,
    val correo:String,
    val password:String,
    val curso:String,
    val alergias:List<Alergeno> = emptyList(),
    val rol:String
)
