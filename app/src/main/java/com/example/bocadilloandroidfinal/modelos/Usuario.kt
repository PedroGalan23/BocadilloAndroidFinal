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

    //Permitimos pasar a Usuario como un argumento en safeArgs
) : Serializable
/*
    Serializar significa convertir un objeto
    en una secuencia de bytes o un formato de datos (como JSON o XML) que puede
    ser fácilmente almacenado, transmitido o enviado a través de una red.
 */

