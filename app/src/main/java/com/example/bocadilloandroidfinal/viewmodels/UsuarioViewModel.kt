package com.example.bocadilloandroidfinal.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bocadilloandroidfinal.api.ApiServiceUsuario
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

    private val _usuarios = MutableLiveData<List<Usuario>>()
    val usuarios: LiveData<List<Usuario>> get() = _usuarios

    private val _usuario_pedido= MutableLiveData<Usuario?>()
    val usuario_pedido: LiveData<Usuario?> get() = _usuario_pedido


    var usuarioIdFirebase: String? = null  // Guarda el ID generado por Firebase

    //Método que recupera todos los usuarios para el CRUD
    fun fetchUsuarios() {
        Log.d("DEBUG", "Entrando en fetchUsuarios")
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiUsuario.getUsuarios()

                //De esta manera desglosamos el MAP y devolvemos una lista de Usuarios adecuada
                response?.let { usuariosMap ->
                    val usuariosLista = usuariosMap.map { (id, usuario) ->
                        usuario.copy(id = id) //De esta manera recuperamos el id del usuario
                    }
                    _usuarios.postValue(usuariosLista)//pasamos la lista de usuarios asignando el id
                } ?: run {
                    _errorMensaje.postValue("No se encontraron usuarios")
                }

            } catch (e: Exception) {
                _errorMensaje.postValue("Error: ${e.message}")
            }
        }
    }




    init {
        _isLogged.value = auth.currentUser != null

        // Si el usuario ya está autenticado, obtener sus datos
        auth.currentUser?.email?.let { email ->
            fetchUsuarioByEmail(email)
        }
    }

    // Iniciar sesión en FirebaseAuth
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

    // Cerrar sesión
    fun signOut() {
        auth.signOut()
        _isLogged.value = false
        _usuarioAutenticado.value = null
        _rolUsuario.value = null
        _mensaje.value = "Sesión cerrada"
    }

    // Obtener usuario por email desde Firebase
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
    fun fetchUsuarioById(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiUsuario.getUsuarios()
                val usuarioEntry = response?.entries?.find { it.value.id == id }

                if (usuarioEntry != null) {
                    val usuarioId = usuarioEntry.key  // 🔥 ID generado por Firebase
                    val usuarioEncontrado = usuarioEntry.value.copy(id = usuarioId)

                    _usuario_pedido.value = usuarioEncontrado
                    _mensaje.value = "Usuario encontrado: ${usuarioEncontrado.nombre}"
                    _errorMensaje.value = "No se encontró un usuario con el id: $id"
                }
            } catch (e: Exception) {
                _errorMensaje.value = "Error al obtener usuario: ${e.message}"
            }
        }
    }

    fun insertarAlumno(usuario: Usuario,onResult: (Boolean)->Unit){
        Log.d("DEBUG", "Entrando en fetchUsuarios")
        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiUsuario.createUsuario(usuario.copy(id = null)) //Decimos que id es null para que no lo cree

                if (response.isSuccessful) {
                    onResult(true) //Insercción realizada con exito
                }else {
                    onResult(false) //Error al insertar
                }
            } catch (e: Exception) {
                _errorMensaje.postValue("Error: ${e.message}")
                onResult(false)
            }
        }
    }
    fun registrarUsuario(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Usuario creado con éxito
                    val user = auth.currentUser
                    callback(true, "Usuario registrado con éxito: ${user?.email}")
                } else {
                    // Error al registrar usuario
                    callback(false, task.exception?.message)
                }
            }
    }
    /**
     * Actualiza los datos de un usuario en Firebase
     */
    fun updateUsuario(usuarioActualizado: Usuario, onResult: (Boolean) -> Unit) {
        val id = usuarioActualizado.id

        if (id.isNullOrBlank()) {
            _errorMensaje.value = "Error: No se pudo obtener el ID del usuario"
            return
        }

        viewModelScope.launch {
            try {
                val usuarioMap = mapOf(
                    "login" to usuarioActualizado.login,
                    "nombre" to usuarioActualizado.nombre,
                    "apellidos" to usuarioActualizado.apellidos,
                    "correo" to usuarioActualizado.correo,
                    "password" to usuarioActualizado.password, // 🔥 Ahora actualiza la contraseña
                    "curso" to usuarioActualizado.curso,
                    "rol" to usuarioActualizado.rol
                )

                RetrofitConnect.apiUsuario.updateUsuario(id, usuarioMap)
                _mensaje.postValue("Usuario actualizado correctamente")
                fetchUsuarios() // 🔄 Refrescar lista
                onResult(true)
            } catch (e: Exception) {
                _errorMensaje.postValue("Error al actualizar usuario: ${e.message}")
                onResult(false)
            }
        }
    }

    // 🔥 Actualizar datos del usuario (sin modificar correo ni contraseña)
    fun updateUsuarioPerfil(nombre: String, apellidos: String, curso: String) {
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

    /**
     * Elimina un usuario de Firebase
     */
    fun deleteUsuario(id: String, onResult: (Boolean) -> Unit) {
        if (id.isBlank()) {
            _errorMensaje.value = "Error: No se pudo obtener el ID del usuario"
            return
        }

        viewModelScope.launch {
            try {
                val response = RetrofitConnect.apiUsuario.deleteUsuario(id)
                if (response.isSuccessful) {
                    _mensaje.postValue("Usuario eliminado correctamente")
                    fetchUsuarios() // 🔄 Actualizar lista después de eliminar
                    onResult(true)
                } else {
                    _errorMensaje.postValue("Error al eliminar usuario")
                    onResult(false)
                }
            } catch (e: Exception) {
                _errorMensaje.postValue("Error al eliminar usuario: ${e.message}")
                onResult(false)
            }
        }
    }
}
