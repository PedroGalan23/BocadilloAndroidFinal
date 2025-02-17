package com.example.bocadilloandroidfinal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.databinding.ItemBocadilloDiaBinding
import com.example.bocadilloandroidfinal.modelos.Bocadillo

class BocadilloDiaAdapter(
    private var bocadillos: List<Bocadillo>,
    private val onItemClick: (Bocadillo) -> Unit
) : RecyclerView.Adapter<BocadilloDiaAdapter.BocadilloViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val binding = ItemBocadilloDiaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BocadilloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BocadilloViewHolder, position: Int) {
        val bocadillo = bocadillos[position]
        holder.bind(bocadillo, position)
    }

    override fun getItemCount(): Int = bocadillos.size

    fun actualizarLista(nuevaLista: List<Bocadillo>) {
        println("DEBUG: Adaptador recibe -> ${nuevaLista.size} bocadillos") // 👀 Ver en Logcat
        bocadillos = nuevaLista
        notifyDataSetChanged()
    }


    inner class BocadilloViewHolder(private val binding: ItemBocadilloDiaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bocadillo: Bocadillo, position: Int) {
            println("DEBUG: Mostrando bocadillo -> ${bocadillo.nombre}") // 👀 Ver en Logcat
            bocadillo.alergenos
            binding.tvNombreBocadillo.text = bocadillo.nombre
            binding.tvDescripcion.text = bocadillo.descripcion
            binding.tvAlergenos.text = "Alergenos: ${bocadillo.nombresAlergenos?.joinToString(", ") ?: "Ninguno"}"
            binding.tvCoste.text = "€ ${bocadillo.coste}"
            binding.tvDia.text = bocadillo.dia
            binding.tvTipo.text = bocadillo.tipo

            // Cargar el icono correctamente desde drawable
            val context = binding.root.context
            val iconoResId = context.resources.getIdentifier(
                bocadillo.icono, "drawable", context.packageName
            )
            if (iconoResId != 0) {
                binding.ivBocadillo.setImageResource(iconoResId)
            } else {
                binding.ivBocadillo.setImageResource(R.drawable.ic_bocadillo) // Icono por defecto si no encuentra el recurso
            }

            // Resaltar el bocadillo seleccionado
            binding.root.setBackgroundResource(
                if (position == selectedPosition) R.color.light_gray else android.R.color.white
            )

            // Manejo de clic para selección del bocadillo
            binding.root.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = adapterPosition
                if (oldPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(oldPosition)
                }
                notifyItemChanged(selectedPosition)

                // Enviar el bocadillo seleccionado al fragmento
                onItemClick(bocadillo)
            }
        }
    }
}
