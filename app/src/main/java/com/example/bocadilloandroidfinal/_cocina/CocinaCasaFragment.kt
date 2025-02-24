package com.example.bocadilloandroidfinal._cocina

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.adapter.PedidoCocinaAdapter
import com.example.bocadilloandroidfinal.viewmodels.PedidoViewModel

class CocinaCasaFragment : Fragment() {
    //El viewModel con by viewmodels
    private val pedidoViewModel: PedidoViewModel by viewModels()
    //Declaramos lo necesario
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PedidoCocinaAdapter
    private lateinit var btnMarcarRetirado: Button
    private lateinit var btnDesmarcarRetirado: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cocina_casa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewPedidos)
        btnMarcarRetirado = view.findViewById(R.id.btnRetirar)
        btnDesmarcarRetirado = view.findViewById(R.id.btnDesmarcarRetirado)
        //Determinamos el layout con layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //La función lambda se utiliza para determinar la acción de seleccionar del adapter
        adapter = PedidoCocinaAdapter(emptyList()) { pedido ->
            Log.d("CocinaCasaFragment", "Pedido seleccionado: ${pedido.id}")
            pedidoViewModel.seleccionarPedido(pedido)
        }
        recyclerView.adapter = adapter

        //Asignamos una lista coherente que no esté vacía al rv
        pedidoViewModel.pedidosHoy.observe(viewLifecycleOwner) { pedidos ->
            if (pedidos != null) {
                Log.d("CocinaCasaFragment", " Actualizando lista de pedidos en RecyclerView")
                adapter.actualizarLista(pedidos)
            }
        }
        //Observamos el pedido seleccionado para determinar los botones según el estado del pedido
        pedidoViewModel.pedidoSeleccionado.observe(viewLifecycleOwner) { pedido ->
            val habilitarRetirado = pedido != null && !pedido.estado
            val habilitarDesmarcar = pedido != null && pedido.estado
            btnMarcarRetirado.isEnabled = habilitarRetirado
            btnDesmarcarRetirado.isEnabled = habilitarDesmarcar

            Log.d("CocinaCasaFragment", " Botones actualizados -> Retirar: $habilitarRetirado | Desmarcar: $habilitarDesmarcar")
        }

        btnMarcarRetirado.setOnClickListener {
            //Con let evitamos los posibles errores derivados de nulos
            pedidoViewModel.pedidoSeleccionado.value?.let { pedido ->
                Log.d("CocinaCasaFragment", "Marcando pedido ${pedido.id} como RETIRADO")
                //Marcamos el pedido como retirado
                pedidoViewModel.cambiarEstadoPedido(pedido, true)
            }
        }

        btnDesmarcarRetirado.setOnClickListener {
            pedidoViewModel.pedidoSeleccionado.value?.let { pedido ->
                Log.d("CocinaCasaFragment", " Desmarcando pedido ${pedido.id}")
                //Marcamos el pedido como no Retirado
                pedidoViewModel.cambiarEstadoPedido(pedido, false)
            }
        }

        Log.d("CocinaCasaFragment", "🔄 Solicitando pedidos del día...")
        pedidoViewModel.obtenerPedidosDeHoy()
    }
}
