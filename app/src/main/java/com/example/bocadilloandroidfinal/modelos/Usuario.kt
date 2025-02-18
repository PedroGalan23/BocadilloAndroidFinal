package com.example.bocadilloandroidfinal.modelos

import java.io.Serializable

data class Usuario(
    val id: String? = null,
    val login: String? = null,
    val nombre: String? = null,
    val apellidos: String? = null,
    val correo: String? = null,
    val password: String? = null,
    val curso: String? = null,
    val rol: String? = null
) : Serializable
//Permitimos pasar a Usuario como un argumento en el grafo de navegacion


