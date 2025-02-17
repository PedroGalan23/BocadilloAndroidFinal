package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.adapter.BocadilloDiaAdapter
import com.example.bocadilloandroidfinal.databinding.FragmentPedidoBinding
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import com.example.bocadilloandroidfinal.util.ConfirmacionDialogFragment
import com.example.bocadilloandroidfinal.viewmodels.BocadilloViewModel
import com.example.bocadilloandroidfinal.viewmodels.PedidoViewModel
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel

class PedidoFragment : Fragment() {

    private var _binding: FragmentPedidoBinding? = null
    private val binding get() = _binding!!

    private val bocadilloViewModel: BocadilloViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()

    private var bocadilloSeleccionado: Bocadillo? = null

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

        val bocadilloAdapter = BocadilloDiaAdapter(emptyList()) { bocadillo ->
            bocadilloSeleccionado = bocadillo
            binding.btnConfirmar.visibility = View.VISIBLE
        }

        binding.recyclerBocadillos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bocadilloAdapter
        }

        bocadilloViewModel.fetchBocadillosDia()

        bocadilloViewModel.bocadillos.observe(viewLifecycleOwner) { bocadillosList ->
            bocadilloAdapter.actualizarLista(bocadillosList)
        }

        usuarioViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                it.id?.let { it1 -> pedidoViewModel.obtenerPedidoDelDia(it1) }
            }
        }

        pedidoViewModel.pedidoDelDia.observe(viewLifecycleOwner) { pedido ->
            if (pedido != null) {
                findNavController().navigate(R.id.action_fragment_pedido_to_resumenFragment) // 🔥 Redirige si ya hay pedido
            }
        }

        binding.btnConfirmar.setOnClickListener {
            ConfirmacionDialogFragment {
                val usuarioId = usuarioViewModel.usuarioIdFirebase
                if (usuarioId.isNullOrBlank() || bocadilloSeleccionado == null) {
                    return@ConfirmacionDialogFragment
                }

                pedidoViewModel.realizarPedido(usuarioId, bocadilloSeleccionado!!)
                findNavController().navigate(R.id.action_fragment_pedido_to_resumenFragment) // 🔥 Navega a resumen tras hacer el pedido
            }.show(parentFragmentManager, "ConfirmDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
