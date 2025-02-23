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

    private val pedidoViewModel: PedidoViewModel by viewModels()
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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PedidoCocinaAdapter(emptyList()) { pedido ->
            Log.d("CocinaCasaFragment", "🟡 Pedido seleccionado: ${pedido.id}")
            pedidoViewModel.seleccionarPedido(pedido)
        }
        recyclerView.adapter = adapter

        pedidoViewModel.pedidosHoy.observe(viewLifecycleOwner) { pedidos ->
            if (pedidos != null) {
                Log.d("CocinaCasaFragment", "🔄 Actualizando lista de pedidos en RecyclerView")
                adapter.actualizarLista(pedidos)
            }
        }

        pedidoViewModel.pedidoSeleccionado.observe(viewLifecycleOwner) { pedido ->
            val habilitarRetirado = pedido != null && !pedido.estado
            val habilitarDesmarcar = pedido != null && pedido.estado
            btnMarcarRetirado.isEnabled = habilitarRetirado
            btnDesmarcarRetirado.isEnabled = habilitarDesmarcar

            Log.d("CocinaCasaFragment", "🎯 Botones actualizados -> Retirar: $habilitarRetirado | Desmarcar: $habilitarDesmarcar")
        }

        btnMarcarRetirado.setOnClickListener {
            pedidoViewModel.pedidoSeleccionado.value?.let { pedido ->
                Log.d("CocinaCasaFragment", "✅ Marcando pedido ${pedido.id} como RETIRADO")
                pedidoViewModel.cambiarEstadoPedido(pedido, true)
            }
        }

        btnDesmarcarRetirado.setOnClickListener {
            pedidoViewModel.pedidoSeleccionado.value?.let { pedido ->
                Log.d("CocinaCasaFragment", "❌ Desmarcando pedido ${pedido.id}")
                pedidoViewModel.cambiarEstadoPedido(pedido, false)
            }
        }

        Log.d("CocinaCasaFragment", "🔄 Solicitando pedidos del día...")
        pedidoViewModel.obtenerPedidosDeHoy()
    }
}
