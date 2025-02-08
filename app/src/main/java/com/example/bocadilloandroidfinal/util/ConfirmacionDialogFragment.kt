package com.example.bocadilloandroidfinal.util

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmacionDialogFragment(private val onConfirm: () -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de que quieres continuar?")
            .setPositiveButton("Sí") { _, _ ->
                onConfirm() // Ejecuta la acción al confirmar
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}
