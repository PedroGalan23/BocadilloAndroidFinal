package com.example.bocadilloandroidfinal.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bocadilloandroidfinal.api.RetrofitConnect
import com.example.bocadilloandroidfinal.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {
    //Recogemos una instancia de FireBaseAuth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //Las definiciones las hacemos de forma segura con MutableLiveData(Recoger y Modificar) y LiveData(Mostrar)

    //Definimos la variable que será observada para determinar y recoger el Objeto Usuario Loggeado
    private val _usuarioAutenticado = MutableLiveData<Usuario?>()
    val usuarioAutenticado: LiveData<Usuario?> get() = _usuarioAutenticado

    //Definimos la variable que será observada para determinar y recoger el Rol del Usuario
    private val _rolUsuario = MutableLiveData<String?>()
    val rolUsuario: LiveData<String?> get() = _rolUsuario

    //Definimos la variable la cual será observada para determinar mensajes de comprobación en el auth
    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> get() = _mensaje

    //Definimos la variable que será obserbada para determinar mensajes de error
    private val _errorMensaje = MutableLiveData<String>()
    val errorMensaje: LiveData<String> get() = _errorMensaje

    //Definimos la variable que será observada para determinar si un Usuario está logueado
    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean> get() = _isLogged

    //Definimos la variable para Observar los Usuarios Recogidos
    private val _usuarios = MutableLiveData<List<Usuario>>()
    val usuarios: LiveData<List<Usuario>> get() = _usuarios

    //Definimos la variable para observar el usuario del pedido (No implementado)
    private val _usuario_pedido= MutableLiveData<Usuario?>()
    val usuario_pedido: LiveData<Usuario?> get() = _usuario_pedido

    //Guarda el id autogenerado en Firebase
    var usuarioIdFirebase: String? = null  // Guarda el ID generado por Firebase

    /*  Metodo muy importante por que se ejecuta cada vez que se instancia UsuarioViewModel
        Sino se ha cerrado la sesión pero si destruida la actividad, cuando entremos de nuevo se logueará automáticamente
     */
    init {
        _isLogged.value = auth.currentUser != null

        // Si el usuario ya está autenticado, obtener sus datos
        auth.currentUser?.email?.let { email ->
            fetchUsuarioByEmail(email)
        }
    }

    //Método que recupera todos los usuarios para el CRUD
    fun fetchUsuarios() {
        Log.d("DEBUG", "Entrando en fetchUsuarios")
        //Se inicia la Corrutina
        viewModelScope.launch {
            try {
                //Obtenemos un Mapa de todos los usuarios
                val response = RetrofitConnect.apiUsuario.getUsuarios()

                //De esta manera desglosamos el MAP y devolvemos una lista de Usuarios adecuada
                //Evitamos el Nulo con ? ejecutando solo si la respuesta no es nula
                response?.let { usuariosMap ->
                    //Desglosamos id, Usuario y lo adjuntamos en la lista asignando id a Usuario y devolviendo Obtejo Final
                    val usuariosLista = usuariosMap.map { (id, usuario) ->
                        usuario.copy(id = id) //Asignamos el id al objeto usuario
                    }
                    _usuarios.postValue(usuariosLista)//Actualiza el Live Data con la nueva lista de Usuarios
                }
            } catch (e: Exception) {
                //Si ocurre algun error actualizamos el Live data
                _errorMensaje.postValue("Error: ${e.message}")
            }
        }
    }

    // Iniciar sesión en FirebaseAuth, Recibe email y contraseña
    fun signInWithEmailAndPassword(email: String, password: String) {
        //Mediante el método de auth iniciamos sesión por Email y Password
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                //Utilizamos una función lambda para verificar la respuesta del método
                if (task.isSuccessful) {
                    //Si ha sido exitosa entonces decimos que el usuario se ha logueado
                    _isLogged.value = true //Similar a PostValue PostValue(true)
                    //Guardamos el mensaje en el LiveData para recogerlo desde el observador
                    _mensaje.value = "Inicio de sesión exitoso"
                    fetchUsuarioByEmail(email)  // Cargar datos del usuario autenticado
                } else {
                    //Sino seguimos con el usuario sin loguear y guardamos el mensaje de error
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

    // Obtener El Objeto usuario por email desde Firebase para instanciarlo y trabajar con el
    fun fetchUsuarioByEmail(email: String) {
        //Utilizamos una Corrutina, asegurando que las operaciones asíncronas estén vinculadas al ciclo de vida (Permite trabajar en sengundo plano
        viewModelScope.launch {
            try {
                //Recogemos todos los usuarios en una Lista desde FireBase
                val response = RetrofitConnect.apiUsuario.getUsuarios()
                /*Este método recupera todas las entradas del MAP<String,Usuario>
                Siendo key:ID generado por FireBase
                Siendo value: Objeto Usuario
                .find { it.value.correo == email } filtra un usuario cuyo correo coincida con el proporcionado
                it es el Pedido actual que se está recorriendo
                 */
                val usuarioEntry = response?.entries?.find { it.value.correo == email }
                //Sino es nulo entonces guardamos los valores en LiveData
                if (usuarioEntry != null) {
                    val usuarioId = usuarioEntry.key  //  ID generado por Firebase
                    val usuarioEncontrado = usuarioEntry.value.copy(id = usuarioId) //Guardamos el Objeto Usuario Asignando su id
                    //asignamos los valores a LiveData para trabajar con ellos
                    _usuarioAutenticado.value = usuarioEncontrado
                    _rolUsuario.value = usuarioEncontrado.rol?.lowercase()
                    _mensaje.value = "Usuario encontrado: ${usuarioEncontrado.nombre}"

                    // Guardamos el id de Firebase
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
