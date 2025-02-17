package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.databinding.FragmentResumenPedidoBinding
import com.example.bocadilloandroidfinal.viewmodels.PedidoViewModel
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel

class ResumenPedidoFragment : Fragment() {
    private var _binding: FragmentResumenPedidoBinding? = null
    private val binding get() = _binding!!

    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResumenPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usuarioViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { usuario ->
            usuario?.let {
                it.id?.let { it1 -> pedidoViewModel.obtenerPedidoDelDia(it1) }
            }
        }

        pedidoViewModel.pedidoDelDia.observe(viewLifecycleOwner) { pedido ->
            if (pedido != null) {
                binding.txtNombreBocadillo.text = pedido.bocadillo.nombre
                binding.txtDescripcionBocadillo.text = pedido.bocadillo.descripcion
                binding.txtCosteBocadillo.text = "${pedido.precio}€"

                val iconoId = resources.getIdentifier(
                    pedido.bocadillo.icono, "drawable", requireContext().packageName
                )
                if (iconoId != 0) {
                    binding.imgBocadillo.setImageResource(iconoId)
                }

                binding.btnCancelarPedido.setOnClickListener {
                    pedidoViewModel.cancelarPedido()
                    findNavController().navigate(R.id.action_resumenFragment_to_fragment_pedido) // 🔥 Ahora vuelve a PedidoFragment
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
