package com.example.bocadilloandroidfinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.modelos.Bocadillo

class BocadilloDiaAdapter(
    //Recibe la lista seleccionada y si hay un Bocadillo Seleccionado
    private var bocadillos: List<Bocadillo>,
    private val onItemClick: (Bocadillo) -> Unit
) : RecyclerView.Adapter<BocadilloDiaAdapter.BocadilloViewHolder>() {

    //Por defecto la posición seleccionada no es ninguna
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    //Creamos la vista a partir del item_bocadillo_dia
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bocadillo_dia, parent, false)
        return BocadilloViewHolder(view)
    }
    //Haremos un Pequeño cambio que será controlar el bocadillo seleccionado para realizar un pedido
    override fun onBindViewHolder(holder: BocadilloViewHolder, position: Int) {
        val bocadillo = bocadillos[position]
        //LLamaremos a la función bindd
        holder.bind(bocadillo, position)
    }

    override fun getItemCount(): Int = bocadillos.size

    fun actualizarLista(nuevaLista: List<Bocadillo>) {
        println("DEBUG: Adaptador recibe -> ${nuevaLista.size} bocadillos")
        bocadillos = nuevaLista
        notifyDataSetChanged()
    }
    //Lo primero que haremos será recoger las referencias del item
    inner class BocadilloViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombreBocadillo: TextView = itemView.findViewById(R.id.tvNombreBocadillo)
        private val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        private val tvAlergenos: TextView = itemView.findViewById(R.id.tvAlergenos)
        private val tvCoste: TextView = itemView.findViewById(R.id.tvCoste)
        private val tvDia: TextView = itemView.findViewById(R.id.tvDia)
        private val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        private val ivBocadillo: ImageView = itemView.findViewById(R.id.ivBocadillo)

        //La función bind asigna los valores y permite la selección del bocadillo
        fun bind(bocadillo: Bocadillo, position: Int) {
            println("DEBUG: Mostrando bocadillo -> ${bocadillo.nombre}")

            tvNombreBocadillo.text = bocadillo.nombre
            tvDescripcion.text = bocadillo.descripcion
            tvAlergenos.text = "Alergenos: ${bocadillo.nombresAlergenos?.joinToString(", ") ?: "Ninguno"}"
            tvCoste.text = "€ ${bocadillo.coste}"
            tvDia.text = bocadillo.dia
            tvTipo.text = bocadillo.tipo

            val context = itemView.context
            val iconoResId = context.resources.getIdentifier(
                bocadillo.icono, "drawable", context.packageName
            )
            if (iconoResId != 0) {
                ivBocadillo.setImageResource(iconoResId)
            } else {
                ivBocadillo.setImageResource(R.drawable.ic_bocadillo)
            }

            //Cuando la posicion y la posicion seleccionada coinciden cambiamos el color del item a grey
            itemView.setBackgroundResource(
                if (position == selectedPosition) R.color.light_gray else android.R.color.white
            )
            //Permitimos el cambio de selección con el setOnCLick en todo el item View
            itemView.setOnClickListener {
                val oldPosition = selectedPosition
                selectedPosition = adapterPosition
                if (oldPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(oldPosition)
                }
                notifyItemChanged(selectedPosition)

                onItemClick(bocadillo)
            }
        }
    }
}
