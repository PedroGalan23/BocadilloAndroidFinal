package com.example.bocadilloandroidfinal.modelos

import java.io.Serializable

data class Bocadillo(
    val id: String? = null,
    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val coste: Double = 0.0,
    val icono: String = "",
    val dia: String = "",
    val alergenos: Map<String, String> = emptyMap() // Cambiado a no nullable
) : Serializable {
    val nombresAlergenos: List<String>
        get() = alergenos.values.toList()
}