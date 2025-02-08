package com.example.bocadilloandroidfinal._alumno

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.adapter.BocadilloCalendarioAdapter
import com.example.bocadilloandroidfinal.databinding.FragmentCalendarioBinding

class CalendarioFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bocadilloAdapter: BocadilloCalendarioAdapter
    //TODO
    private lateinit var binding: FragmentCalendarioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento
        return inflater.inflate(R.layout.fragment_calendario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        recyclerView = view.findViewById(R.id.recyclerViewBocadillos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        bocadilloAdapter = BocadilloAdapter(getBocadillosSemana()) { bocadillo ->
            println("Bocata Seleccionado")
        }

        recyclerView.adapter = bocadilloAdapter

        recyclerView.addItemDecoration(GridSpacingItemDecoration(16))
        */
    }

}
