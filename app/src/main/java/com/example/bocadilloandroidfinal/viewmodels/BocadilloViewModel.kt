package com.example.bocadilloandroidfinal.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bocadilloandroidfinal.api.RetrofitConnect
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class BocadilloViewModel : ViewModel() {

    private val _listaBocadillos = MutableLiveData<List<Bocadillo>>()
    val listaBocadillos: LiveData<List<Bocadillo>> get() = _listaBocadillos

    fun fetchBocadillosDia() {
        viewModelScope.launch {
            try {
                Log.d("DEBUG", "Iniciando la carga de bocadillos...")

                val bocadillosResponse = withContext(Dispatchers.IO) {
                    RetrofitConnect.apiBocadillo.getBocadillos()
                }

                val alergenosResponse = withContext(Dispatchers.IO) {
                    RetrofitConnect.apiAlergenos.getAlergenos()
                }

                Log.d("DEBUG", "Bocadillos obtenidos: ${bocadillosResponse.size}")
                Log.d("DEBUG", "Alergenos obtenidos: ${alergenosResponse.size}")

                // Crear un mapa de alérgenos con nombres
                val alergenosMap = alergenosResponse.mapValues { it.value.nombre }

                // Obtener día actual
                val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
                val diaActual = sdf.format(Date())

                // Filtrar bocadillos por día
                val bocadillosDiaActual = bocadillosResponse.values.filter { it.dia.equals(diaActual, ignoreCase = true) }

                Log.d("DEBUG", "Bocadillos disponibles para el día $diaActual: ${bocadillosDiaActual.size}")

                // Seleccionar un bocadillo frío y uno caliente
                val bocadillosFiltrados = mutableListOf<Bocadillo>()
                val frio = bocadillosDiaActual.find { it.tipo.equals("Frio", ignoreCase = true) }
                val caliente = bocadillosDiaActual.find { it.tipo.equals("Caliente", ignoreCase = true) }

                frio?.let { bocadillosFiltrados.add(it) }
                caliente?.let { bocadillosFiltrados.add(it) }

                Log.d("DEBUG", "Bocadillos seleccionados para mostrar: ${bocadillosFiltrados.size}")

                _listaBocadillos.postValue(bocadillosFiltrados)

            } catch (e: Exception) {
                Log.e("ERROR", "Error al obtener bocadillos", e)
            }
        }
    }

    fun fetchBocadillos(){

    }
}
