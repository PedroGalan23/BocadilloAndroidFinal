package com.example.bocadilloandroidfinal.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.adapter.UsuarioAdapter.AlumnoViewHolder
import com.example.bocadilloandroidfinal.modelos.Bocadillo
import com.example.bocadilloandroidfinal.modelos.Usuario

class BocadilloCrudAdapter (
    //Valores que recibe el constructor
    private var lista_bocadillos: List<Bocadillo>,
    //Desde aqí asignamos la llamada al CRUD pasando el Obtejo bocadillo a Serializar
    private val onEditarClick: (Bocadillo) -> Unit,  // Callback para editar
    private val onEliminarClick: (Bocadillo) -> Unit // Callback para eliminar
): RecyclerView.Adapter<BocadilloCrudAdapter.BocadilloViewHolder>(){

    //Creamos la vista para cada item del RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bocadillo_crud, parent, false)
        Log.d("DEBUG", "Adaptador -> onCreateViewHolder() llamado")
        return BocadilloViewHolder(view)
    }

    //Obtenemos una cuenta de todos los elementos obtenidos
    override fun getItemCount(): Int {
        Log.d("DEBUG", "Adaptador Usuarios-> getItemCount() devuelve ${lista_bocadillos.size}")
        return lista_bocadillos.size
    }

    override fun onBindViewHolder(holder: BocadilloCrudAdapter.BocadilloViewHolder, position: Int) {
        val bocadillo=lista_bocadillos[position]

        val iconoId= holder.imgBocadillo.context.resources.getIdentifier(
            bocadillo.icono,"drawable",holder.itemView.context.packageName
        )
        if(iconoId!=null){
            holder.imgBocadillo.setImageResource(iconoId)
        }else{
            holder.imgBocadillo.setImageResource(R.drawable.ic_bocadillo)
        }
        holder.txtNombreBocadillo.text=bocadillo.nombre
        holder.txtDia.text=bocadillo.dia
        holder.txtAlergenos.text = "Alergenos: ${bocadillo.nombresAlergenos?.joinToString(", ") ?: "Ninguno"}"
        holder.txtCoste.text = "Coste: ${bocadillo.coste}€"
        holder.txtTipo.text = "Tipo: ${bocadillo.tipo}"
        holder.tvID.text=bocadillo.id

        //Mediante el holder llamamos al metodo de editar y le pasamos el Objeto bocadillo
        holder.imgEdit.setOnClickListener {
            onEditarClick(bocadillo)
        }
        //Mediante el holder llamamos al método editar y le pasamos el obtejo bocadillo
        holder.imgDelete.setOnClickListener {
            onEliminarClick(bocadillo)
        }

        // holder.imgEdit.setOnClickListener { onEditarClick(bocadillo) }
        //holder.imgDelete.setOnClickListener { onEliminarClick(bocadillo) }

    }
    //Usamos actualizar si hay algun cambio en FireBase
    fun actualizarLista(nuevaLista: List<Bocadillo>) {
        Log.d("DEBUG", "Adaptador -> actualizando lista con ${nuevaLista.size} bocadillos")
        lista_bocadillos = nuevaLista
        notifyDataSetChanged() // Asegura que RecyclerView se actualiza
    }
    //Vinculación de las vistas con el Holder
    class BocadilloViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imgBocadillo: ImageView = view.findViewById(R.id.imgBocadilloCrud)
        val txtNombreBocadillo: TextView = view.findViewById(R.id.txtNombreBocadilloCrud)
        val txtDia: TextView = view.findViewById(R.id.txtDiaCrud)
        val txtAlergenos: TextView = view.findViewById(R.id.txtAlergenosCrud)
        val txtCoste: TextView = view.findViewById(R.id.txtCosteCrud)
        val txtTipo: TextView = view.findViewById(R.id.txtTipoCrud) // 🔥 SE AGREGÓ ESTE CAMPO
        val imgEdit: ImageView = view.findViewById(R.id.btnEditarBocadiloCrud)
        val imgDelete: ImageView = view.findViewById(R.id.btnEliminarBocadiloCrud)
        val tvID:TextView=view.findViewById(R.id.tvIdBocadilloCrud)
    }

}



