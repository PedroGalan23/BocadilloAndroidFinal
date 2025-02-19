package com.example.bocadilloandroidfinal._admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.adapter.BocadilloCrudAdapter
import com.example.bocadilloandroidfinal.adapter.UsuarioAdapter
import com.example.bocadilloandroidfinal.databinding.FragmentAdminAlumnoBinding
import com.example.bocadilloandroidfinal.databinding.FragmentAdminBocadilloBinding
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import com.example.bocadilloandroidfinal.modelos.Usuario
import com.example.bocadilloandroidfinal.viewmodels.BocadilloViewModel

class AdminBocadilloFragment : Fragment() {
    private var _binding: FragmentAdminBocadilloBinding? = null
    private val binding get() = _binding!!

    private val bocadilloViewModel: BocadilloViewModel by viewModels()
    private lateinit var bocadilloAdapter: BocadilloCrudAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inicializar correctamente `binding`
        _binding = FragmentAdminBocadilloBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bocadilloViewModel.fetchBocadillos()

        //  Usar binding.recyclerViewAlumnos en lugar de findViewById
        binding.recyclerViewBocadillos.layoutManager = LinearLayoutManager(requireContext())
        bocadilloAdapter = BocadilloCrudAdapter(
            lista_bocadillos = emptyList(),
            onEditarClick = { bocadillo -> irAEditarBocadillo(bocadillo) }, // Pasa el bocadillo correcto
            onEliminarClick = { bocadillo -> eliminarBocadillo(bocadillo) }  // Ahora permite eliminar
        )
        binding.recyclerViewBocadillos.adapter = bocadilloAdapter


        bocadilloViewModel.bocadillos.observe(viewLifecycleOwner) { usuarios ->
            if (!usuarios.isNullOrEmpty()) {
                bocadilloAdapter.actualizarLista(usuarios)
            } else {
                Toast.makeText(requireContext(), "No hay datos", Toast.LENGTH_SHORT).show()
            }
        }

        bocadilloViewModel.errorMessage.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
        }

        binding.fabAgregar.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_admin_bocadillo_to_adminAgregarBocadilloFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evitar fugas de memoria
    }

    private fun irAEditarBocadillo(bocadillo: Bocadillo) {
        val action = AdminBocadilloFragmentDirections
            .actionFragmentAdminBocadilloToAdminEditarBocadilloFragment(bocadillo)
        findNavController().navigate(action)
    }
    private fun eliminarBocadillo(bocadillo: Bocadillo) {
        bocadilloViewModel.eliminarBocadillo(bocadillo.id) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Bocadillo eliminado", Toast.LENGTH_SHORT).show()
                bocadilloViewModel.fetchBocadillos() // 🔄 Refrescar lista
            } else {
                Toast.makeText(requireContext(), "Error al eliminar bocadillo", Toast.LENGTH_SHORT).show()
            }
        }
    }


}