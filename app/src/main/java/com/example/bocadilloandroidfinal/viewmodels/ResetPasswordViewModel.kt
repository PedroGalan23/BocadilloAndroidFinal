package com.example.bocadilloandroidfinal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordViewModel : ViewModel() {

    //Utilizamos status para avisar de algún error en el email o confirmar que el Correo de Recuperación se ha enviado
    private val _resetStatus = MutableLiveData<String>()
    val resetStatus: LiveData<String> get() = _resetStatus

    //Recibe un Email y envia el correo mediante FireBaseAUTH
    fun resetPassword(email: String) {
        //Si la variable email está vacía cambia el status y se mostrará por pantalla el error
        if (email.isBlank()) {
            _resetStatus.value = "Ingrese un correo válido"
            return
        }
        //Sino es así se enviará el correo
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Se cambiará el status a modo de conrfirmación
                    _resetStatus.value = "Correo de restablecimiento enviado"
                } else {
                    //Si existe algune error se cambiará el status mostrando el error
                    _resetStatus.value = "Error al enviar el correo"
                }
            }
    }
}
