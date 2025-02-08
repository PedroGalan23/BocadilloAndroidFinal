package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.databinding.FragmentHistorialBinding
import java.sql.Time
import java.util.Date

class HistorialFragment : Fragment() {

    //TODO
    private lateinit var binding:FragmentHistorialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }


    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerview= view.findViewById<RecyclerView>(R.id.rwPedidos)
        recyclerview.layoutManager=LinearLayoutManager(requireContext())
        recyclerview.adapter= PedidoAdapter(getPedidosSemana())

    }

    private fun getPedidosSemana(): List<Pedido> {
        return listOf(
            Pedido(1, 123, Bocadillo(1, "Pechuga", TipoBocadillo.CALIENTE, "Pollo a la plancha", "Gluten", 2.50,
                R.drawable.ic_pollo
            ), Date(2025, 2, 1), Time(10, 30, 0), true, 2.50),
            Pedido(2, 123, Bocadillo(2, "Jamon York", TipoBocadillo.FRIO, "Jamón con queso", "Gluten", 2.00,
                R.drawable.ic_jamonyork
            ), Date(2025, 2, 2), Time(11, 15, 0), true, 2.00),
            Pedido(3, 123, Bocadillo(3, "Lomo", TipoBocadillo.CALIENTE, "Lomo adobado", "Gluten", 2.80,
                R.drawable.ic_lomo
            ), Date(2025, 2, 3), Time(9, 45, 0), false, 2.80),
            Pedido(4, 123, Bocadillo(4, "Queso Fresco", TipoBocadillo.FRIO, "Queso de cabra", "Gluten", 2.30,
                R.drawable.ic_queso
            ), Date(2025, 2, 4), Time(12, 0, 0), true, 2.30),
            Pedido(5, 123, Bocadillo(5, "Bacon", TipoBocadillo.CALIENTE, "Bacon con queso", "Gluten", 2.90,
                R.drawable.ic_bacon
            ), Date(2025, 2, 5), Time(10, 50, 0), false, 2.90),
            Pedido(6, 123, Bocadillo(6, "Atún", TipoBocadillo.FRIO, "Atún con mayonesa", "Gluten", 2.40,
                R.drawable.ic_atun
            ), Date(2025, 2, 6), Time(11, 30, 0), true, 2.40),
            Pedido(7, 123, Bocadillo(7, "Hamburguesa", TipoBocadillo.CALIENTE, "Carne de ternera", "Gluten", 3.50,
                R.drawable.ic_hamburguesa
            ), Date(2025, 2, 7), Time(12, 15, 0), true, 3.50),
            Pedido(8, 123, Bocadillo(8, "Ensaladilla", TipoBocadillo.FRIO, "Patata y zanahoria", "Gluten", 2.20,
                R.drawable.ic_mahonesa
            ), Date(2025, 2, 8), Time(9, 30, 0), false, 2.20),
            Pedido(9, 123, Bocadillo(9, "Chorizo", TipoBocadillo.CALIENTE, "Chorizo picante", "Gluten", 2.70,
                R.drawable.ic_chorizo
            ), Date(2025, 2, 9), Time(10, 10, 0), true, 2.70),
            Pedido(10, 123, Bocadillo(10, "Pavo", TipoBocadillo.FRIO, "Pavo con queso", "Gluten", 2.60,
                R.drawable.ic_pavo
            ), Date(2025, 2, 10), Time(11, 45, 0), true, 2.60),
            Pedido(11, 123, Bocadillo(11, "Salchichas", TipoBocadillo.CALIENTE, "Salchichas con ketchup", "Gluten", 2.50,
                R.drawable.ic_salchichas
            ), Date(2025, 2, 11), Time(10, 20, 0), false, 2.50),
            Pedido(12, 123, Bocadillo(12, "Huevo Duro", TipoBocadillo.FRIO, "Huevo con tomate", "Gluten", 2.10,
                R.drawable.ic_huevo
            ), Date(2025, 2, 12), Time(12, 10, 0), true, 2.10),
            Pedido(13, 123, Bocadillo(13, "Chistorra", TipoBocadillo.CALIENTE, "Chistorra a la plancha", "Gluten", 2.80,
                R.drawable.ic_chistorra
            ), Date(2025, 2, 13), Time(9, 55, 0), false, 2.80),
            Pedido(14, 123, Bocadillo(14, "Vegetal", TipoBocadillo.FRIO, "Lechuga y tomate", "Gluten", 2.00,
                R.drawable.ic_vegeta
            ), Date(2025, 2, 14), Time(11, 5, 0), true, 2.00),
            Pedido(15, 123, Bocadillo(1, "Pechuga", TipoBocadillo.CALIENTE, "Pollo a la plancha", "Gluten", 2.50,
                R.drawable.ic_pollo
            ), Date(2025, 2, 15), Time(10, 40, 0), true, 2.50),
            Pedido(16, 123, Bocadillo(3, "Lomo", TipoBocadillo.CALIENTE, "Lomo adobado", "Gluten", 2.80,
                R.drawable.ic_lomo
            ), Date(2025, 2, 16), Time(9, 20, 0), false, 2.80),
            Pedido(17, 123, Bocadillo(7, "Hamburguesa", TipoBocadillo.CALIENTE, "Carne de ternera", "Gluten", 3.50,
                R.drawable.ic_hamburguesa
            ), Date(2025, 2, 17), Time(12, 30, 0), true, 3.50),
            Pedido(18, 123, Bocadillo(10, "Pavo", TipoBocadillo.FRIO, "Pavo con queso", "Gluten", 2.60,
                R.drawable.ic_pavo
            ), Date(2025, 2, 18), Time(11, 50, 0), true, 2.60),
            Pedido(19, 123, Bocadillo(5, "Bacon", TipoBocadillo.CALIENTE, "Bacon con queso", "Gluten", 2.90,
                R.drawable.ic_bacon
            ), Date(2025, 2, 19), Time(10, 15, 0), false, 2.90),
            Pedido(20, 123, Bocadillo(2, "Jamon York", TipoBocadillo.FRIO, "Jamón con queso", "Gluten", 2.00,
                R.drawable.ic_jamonyork
            ), Date(2025, 2, 20), Time(9, 35, 0), true, 2.00)
        )
    }*/
}
