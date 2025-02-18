package com.example.bocadilloandroidfinal.adapter

import android.util.Log // Importar para Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.modelos.Bocadillo

class BocadilloCalendarioAdapter(
    private var bocadillos: List<Bocadillo>
) : RecyclerView.Adapter<BocadilloCalendarioAdapter.BocadilloViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bocadillo, parent, false)
        Log.d("DEBUG", "Adaptador -> onCreateViewHolder() llamado")
        return BocadilloViewHolder(view)
    }

    fun actualizarLista(nuevaLista: List<Bocadillo>) {
        Log.d("DEBUG", "Adaptador -> actualizando lista con ${nuevaLista.size} bocadillos")
        bocadillos = nuevaLista
        notifyDataSetChanged() // <- Asegura que RecyclerView se actualiza
    }
    override fun onBindViewHolder(holder: BocadilloViewHolder, position: Int) {
        val bocadillo = bocadillos[position]

        holder.txtNombre.text = bocadillo.nombre
        holder.txtDescripcion.text = bocadillo.descripcion
        holder.txtAlergenos.text = "Alergenos: ${bocadillo.nombresAlergenos?.joinToString(", ") ?: "Ninguno"}"
        holder.txtCoste.text = "Coste: ${bocadillo.coste}€"
        holder.txtDia.text = "Día asociado: ${bocadillo.dia}"
        holder.txtTipo.text = "Tipo: ${bocadillo.tipo}"
        val iconoId= holder.img.context.resources.getIdentifier(
            bocadillo.icono,"drawable",holder.itemView.context.packageName
        )
        if(iconoId!=null){
            holder.img.setImageResource(iconoId)
        }else{
            holder.img.setImageResource(R.drawable.ic_bocadillo)
        }

        Log.d("DEBUG", "Adaptador -> Bocadillo en posición $position -> ${bocadillo.nombre}")
    }

    override fun getItemCount(): Int {
        Log.d("DEBUG", "Adaptador -> getItemCount() devuelve ${bocadillos.size}")
        return bocadillos.size
    }

    class BocadilloViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre: TextView = view.findViewById(R.id.txtNombreAlumno)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val txtAlergenos: TextView = view.findViewById(R.id.txtAlergenos)
        val txtCoste: TextView = view.findViewById(R.id.txtCoste)
        val img: ImageView = view.findViewById(R.id.imgBocadillo)
        val txtDia: TextView = view.findViewById(R.id.txtDia)
        val txtTipo: TextView = view.findViewById(R.id.txtTipo)
    }
}
