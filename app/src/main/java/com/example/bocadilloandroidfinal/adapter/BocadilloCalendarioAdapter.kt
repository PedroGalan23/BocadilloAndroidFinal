package com.example.bocadilloandroidfinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.bocadilloandroidfinal.R
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.modelos.Bocadillo

class BocadilloCalendarioAdapter(
    private val bocadillos: List<Bocadillo>,
    private val onItemClick: (Bocadillo) -> Unit // Callback para enviar el bocadillo seleccionado
) : RecyclerView.Adapter<BocadilloCalendarioAdapter.BocadilloViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION // Para rastrear la selección previa

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bocadillo, parent, false)
        return BocadilloViewHolder(view)
    }

    override fun onBindViewHolder(holder: BocadilloViewHolder, position: Int) {
        val bocadillo = bocadillos[position]

        holder.txtNombre.text = bocadillo.nombre
        holder.txtDescripcion.text = bocadillo.descripcion
        holder.txtAlergenos.text = "Alergenos: ${bocadillo.alergenos}"
        holder.txtCoste.text = "€ ${bocadillo.coste}"
        //holder.img.setImageResource(bocadillo.icono)

        // Resalta el elemento seleccionado
        holder.itemView.setBackgroundResource(
            if (position == selectedPosition) R.color.light_gray else android.R.color.white
        )

        // Manejo de clic para selección del bocadillo
        holder.itemView.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            // Notificar solo los cambios en los elementos afectados
            if (oldPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(oldPosition)
            }
            notifyItemChanged(selectedPosition)

            // Enviar el bocadillo seleccionado al fragmento
            onItemClick(bocadillo)
        }
    }

    override fun getItemCount(): Int = bocadillos.size
    //Ejemplo ViewHolder con findViewbyID
    class BocadilloViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombre)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val txtAlergenos: TextView = view.findViewById(R.id.txtAlergenos)
        val txtCoste: TextView = view.findViewById(R.id.txtCoste)
        val img: ImageView = view.findViewById(R.id.img_logo)
    }
}
