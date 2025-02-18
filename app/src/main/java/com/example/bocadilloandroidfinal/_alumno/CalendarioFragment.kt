package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import android.util.Log // Importar para Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bocadilloandroidfinal.adapter.BocadilloCalendarioAdapter
import com.example.bocadilloandroidfinal.databinding.FragmentCalendarioBinding
import com.example.bocadilloandroidfinal.viewmodels.BocadilloViewModel

class CalendarioFragment : Fragment() {

    private var _binding: FragmentCalendarioBinding? = null
    private val binding get() = _binding!!

    private lateinit var bocadilloAdapter: BocadilloCalendarioAdapter
    private lateinit var bocadilloViewModel: BocadilloViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("DEBUG", "CalendarioFragment -> onCreateView") // 👀 Depuración
        _binding = FragmentCalendarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DEBUG", "CalendarioFragment -> onViewCreated")

        try {
            // Inicializar el ViewModel
            bocadilloViewModel = ViewModelProvider(this)[BocadilloViewModel::class.java]
            Log.d("DEBUG", "ViewModel inicializado correctamente")
        } catch (e: Exception) {
            Log.e("ERROR", "Error al inicializar ViewModel", e)
        }

        // Inicializar el Adapter con una lista vacía
        bocadilloAdapter = BocadilloCalendarioAdapter(emptyList())
        Log.d("DEBUG", "Adaptador inicializado correctamente")

        binding.recyclerViewBocadillos.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = bocadilloAdapter
        }
        Log.d("DEBUG", "RecyclerView configurado con GridLayoutManager")
        bocadilloViewModel.fetchBocadillos()

        // 👇 PRIMERO se activa el observe, DESPUÉS se ejecuta fetchBocadillos()
        bocadilloViewModel.bocadillos.observe(viewLifecycleOwner) { bocadillosList ->
            Log.d("DEBUG", "Observe activado -> Se recibieron ${bocadillosList.size} bocadillos")

            if (bocadillosList.isNotEmpty()) {
                bocadilloAdapter.actualizarLista(bocadillosList)
                Log.d("DEBUG", "Adaptador actualizado correctamente con ${bocadillosList.size} bocadillos")
            } else {
                Log.d("DEBUG", "Observe activado pero la lista de bocadillos está vacía")
            }
        }

        // AHORA se llama a fetchBocadillos(), asegurando que el observe ya está listo
        Log.d("DEBUG", "fetchBocadillos() ejecutado")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("DEBUG", "CalendarioFragment -> onDestroyView") // 👀 Depuración
        _binding = null
    }
}
