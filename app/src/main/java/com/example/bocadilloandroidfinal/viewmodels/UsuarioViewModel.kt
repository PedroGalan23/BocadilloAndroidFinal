package com.example.bocadilloandroidfinal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class UsuarioViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean> get() = _isLogged

    init {
        // Actualizar estado al iniciar
        _isLogged.value = auth.currentUser != null
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLogged.value = true // Actualiza LiveData cuando inicia sesión correctamente
                } else {
                    _isLogged.value = false // Indica fallo de autenticación
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _isLogged.value = false // Actualiza LiveData cuando se cierra sesión
    }
}
