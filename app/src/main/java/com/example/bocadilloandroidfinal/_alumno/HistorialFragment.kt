package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bocadilloandroidfinal.adapter.PedidoAdapter
import com.example.bocadilloandroidfinal.databinding.FragmentHistorialBinding
import com.example.bocadilloandroidfinal.viewmodels.PedidoViewModel
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel

class HistorialFragment : Fragment() {
    //Definimos el binding de forma segura para evitar problemas de fugas
    private var _binding: FragmentHistorialBinding? = null
    private val binding get() = _binding!!
    //Definimos los viewModels
    private val pedidoViewModel: PedidoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    private lateinit var pedidoAdapter: PedidoAdapter

    //Creamos la vista a partir de binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistorialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializamos el adapter con una lista vacía
        pedidoAdapter = PedidoAdapter(emptyList())
        //Apply permite realizar varias operaciones en una sobre un objeto
        binding.rwPedidos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pedidoAdapter
        }

        //Observamos el usuarioAutenticado para cargar los pedidos del alumno
        usuarioViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { usuario ->
            //Utilizamos .let para evitar el errror sobre los nullable
            usuario?.let {
                it.id?.let { it1 -> pedidoViewModel.obtenerPedidosAlumno(it1) }
            }
        }

        //Observamos los pedidos del alumno para reiniciar el rv si hay algún cambio
        pedidoViewModel.pedidosAlumno.observe(viewLifecycleOwner) { pedidos ->
            pedidoAdapter.actualizarLista(pedidos)
        }
        //Observamos el total de bocadillos para recargar si se hace una insercción o una modificación
        pedidoViewModel.totalBocadillos.observe(viewLifecycleOwner) { total ->
            binding.txtBocadillosCantidad.text = total.toString()
        }

        //Observamos el total gastado para recargar si hace falta
        pedidoViewModel.totalGastado.observe(viewLifecycleOwner) { total ->
            binding.txtTotalGastado.text = "%.2f€".format(total)
        }
    }
    //Destruimos la vista bind
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
