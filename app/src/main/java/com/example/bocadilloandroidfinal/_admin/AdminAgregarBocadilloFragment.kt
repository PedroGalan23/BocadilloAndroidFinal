package com.example.bocadilloandroidfinal._admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.databinding.FragmentAdminAgregarBocadilloBinding
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import com.example.bocadilloandroidfinal.viewmodels.BocadilloViewModel

class AdminAgregarBocadilloFragment : Fragment() {
    private lateinit var binding: FragmentAdminAgregarBocadilloBinding
    private val bocadilloViewModel: BocadilloViewModel by viewModels()

    // Map con los alérgenos y sus IDs correspondientes
    private val alergenosDisponibles = mapOf(
        "-OIXyGEpWE3eNaVReeum" to "Gluten",
        "-OIXyI8q_DQ00NepPj3Z" to "Huevo",
        "-OIXyKU7UQkLrfqvb69p" to "Pescado",
        "-OIXyMgVrLhDMZ1TmBR0" to "Lácteos"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminAgregarBocadilloBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarSpinners()
        configurarAlergenos()

        binding.btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_adminAgregarBocadilloFragment_to_fragment_admin_bocadillo)
        }

        binding.btnAgregar.setOnClickListener {
            guardarBocadillo()
        }
    }

    /** Configura el Spinner de tipos de bocadillos */
    private fun configurarSpinners() {
        val tiposBocadillo = listOf("Frío", "Caliente")
        val adapter = android.widget.ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, tiposBocadillo)
        binding.spinnerTipo.adapter = adapter
    }

    /** Configura los CheckBoxes para la selección múltiple de alérgenos con sus respectivos IDs */
    private fun configurarAlergenos() {
        for ((id, nombre) in alergenosDisponibles) {
            val checkBox = CheckBox(requireContext()).apply {
                text = nombre
                tag = id // Guardamos el ID en el tag del CheckBox
            }
            binding.layoutAlergenos.addView(checkBox)
        }
    }

    /** Obtiene los alérgenos seleccionados devolviendo un Map con ID como clave y nombre como valor */
    private fun obtenerAlergenosSeleccionados(): Map<String, String> {
        val alergenosSeleccionados = mutableMapOf<String, String>()
        for (i in 0 until binding.layoutAlergenos.childCount) {
            val checkBox = binding.layoutAlergenos.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                val id = checkBox.tag as String // Obtenemos el ID almacenado en el tag
                alergenosSeleccionados[id] = checkBox.text.toString()
            }
        }
        return alergenosSeleccionados
    }

    /** Guarda el bocadillo en Firebase */
    private fun guardarBocadillo() {
        val nombreBocadillo = binding.edtNombreBocadillo.text.toString().trim()
        val descripcionBocadillo = binding.edtDescripcion.text.toString().trim()
        val tipoBocadillo = binding.spinnerTipo.selectedItem.toString()

        val costeStr = binding.edtCoste.text.toString().trim()
        if (costeStr.isEmpty()) {
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val coste = costeStr.toDoubleOrNull()
        if (coste == null) {
            Toast.makeText(requireContext(), "El coste debe ser un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val alergenos = obtenerAlergenosSeleccionados()

        val nuevoBocadillo = Bocadillo(
            nombre = nombreBocadillo,
            descripcion = descripcionBocadillo,
            tipo = tipoBocadillo,
            coste = coste,
            icono = "",
            dia = "",
            alergenos = alergenos
        )

        bocadilloViewModel.insertarBocadillo(nuevoBocadillo) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Bocadillo guardado con éxito", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminAgregarBocadilloFragment_to_fragment_admin_bocadillo)
            } else {
                Toast.makeText(requireContext(), "Error al guardar el bocadillo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
