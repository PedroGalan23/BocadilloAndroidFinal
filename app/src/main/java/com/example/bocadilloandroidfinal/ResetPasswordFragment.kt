package com.example.bocadilloandroidfinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.viewmodels.ResetPasswordViewModel

class ResetPasswordFragment : Fragment() {

    // Se usa by viewModels para obtener una instancia del ViewModel
    private val viewModel: ResetPasswordViewModel by viewModels()

    //Creamos la interfaz
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        //Le adjuntamos la interfaz definidica para esta actividad
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText: EditText = view.findViewById(R.id.emailEditText)
        val resetPasswordButton: Button = view.findViewById(R.id.resetPasswordButton)
        val statusTextView: TextView = view.findViewById(R.id.statusTextView)
        val volver: Button = view.findViewById(R.id.backButton)

        //Llamamos a resetPassword del viewModel
        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            viewModel.resetPassword(email)
        }

        //Utilizamos LiveData para observar los cambios en el estado del reseteo
        //Lamda función anónima, cada vez que el valor del estatus cambie se ejecutará el lamda
        viewModel.resetStatus.observe(viewLifecycleOwner) { status ->
            statusTextView.text = status //Si estatus contiene Correo Enviado Correctamente este mensaje se mostrará por pantalla
            statusTextView.visibility = View.VISIBLE //Asegura que se muestre por pantalla en modo de confirmación
        }

        //Acción para volver
        volver.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
    }
}
