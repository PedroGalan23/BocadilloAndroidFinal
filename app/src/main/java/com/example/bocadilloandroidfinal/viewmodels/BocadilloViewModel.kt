package com.example.bocadilloandroidfinal.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bocadilloandroidfinal.api.RetrofitConnect
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import kotlinx.coroutines.launch

class BocadilloViewModel : ViewModel() {

    private val _bocadillos = MutableLiveData<List<Bocadillo>>()
    val bocadillos: LiveData<List<Bocadillo>> get() = _bocadillos

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    /** Obtener todos los bocadillos desde la API */
    fun fetchBocadillos() {
        Log.d("DEBUG", "Obteniendo todos los bocadillos...")
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiBocadillo.getBocadillos()
                _bocadillos.value = response.values.toList()
                Log.d("DEBUG", "Bocadillos cargados: ${_bocadillos.value?.size}")
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener bocadillos: ${e.message}"
                Log.e("ERROR", "Error al obtener bocadillos", e)
            }
        }
    }

    /** Obtener bocadillos del día actual */
    fun fetchBocadillosDia() {
        Log.d("DEBUG", "Obteniendo bocadillos del día...")
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiBocadillo.getBocadillos()
                val diaActual = obtenerDiaActual()
                val bocadillosFiltrados = response.values.filter { it.dia.equals(diaActual, ignoreCase = true) }

                _bocadillos.value = bocadillosFiltrados
                Log.d("DEBUG", "Bocadillos para $diaActual: ${bocadillosFiltrados.size}")
            } catch (e: Exception) {
                _errorMessage.value = "Error al obtener bocadillos del día: ${e.message}"
                Log.e("ERROR", "Error al obtener bocadillos del día", e)
            }
        }
    }

    /** Obtener el día de la semana en formato de texto */
    private fun obtenerDiaActual(): String {
        return java.text.SimpleDateFormat("EEEE", java.util.Locale.getDefault()).format(java.util.Date())
    }
}
