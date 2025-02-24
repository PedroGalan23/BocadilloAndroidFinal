package com.example.bocadilloandroidfinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.modelos.Pedido
import java.text.SimpleDateFormat
import java.util.*
//Recibe la lista de pedidos y el Método de OnPedidoSeleccionado
class PedidoCocinaAdapter(
    private var pedidos: List<Pedido>,
    //Desde su llamada será adjuntado con un lamda lo que pasará cuando se seleccione
    private val onPedidoSeleccionado: (Pedido) -> Unit
) : RecyclerView.Adapter<PedidoCocinaAdapter.PedidoCocinaViewHolder>() {

    //Actualizamos la Lista si Ocurre algún cambio en FireBase
    fun actualizarLista(nuevaLista: List<Pedido>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PedidoCocinaViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.tvNombreBocadillo.text = pedido.bocadillo.nombre

        val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
        holder.tvFecha.text = sdf.format(pedido.fecha_hora.toDate())

        holder.tvEstado.text = if (pedido.estado) "Retirado" else "No Retirado"
        holder.tvEstado.setTextColor(
            if (pedido.estado) holder.itemView.context.getColor(R.color.verde)
            else holder.itemView.context.getColor(R.color.rojo_claro)
        )

        holder.tvCoste.text = "%.2f€".format(pedido.precio)

        val iconoId = holder.img.context.resources.getIdentifier(
            pedido.bocadillo.icono, "drawable", holder.itemView.context.packageName
        )
        holder.img.setImageResource(if (iconoId != 0) iconoId else R.drawable.ic_bocadillo)

        holder.tvNombreAlumno.text = pedido.id_usuario

        holder.itemView.setOnClickListener {
            onPedidoSeleccionado(pedido)
        }
    }
    //Recuento de los elementos
    override fun getItemCount(): Int = pedidos.size

    //Creamos la vista a partir del item pedido_cocina
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoCocinaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido_cocina, parent, false)
        return PedidoCocinaViewHolder(view)
    }
    //Recuperamos los valores de la vista con el viewHolder
    class PedidoCocinaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreBocadillo: TextView = view.findViewById(R.id.tvNombreBocadillo)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvCoste: TextView = view.findViewById(R.id.tvCoste)
        val img: ImageView = view.findViewById(R.id.imageView)
        val tvNombreAlumno: TextView = view.findViewById(R.id.tvNombreAlumno)
    }
}
