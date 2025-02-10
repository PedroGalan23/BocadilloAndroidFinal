package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.adapter.BocadilloDiaAdapter
import com.example.bocadilloandroidfinal.databinding.FragmentPedidoBinding
import com.example.bocadilloandroidfinal.util.ConfirmacionDialogFragment
import com.example.bocadilloandroidfinal.viewmodels.BocadilloViewModel
import java.text.SimpleDateFormat
import java.util.*

class PedidoFragment : Fragment() {

    private var _binding: FragmentPedidoBinding? = null
    private val binding get() = _binding!!

    private val bocadilloViewModel: BocadilloViewModel by viewModels()
    private lateinit var bocadilloAdapter: BocadilloDiaAdapter

    private var bocadilloSeleccionado: String? = null // Guardar el nombre del bocadillo seleccionado

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmar.visibility = View.GONE

        bocadilloAdapter = BocadilloDiaAdapter(emptyList()) { bocadillo ->
            bocadilloSeleccionado = bocadillo.nombre
            binding.btnConfirmar.visibility = View.VISIBLE
        }

        binding.recyclerBocadillos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bocadilloAdapter
        }

        bocadilloViewModel.fetchBocadillosDia()

        bocadilloViewModel.bocadillos.observe(viewLifecycleOwner, Observer { bocadillosList ->
            bocadilloAdapter.actualizarLista(bocadillosList)
        })
        Log.d("DEBUG", "RecyclerView después de asignar datos: ${bocadilloAdapter.itemCount}")

        //Manejar la confirmación del pedido
        binding.btnConfirmar.setOnClickListener {
            ConfirmacionDialogFragment {
                if (!bocadilloSeleccionado.isNullOrEmpty()) {
                    findNavController().navigate(R.id.action_fragment_pedido_to_resumenFragment)
                }
            }.show(parentFragmentManager, "ConfirmDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
