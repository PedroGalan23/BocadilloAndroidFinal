package com.example.bocadilloandroidfinal.modelos

data class Bocadillo(
    val id: String,
    val nombre: String,
    val tipo: String, // 👈 Ahora es un String en lugar de un enum
    val descripcion: String,
    val coste: Double,
    val icono: String,
    val dia: String,
    val alergenos: Map<String, Boolean>? = null, // Se mantiene el Map<String, Boolean>
    val nombresAlergenos: List<String> = emptyList() // Nuevo campo con nombres de alérgenos
)
