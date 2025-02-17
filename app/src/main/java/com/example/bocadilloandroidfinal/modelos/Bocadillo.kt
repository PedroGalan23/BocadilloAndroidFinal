package com.example.bocadilloandroidfinal.modelos

data class Bocadillo(
    val nombre: String = "",
    val tipo: String = "",
    val descripcion: String = "",
    val coste: Double = 0.0,
    val icono: String = "",
    val dia: String = "",
    val alergenos: Map<String, String>? = null
) {
    val nombresAlergenos: List<String>
        get() = alergenos?.values?.toList() ?: emptyList()
}
