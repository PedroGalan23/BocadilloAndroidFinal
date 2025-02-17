package com.example.bocadilloandroidfinal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bocadilloandroidfinal.api.RetrofitConnect
import com.example.bocadilloandroidfinal.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _usuarioAutenticado = MutableLiveData<Usuario?>()
    val usuarioAutenticado: LiveData<Usuario?> get() = _usuarioAutenticado

    private val _rolUsuario = MutableLiveData<String?>()
    val rolUsuario: LiveData<String?> get() = _rolUsuario

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> get() = _mensaje

    private val _errorMensaje = MutableLiveData<String>()
    val errorMensaje: LiveData<String> get() = _errorMensaje

    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean> get() = _isLogged

    var usuarioIdFirebase: String? = null  // 🔥 Guarda el ID generado por Firebase

    init {
        _isLogged.value = auth.currentUser != null

        // Si el usuario ya está autenticado, obtener sus datos
        auth.currentUser?.email?.let { email ->
            fetchUsuarioByEmail(email)
        }
    }

    // 🔥 Iniciar sesión en FirebaseAuth
    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLogged.value = true
                    _mensaje.value = "Inicio de sesión exitoso"
                    fetchUsuarioByEmail(email)  // Cargar datos del usuario autenticado
                } else {
                    _isLogged.value = false
                    _errorMensaje.value = "Error al iniciar sesión: ${task.exception?.message}"
                }
            }
    }

    // 🔥 Cerrar sesión
    fun signOut() {
        auth.signOut()
        _isLogged.value = false
        _usuarioAutenticado.value = null
        _rolUsuario.value = null
        _mensaje.value = "Sesión cerrada"
    }

    // 🔥 Obtener usuario por email desde Firebase
    fun fetchUsuarioByEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiUsuario.getUsuarios()
                val usuarioEntry = response?.entries?.find { it.value.correo == email }

                if (usuarioEntry != null) {
                    val usuarioId = usuarioEntry.key  // 🔥 ID generado por Firebase
                    val usuarioEncontrado = usuarioEntry.value.copy(id = usuarioId)

                    _usuarioAutenticado.value = usuarioEncontrado
                    _rolUsuario.value = usuarioEncontrado.rol?.lowercase()
                    _mensaje.value = "Usuario encontrado: ${usuarioEncontrado.nombre}"

                    // 🔥 Guardar ID de Firebase para futuras actualizaciones
                    usuarioIdFirebase = usuarioId
                } else {
                    _errorMensaje.value = "No se encontró un usuario con el email: $email"
                }
            } catch (e: Exception) {
                _errorMensaje.value = "Error al obtener usuario: ${e.message}"
            }
        }
    }

    // 🔥 Actualizar datos del usuario (sin modificar correo ni contraseña)
    fun updateUsuario(nombre: String, apellidos: String, curso: String) {
        val usuario = usuarioAutenticado.value

        if (usuario == null || usuarioIdFirebase.isNullOrBlank()) {
            _errorMensaje.value = "Error: No se pudo obtener el usuario o su ID"
            return
        }

        viewModelScope.launch {
            try {
                val usuarioActualizado = usuario.copy(
                    nombre = nombre,
                    apellidos = apellidos,
                    curso = curso
                )

                val usuarioMap = mapOf(
                    "login" to usuarioActualizado.login,
                    "nombre" to usuarioActualizado.nombre,
                    "apellidos" to usuarioActualizado.apellidos,
                    "correo" to usuarioActualizado.correo,
                    "curso" to usuarioActualizado.curso,
                    "rol" to usuarioActualizado.rol
                )

                usuarioIdFirebase?.let { id ->
                    RetrofitConnect.apiUsuario.updateUsuario(id, usuarioMap)
                    _usuarioAutenticado.value = usuarioActualizado
                    _mensaje.value = "Perfil actualizado correctamente"
                }
            } catch (e: Exception) {
                _errorMensaje.value = "Error al actualizar perfil: ${e.message}"
            }
        }
    }
}
