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

class PedidoAdapter(
    private var pedidos: List<Pedido>
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    //Creamos la vista correspondiente a partir del item_pedido
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    //Actualizamos la lista si sucede algún cambio y notificamos para que RecyclerView refresque
    fun actualizarLista(nuevaLista: List<Pedido>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]

        holder.tvNombreBocadillo.text = pedido.bocadillo.nombre

        // Formatear fecha para que MariCarmen no se queje
        val sdf = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
        holder.tvFecha.text = sdf.format(pedido.fecha_hora.toDate())

        holder.tvEstado.text = if (pedido.estado) "Retirado" else "No Retirado"
        holder.tvEstado.setTextColor(
            if (pedido.estado) holder.itemView.context.getColor(R.color.verde)
            else holder.itemView.context.getColor(R.color.rojo_claro)
        )

        holder.tvCoste.text = "%.2f€".format(pedido.precio)

        // Cargar icono de bocadillo
        val iconoId = holder.img.context.resources.getIdentifier(
            pedido.bocadillo.icono, "drawable", holder.itemView.context.packageName
        )
        holder.img.setImageResource(if (iconoId != 0) iconoId else R.drawable.ic_bocadillo)
    }

    override fun getItemCount(): Int = pedidos.size
    //Recuperamos las referencias de la vista con el ViewHolder
    class PedidoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreBocadillo: TextView = view.findViewById(R.id.tvNombreBocadillo)
        val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        val tvCoste: TextView = view.findViewById(R.id.tvCoste)
        val img: ImageView = view.findViewById(R.id.imageView)
    }
}
