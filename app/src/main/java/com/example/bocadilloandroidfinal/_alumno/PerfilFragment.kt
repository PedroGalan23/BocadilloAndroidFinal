package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bocadilloandroidfinal.databinding.FragmentPerfilBinding
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //utilizamos una función lambda para vincular los valores del usuario con la vista
        usuarioViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null) {
                binding.etNombre.setText(usuario.nombre)
                binding.etApellidos.setText(usuario.apellidos)
                binding.etCurso.setText(usuario.curso)
                binding.tvCorreo.text = usuario.correo
                binding.tvRol.text = usuario.rol
                binding.tvId.text = usuario.id  // Ahora se muestra el ID del usuario
            }
        }

        // Si se devuelve algun mensaje estamos en constante Escucha (Nombre actualizado,Apellido Actualizado)
        usuarioViewModel.mensaje.observe(viewLifecycleOwner) { mensaje ->
            if (!mensaje.isNullOrEmpty()) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
            }
        }

        //Si se da algún mensaje de error estamos en constante Escucha para determinar el error
        usuarioViewModel.errorMensaje.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnActualizarPerfil.setOnClickListener {
            //Pasamos los valores omitiendo los espacios
            val nuevoNombre = binding.etNombre.text.toString().trim()
            val nuevosApellidos = binding.etApellidos.text.toString().trim()
            val nuevoCurso = binding.etCurso.text.toString().trim()

            //Actualizamos solo algunos valores del Usuario
            usuarioViewModel.updateUsuarioPerfil(
                nombre = nuevoNombre,
                apellidos = nuevosApellidos,
                curso = nuevoCurso
            )
        }
    }
    //Destruimos el bind

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
