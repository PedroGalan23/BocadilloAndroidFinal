package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.R

class ResumenPedidoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resumen_pedido, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCancelar = view.findViewById<Button>(R.id.btnCancelarPedido)

        btnCancelar.setOnClickListener {
            findNavController().navigate(R.id.action_resumenFragment_to_fragment_pedido)
        }
    }
}
