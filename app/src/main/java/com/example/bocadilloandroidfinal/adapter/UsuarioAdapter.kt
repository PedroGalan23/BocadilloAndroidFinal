package com.example.bocadilloandroidfinal.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.modelos.Usuario

class UsuarioAdapter (
    private var lista_usuarios: List<Usuario>,
    private val onEditarClick: (Usuario) -> Unit,  // 🔥 Callback para editar
    private val onEliminarClick: (Usuario) -> Unit // 🔥 Callback para eliminar
): RecyclerView.Adapter<UsuarioAdapter.AlumnoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alumno, parent, false)
        Log.d("DEBUG", "Adaptador -> onCreateViewHolder() llamado")
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioAdapter.AlumnoViewHolder, position: Int) {
        val usuario=lista_usuarios[position]

        holder.txtNombre.text=usuario.nombre
        holder.txtEmail.text=usuario.correo
        holder.txtCurso.text=usuario.curso

        holder.imgEdit.setOnClickListener { onEditarClick(usuario) }
        holder.imgDelete.setOnClickListener { onEliminarClick(usuario) }

    }

    override fun getItemCount(): Int {
        Log.d("DEBUG", "Adaptador Usuarios-> getItemCount() devuelve ${lista_usuarios.size}")
        return lista_usuarios.size
    }

    class AlumnoViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtNombre: TextView=view.findViewById(R.id.txtNombreAlumno)
        val txtEmail: TextView=view.findViewById(R.id.txtEmailAlumno)
        val txtCurso: TextView=view.findViewById(R.id.txtCurso)
        val imgEdit: ImageView=view.findViewById(R.id.btnEditarAlumno)
        val imgDelete: ImageView=view.findViewById(R.id.btnEliminarAlumno)

    }

    fun actualizarLista(nuevaLista: List<Usuario>) {
        Log.d("DEBUG", "Adaptador -> actualizando lista con ${nuevaLista.size} bocadillos")
        lista_usuarios = nuevaLista
        notifyDataSetChanged() // <- Asegura que RecyclerView se actualiza
    }
}
