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
    private val onEditarClick: (Bocadillo) -> Unit,  // 🔥 Callback para editar
    private val onEliminarClick: (Bocadillo) -> Unit // 🔥 Callback para eliminar
): RecyclerView.Adapter<BocadilloCrudAdapter.BocadilloViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BocadilloViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bocadillo_crud, parent, false)
        Log.d("DEBUG", "Adaptador -> onCreateViewHolder() llamado")
        return BocadilloViewHolder(view)
    }


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


        // holder.imgEdit.setOnClickListener { onEditarClick(bocadillo) }
        //holder.imgDelete.setOnClickListener { onEliminarClick(bocadillo) }

    }
    fun actualizarLista(nuevaLista: List<Bocadillo>) {
        Log.d("DEBUG", "Adaptador -> actualizando lista con ${nuevaLista.size} bocadillos")
        lista_bocadillos = nuevaLista
        notifyDataSetChanged() // <- Asegura que RecyclerView se actualiza
    }

    class BocadilloViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imgBocadillo: ImageView = view.findViewById(R.id.imgBocadilloCrud)
        val txtNombreBocadillo: TextView = view.findViewById(R.id.txtNombreBocadilloCrud)
        val txtDia: TextView = view.findViewById(R.id.txtDiaCrud)
        val txtAlergenos: TextView = view.findViewById(R.id.txtAlergenosCrud)
        val txtCoste: TextView = view.findViewById(R.id.txtCosteCrud)
        val txtTipo: TextView = view.findViewById(R.id.txtTipoCrud) // 🔥 SE AGREGÓ ESTE CAMPO
        val imgEdit: ImageView = view.findViewById(R.id.btnEditarBocadiloCrud)
        val imgDelete: ImageView = view.findViewById(R.id.btnEliminarBocadiloCrud)
    }

}



