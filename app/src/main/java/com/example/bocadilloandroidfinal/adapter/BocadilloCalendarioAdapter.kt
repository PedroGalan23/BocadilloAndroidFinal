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
    //Bocadillos adapter recibirá una lista de bocadillos
    private var bocadillos: List<Bocadillo>
) : RecyclerView.Adapter<BocadilloCalendarioAdapter.BocadilloViewHolder>() {

    //Creamos la vista del adapter que será el item_bocadillo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bocadillo, parent, false)
        Log.d("DEBUG", "Adaptador -> onCreateViewHolder() llamado")
        return BocadilloViewHolder(view)
    }

    //Actualizamos la Lista cuando hacemos cambios en Firebase, le pasamos la nueva Lista y volvemos a llamar
    fun actualizarLista(nuevaLista: List<Bocadillo>) {
        Log.d("DEBUG", "Adaptador -> actualizando lista con ${nuevaLista.size} bocadillos")
        bocadillos = nuevaLista
        notifyDataSetChanged() //  Asegura que RecyclerView se actualiza
    }
    override fun onBindViewHolder(holder: BocadilloViewHolder, position: Int) {
        val bocadillo = bocadillos[position]

        holder.txtNombre.text = bocadillo.nombre
        holder.txtDescripcion.text = bocadillo.descripcion
        //Hacemos un join de los Alergenos separando por comas
        holder.txtAlergenos.text = "Alergenos: ${bocadillo.nombresAlergenos?.joinToString(", ") ?: "Ninguno"}"
        holder.txtCoste.text = "Coste: ${bocadillo.coste}€"
        holder.txtDia.text = "Día asociado: ${bocadillo.dia}"
        holder.txtTipo.text = "Tipo: ${bocadillo.tipo}"
        val iconoId= holder.img.context.resources.getIdentifier(
            bocadillo.icono,"drawable",holder.itemView.context.packageName
        )
        //asignamos la img con el nombre del archivo guardado en Firebase EJ ic_pavo
        if(iconoId!=null){
            holder.img.setImageResource(iconoId)
        }else{
            holder.img.setImageResource(R.drawable.ic_bocadillo)
        }

        Log.d("DEBUG", "Adaptador -> Bocadillo en posición $position -> ${bocadillo.nombre}")
    }

    //Obtiene el total de resultados
    override fun getItemCount(): Int {
        Log.d("DEBUG", "Adaptador -> getItemCount() devuelve ${bocadillos.size}")
        return bocadillos.size
    }

    //Recogemos todas las referencias al item desde el ViewHolder
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
