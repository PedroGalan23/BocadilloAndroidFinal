package com.example.bocadilloandroidfinal.modelos

data class Usuario(
    val id: String? = null,  // 🔥 ID generado por Firebase (ahora opcional)
    val login: String? = null,
    val nombre: String? = null,
    val apellidos: String? = null,
    val correo: String? = null,
    val password: String? = null,
    val curso: String? = null,
    val rol: String? = null
)
