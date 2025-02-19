package com.example.bocadilloandroidfinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.bocadilloandroidfinal.databinding.FragmentAdminEditarBocadilloBinding
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import com.example.bocadilloandroidfinal.viewmodels.BocadilloViewModel

class AdminEditarBocadilloFragment : Fragment() {

    private var _binding: FragmentAdminEditarBocadilloBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BocadilloViewModel by viewModels()
    private val args: AdminEditarBocadilloFragmentArgs by navArgs()
    private lateinit var bocadilloId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEditarBocadilloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bocadillo = args.bocadillo

        // Llenamos los campos con los datos del bocadillo
        binding.edtNombreBocadilloCrud.setText(bocadillo.nombre)
        binding.edtDescripcionBocadilloCrud.setText(bocadillo.descripcion)
        binding.edtCosteBocadilloCrud.setText(bocadillo.coste.toString())
        binding.edtIcono.setText(bocadillo.icono)

        // Configurar los spinners
        setupSpinner(binding.spinnerTipoBocadilloCrud, arrayOf("FRÍO", "CALIENTE"), bocadillo.tipo)
        setupSpinner(binding.spinnerDiaBocadilloCrud, arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes"), bocadillo.dia)

        // Botón de actualización
        binding.btnEditarBocadiloCrud.setOnClickListener {
            actualizarBocadillo()
        }

        /*Botón de eliminación
        binding.btnEditarBocadiloCrud.setOnClickListener {
            eliminarBocadillo()
        }*/
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
            "-OIXyI8q_DQ00NepPj3Z" to "Huevo",
            "-OIXyKU7UQkLrfqvb69p" to "Pescado",
            "-OIXyMgVrLhDMZ1TmBR0" to "Lácteos"
        )

        val bocadilloActualizado = Bocadillo(nombre, tipo, descripcion, coste, icono, dia, alergenos)

        viewModel.actualizarBocadillo(bocadilloId, bocadilloActualizado) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Bocadillo actualizado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eliminarBocadillo() {
        viewModel.eliminarBocadillo(bocadilloId) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Bocadillo eliminado", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
