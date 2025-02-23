package com.example.bocadilloandroidfinal.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bocadilloandroidfinal.api.RetrofitConnect
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import com.example.bocadilloandroidfinal.modelos.Pedido
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class PedidoViewModel : ViewModel() {

    private val _pedidoDelDia = MutableLiveData<Pedido?>()
    val pedidoDelDia: LiveData<Pedido?> get() = _pedidoDelDia

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> get() = _mensaje

    private val _errorMensaje = MutableLiveData<String>()
    val errorMensaje: LiveData<String> get() = _errorMensaje

    private var idPedidoActual: String? = null
    //Parte de historial
    private val _pedidosAlumno = MutableLiveData<List<Pedido>>()
    val pedidosAlumno: LiveData<List<Pedido>> get() = _pedidosAlumno

    private val _totalGastado = MutableLiveData<Double>()
    val totalGastado: LiveData<Double> get() = _totalGastado

    private val _totalBocadillos = MutableLiveData<Int>()
    val totalBocadillos: LiveData<Int> get() = _totalBocadillos

    private val _pedidosHoy = MutableLiveData<List<Pedido>?>()
    val pedidosHoy: LiveData<List<Pedido>?> get() = _pedidosHoy

    private val _pedidoSeleccionado = MutableLiveData<Pedido?>()
    val pedidoSeleccionado: LiveData<Pedido?> get() = _pedidoSeleccionado

    fun obtenerPedidosDeHoy() {
        viewModelScope.launch {
            try {
                Log.d("PedidoViewModel", "🔄 Iniciando la obtención de pedidos de hoy...")

                val response = RetrofitConnect.apiPedido.getPedidos()
                if (response.isNullOrEmpty()) {
                    Log.w("PedidoViewModel", "⚠️ No se recibieron pedidos desde Firebase.")
                    _pedidosHoy.postValue(emptyList())
                }

                if (response != null) {
                    Log.d("PedidoViewModel", "📌 Cantidad de pedidos recibidos: ${response.size}")
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val hoy = sdf.format(Date())

                val pedidosFiltrados = response?.entries?.filter { entry ->
                    val pedido = entry.value
                    val fechaSegundos = pedido.fecha_hora.seconds

                    if (fechaSegundos == null) {
                        Log.e("PedidoViewModel", "❌ Pedido ${entry.key} tiene fecha_hora nulo. Se ignorará.")
                        return@filter false
                    }

                    val pedidoFecha = Date(fechaSegundos * 1000)
                    val pedidoFechaStr = sdf.format(pedidoFecha)

                    pedidoFechaStr == hoy
                }?.map { it.value.copy(id = it.key) }

                if (pedidosFiltrados != null) {
                    Log.d("PedidoViewModel", "📋 Pedidos de hoy encontrados: ${pedidosFiltrados.size}")
                }
                _pedidosHoy.postValue(pedidosFiltrados)
            } catch (e: Exception) {
                Log.e("PedidoViewModel", "🚨 Error en obtenerPedidosDeHoy: ${e.message}", e)
                _pedidosHoy.postValue(emptyList())
            }
        }
    }

    fun cambiarEstadoPedido(pedido: Pedido, estado: Boolean) {
        viewModelScope.launch {
            try {
                val idPedido = pedido.id
                val update = mapOf("estado" to estado)  // ✅ Cambiado a `Map<String, Boolean>`
                RetrofitConnect.apiPedido.actualizarEstadoPedido(idPedido, update)
                Log.d("PedidoViewModel", "✅ Estado del pedido $idPedido actualizado a: $estado")
                obtenerPedidosDeHoy()
            } catch (e: Exception) {
                Log.e("PedidoViewModel", "🚨 Error al actualizar estado del pedido: ${e.message}", e)
            }
        }
    }


    fun seleccionarPedido(pedido: Pedido) {
        _pedidoSeleccionado.value = pedido
    }


    fun obtenerPedidosAlumno(idUsuario: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiPedido.getPedidos()
                val pedidosFiltrados = response?.values?.filter { it.id_usuario == idUsuario } ?: emptyList()

                _pedidosAlumno.postValue(pedidosFiltrados)
                _totalGastado.postValue(pedidosFiltrados.sumOf { it.precio })
                _totalBocadillos.postValue(pedidosFiltrados.size)

            } catch (e: Exception) {
                _pedidosAlumno.postValue(emptyList())
                _totalGastado.postValue(0.0)
                _totalBocadillos.postValue(0)
            }
        }
    }




    //Parte de Pedir

    fun obtenerPedidoDelDia(idUsuario: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiPedido.getPedidos()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val hoy = sdf.format(Date())

                val pedidoEncontrado = response?.entries?.find { entry ->
                    val pedido = entry.value
                    val fechaSegundos = pedido.fecha_hora.seconds
                    if (fechaSegundos == null) return@find false

                    val pedidoFecha = Date(fechaSegundos * 1000)
                    val pedidoFechaStr = sdf.format(pedidoFecha)

                    pedido.id_usuario == idUsuario && pedidoFechaStr == hoy
                }

                if (pedidoEncontrado != null) {
                    idPedidoActual = pedidoEncontrado.key
                    _pedidoDelDia.postValue(pedidoEncontrado.value)
                    _mensaje.postValue("Pedido cargado correctamente")
                } else {
                    _pedidoDelDia.postValue(null)
                    _mensaje.postValue("No tienes un pedido activo")
                }
            } catch (e: Exception) {
                _errorMensaje.postValue("Error al obtener el pedido: ${e.message}")
            }
        }
    }


    fun realizarPedido(idUsuario: String, bocadillo: Bocadillo) {
        if (_pedidoDelDia.value != null) {
            _errorMensaje.postValue("Ya tienes un pedido para hoy.")
            return
        }

        val nuevoPedido = Pedido(
            id = UUID.randomUUID().toString(),
            id_usuario = idUsuario,
            bocadillo = bocadillo,
            fecha_hora = Timestamp.now(),
            estado = false,
            precio = bocadillo.coste
        )

        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiPedido.realizarPedido(nuevoPedido)
                if (response.isSuccessful) {
                    _pedidoDelDia.postValue(nuevoPedido)
                    _mensaje.postValue("Pedido realizado con éxito")
                }
            } catch (e: Exception) {
                _errorMensaje.postValue("Error al realizar pedido: ${e.message}")
            }
        }
    }

    fun cancelarPedido() {
        idPedidoActual?.let { id ->
            viewModelScope.launch {
                RetrofitConnect.apiPedido.cancelarPedido(id)
                _pedidoDelDia.postValue(null)
            }
        }
    }
}
