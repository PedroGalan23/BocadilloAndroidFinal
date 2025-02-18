package com.example.bocadilloandroidfinal._admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bocadilloandroidfinal.databinding.FragmentAdminEditarUsuarioBinding
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel

class FragmentEditarUsuario : Fragment() {
    private lateinit var binding: FragmentAdminEditarUsuarioBinding
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val args: FragmentEditarUsuarioArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminEditarUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usuario = args.usuario // Obtener usuario enviado desde el otro fragmento

        // Lista de roles
        val roles = listOf("Alumno", "Cocina", "Admin")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles)
        binding.spinnerRol.adapter = adapter

        // Prellenar los campos con los datos actuales
        binding.edtLogin.setText(usuario.login)
        binding.edtNombre.setText(usuario.nombre)
        binding.edtApellidos.setText(usuario.apellidos)
        binding.edtCorreo.setText(usuario.correo)
        binding.edtPassword.setText(usuario.password)
        binding.edtCurso.setText(usuario.curso)

        // Seleccionar el rol actual en el spinner
        val posicionRol = roles.indexOfFirst { it.equals(usuario.rol, ignoreCase = true) }
        if (posicionRol >= 0) {
            binding.spinnerRol.setSelection(posicionRol)
        }

        // Botón para guardar cambios
        binding.btnAgregar.setOnClickListener {
            val nuevoLogin = binding.edtLogin.text.toString().trim()
            val nuevoNombre = binding.edtNombre.text.toString().trim()
            val nuevosApellidos = binding.edtApellidos.text.toString().trim()
            val nuevoCorreo = binding.edtCorreo.text.toString().trim()
            val nuevaPassword = binding.edtPassword.text.toString().trim()
            val nuevoCurso = binding.edtCurso.text.toString().trim()
            val nuevoRol = binding.spinnerRol.selectedItem.toString()

            if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty()) {
                Toast.makeText(requireContext(), "Los campos Nombre y Correo son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuarioActualizado = usuario.copy(
                login = nuevoLogin,
                nombre = nuevoNombre,
                apellidos = nuevosApellidos,
                correo = nuevoCorreo,
                password = nuevaPassword,
                curso = nuevoCurso,
                rol = nuevoRol
            )

            usuarioViewModel.updateUsuario(usuarioActualizado) { exito ->
                if (exito) {
                    Toast.makeText(requireContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp() // 🔙 Volver atrás
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Botón para volver atrás
        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
