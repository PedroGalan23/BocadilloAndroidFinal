package com.example.bocadilloandroidfinal._admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.databinding.FragmentAdminAgregarAlumno1Binding
import com.example.bocadilloandroidfinal.modelos.Usuario
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel


class AdminAgregarAlumnoFragment : Fragment() {
    private lateinit var binding: FragmentAdminAgregarAlumno1Binding
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminAgregarAlumno1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lista de roles
        val roles = listOf("Alumno", "Cocina", "Admin")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles)
        binding.spinnerRol.adapter = adapter
        binding.btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_adminAgregarAlumnoFragment_to_fragment_admin_alumno)

        }
        binding.btnAgregar.setOnClickListener {
            guardarAlumno()
        }

    }
    private fun guardarAlumno() {
        val login = binding.edtLogin.text.toString().trim()
        val nombre = binding.edtNombre.text.toString().trim()
        val apellidos = binding.edtApellidos.text.toString().trim()
        val correo = binding.edtCorreo.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val curso = binding.edtCurso.text.toString().trim()
        val rol = binding.spinnerRol.selectedItem.toString()

        if (login.isEmpty() || nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoUsuario = Usuario(
            id = null, // 🔥 Firebase generará el ID
            login = login,
            nombre = nombre,
            apellidos = apellidos,
            correo = correo,
            password = password,
            curso = curso,
            rol = rol
        )

            usuarioViewModel.insertarAlumno(nuevoUsuario) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Alumno guardado con éxito", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminAgregarAlumnoFragment_to_fragment_admin_alumno)
            } else {
                Toast.makeText(requireContext(), "Error al guardar el alumno", Toast.LENGTH_SHORT).show()
            }
        }
    }
}