package com.example.bocadilloandroidfinal._admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.databinding.FragmentAdminEditarBocadillo1Binding
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import com.example.bocadilloandroidfinal.viewmodels.BocadilloViewModel


class AdminEditarBocadilloFragment : Fragment() {
    private lateinit var binding:FragmentAdminEditarBocadillo1Binding
    private val bocadilloViewModel:BocadilloViewModel by viewModels()
    private val args: AdminEditarBocadilloFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAdminEditarBocadillo1Binding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bocadillo=args.bocadillo

        //Lista de atributos
        // Llenamos los campos con los datos del bocadillo
        binding.edtNombreBocadilloCrud.setText(bocadillo.nombre)
        binding.edtDescripcionBocadilloCrud.setText(bocadillo.descripcion)
        binding.edtCosteBocadilloCrud.setText(bocadillo.coste.toString())
        binding.edtIcono.setText(bocadillo.icono)
        // Configurar los spinners
        setupSpinner(binding.spinnerTipoBocadilloCrud, arrayOf("FRÍO", "CALIENTE"), bocadillo.tipo)
        setupSpinner(binding.spinnerDiaBocadilloCrud, arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes"), bocadillo.dia)

        binding.btnEditarBocadiloCrud.setOnClickListener {
            actualizarBocadillo()
        }
        // Botón para volver atrás
        binding.btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_adminEditarBocadilloFragment_to_fragment_admin_bocadillo)
        }
    }
    private fun setupSpinner(spinner: Spinner, items: Array<String>, selectedValue: String) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        spinner.adapter = adapter
        spinner.setSelection(items.indexOf(selectedValue))
    }

    private fun actualizarBocadillo() {
        val nombre = binding.edtNombreBocadilloCrud.text.toString()
        val descripcion = binding.edtDescripcionBocadilloCrud.text.toString()
        val coste = binding.edtCosteBocadilloCrud.text.toString().toDoubleOrNull() ?: 0.0
        val tipo = binding.spinnerTipoBocadilloCrud.selectedItem.toString()
        val dia = binding.spinnerDiaBocadilloCrud.selectedItem.toString()
        val icono = binding.edtIcono.text.toString()
        val alergenos = mapOf(
            "-OIXyGEpWE3eNaVReeum" to "Gluten",
        )
        val bocadilloActualizado = Bocadillo(
            nombre = nombre,
            descripcion = descripcion,
            tipo = tipo,
            coste = coste,
            alergenos = alergenos,
            icono = icono,
            dia=dia
        )
        args.bocadillo.id?.let {
            bocadilloViewModel.actualizarBocadillo(it, bocadilloActualizado) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Bocadillo actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}