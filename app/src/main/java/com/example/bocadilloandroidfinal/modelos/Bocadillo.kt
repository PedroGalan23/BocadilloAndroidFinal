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
    val alergenos: Map<String, String> = emptyMap() // Lo inicializamos para que no haya problema con alergenos
    //Utilizamos Serializable para pasar la clase Bocadillo con safeArgs
) : Serializable {
    /*Nombre de alergenos es una propiedad derivada,siendo una propiedad calculada a partir de la recolección de alergenos de la bd
     Debido a que alergenos se guarda como un Map<String,String> siendo complicado mostrar una lista directamente
     */
    val nombresAlergenos: List<String>
        get() = alergenos.values.toList()
}

/*
    Serializar significa convertir un objeto
    en una secuencia de bytes o un formato de datos (como JSON o XML) que puede
    ser fácilmente almacenado, transmitido o enviado a través de una red.
 */
