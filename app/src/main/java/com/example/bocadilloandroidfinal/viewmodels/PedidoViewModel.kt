package com.example.bocadilloandroidfinal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bocadilloandroidfinal.api.RetrofitConnect
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import com.example.bocadilloandroidfinal.modelos.Pedido
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.UUID

class PedidoViewModel : ViewModel() {

    private val _pedidoDelDia = MutableLiveData<Pedido?>()
    val pedidoDelDia: LiveData<Pedido?> get() = _pedidoDelDia

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> get() = _mensaje

    private val _errorMensaje = MutableLiveData<String>()
    val errorMensaje: LiveData<String> get() = _errorMensaje

    private var idPedidoActual: String? = null

    fun obtenerPedidoDelDia(idUsuario: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiPedido.getPedidos()
                val pedidoEncontrado = response?.entries?.find { it.value.id_usuario == idUsuario }

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
            estado = true,
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
