package com.example.bocadilloandroidfinal._admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.adapter.UsuarioAdapter
import com.example.bocadilloandroidfinal.databinding.FragmentAdminAlumnoBinding
import com.example.bocadilloandroidfinal.modelos.Usuario
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel

class AdminAlumnoFragment : Fragment() {
    private var _binding: FragmentAdminAlumnoBinding? = null
    private val binding get() = _binding!!



    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var usuarioAdapter: UsuarioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inicializar correctamente `binding`
        _binding = FragmentAdminAlumnoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usuarioViewModel.fetchUsuarios()

        //  Usar binding.recyclerViewAlumnos en lugar de findViewById
        binding.recyclerViewAlumnos.layoutManager = LinearLayoutManager(requireContext())
        usuarioAdapter = UsuarioAdapter(
            lista_usuarios = emptyList(),
            onEditarClick = { usuario -> irAEditarUsuario(usuario) }, //  Editar usuario
            onEliminarClick = { usuario -> eliminarUsuario(usuario) }  // Eliminar usuario
        )
        binding.recyclerViewAlumnos.adapter = usuarioAdapter

        usuarioViewModel.usuarios.observe(viewLifecycleOwner) { usuarios ->
            if (!usuarios.isNullOrEmpty()) {
                usuarioAdapter.actualizarLista(usuarios)
            } else {
                Toast.makeText(requireContext(), "No hay datos", Toast.LENGTH_SHORT).show()
            }
        }

        usuarioViewModel.errorMensaje.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
        }

        binding.fabAgregar.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_admin_alumno_to_adminAgregarUsuarioFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }

    private fun irAEditarUsuario(usuario: Usuario) {
        val action = AdminAlumnoFragmentDirections.actionFragmentAdminAlumnoToAdminEditarUsuarioFragment(usuario)
        findNavController().navigate(action)
    }


    private fun eliminarUsuario(usuario: Usuario) {

        usuarioViewModel.deleteUsuario(usuario.id!!) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show()
                usuarioViewModel.fetchUsuarios() // 🔄 Refrescar lista
            } else {
                Toast.makeText(requireContext(), "Error al eliminar usuario", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}
