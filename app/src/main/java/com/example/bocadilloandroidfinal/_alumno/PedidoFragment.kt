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
    //Declaramos el binding de forma segura para evitar problemas de fugas
    private var _binding: FragmentPedidoBinding? = null
    private val binding get() = _binding!!

    //Declaramos los ViewModels que vamos a utilizar
    private val bocadilloViewModel: BocadilloViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val pedidoViewModel: PedidoViewModel by viewModels()
    //Declaramos el valor del bocadillo seleccionado
    private var bocadilloSeleccionado: Bocadillo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Declaramos el fragment con binding
        _binding = FragmentPedidoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //por defecto el btn Confirmar pedido estará desaparecido Gone invisible
        binding.btnConfirmar.visibility = View.GONE

        //instanciamos el bocadilloAdapter con una emptyList
        val bocadilloAdapter = BocadilloDiaAdapter(emptyList()) { bocadillo ->
            bocadilloSeleccionado = bocadillo
            binding.btnConfirmar.visibility = View.VISIBLE
        }

        //Al recyclerView le aplicamos el layput Manager y el adapter una manera mas rápida de determinarlo
        //Apply permite hacer varias operaciones sobre un objeto en un bloque de código
        binding.recyclerBocadillos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bocadilloAdapter
        }

        //Desde el bocadilloViewModel nos traemos los bocadillos del dia
        bocadilloViewModel.fetchBocadillosDia()

        //Estamos en constante observación de la lista para avisar al RecyclerView si existen actualizaciones
        bocadilloViewModel.bocadillos.observe(viewLifecycleOwner) { bocadillosList ->
            bocadilloAdapter.actualizarLista(bocadillosList)
        }

        //Observamos al usuario autenticado por si existe algun cambio y obtenemos los pedidos del dia
        usuarioViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { usuario ->
            //Si el objeto es null el bloque de código no se ejecuta
            usuario?.let {
                it.id?.let { it1 -> pedidoViewModel.obtenerPedidoDelDia(it1) }
            }
        }

        //Observamos el LiveData por si hay algún cambio en el estado o en la confirmación del pedido para redirigir a resumen o confirmado
        pedidoViewModel.pedidoDelDia.observe(viewLifecycleOwner) { pedido ->
            if (pedido != null) {
                if (pedido.estado) {
                    // Si el pedido está marcado como retirado, navega a RetiradoFragment
                    findNavController().navigate(R.id.action_fragment_pedido_to_retiradoFragment)
                } else {
                    //Si hay un pedido del dia para ese alumno se manda a resumenFragment
                    // Si el pedido no está marcado como retirado, navega a ResumenFragment
                    findNavController().navigate(R.id.action_fragment_pedido_to_resumenFragment)
                }
            }
        }
        //Al boton confirmar le métemos un ConfirmacionDialogFragment
        binding.btnConfirmar.setOnClickListener {
            ConfirmacionDialogFragment {
                val usuarioId = usuarioViewModel.usuarioIdFirebase
                if (usuarioId.isNullOrBlank() || bocadilloSeleccionado == null) {
                    return@ConfirmacionDialogFragment
                    //Si el usuario no esta registrado o el bocadilloSeleccionado es nulo se sale de la función
                }
                //Realiza el pedido
                pedidoViewModel.realizarPedido(usuarioId, bocadilloSeleccionado!!)
                findNavController().navigate(R.id.action_fragment_pedido_to_resumenFragment) //  Navega a resumen tras hacer el pedido
            }.show(parentFragmentManager, "ConfirmDialog")
        }
    }
    //Destruimos el bind

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
