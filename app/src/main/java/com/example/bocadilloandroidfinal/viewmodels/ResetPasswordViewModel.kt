package com.example.bocadilloandroidfinal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {

    private val _resetStatus = MutableLiveData<String>()
    val resetStatus: LiveData<String> get() = _resetStatus

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _resetStatus.value = "Ingrese un correo válido"
            return
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resetStatus.value = "Correo de restablecimiento enviado"
                } else {
                    _resetStatus.value = "Error al enviar el correo"
                }
            }
    }
}
