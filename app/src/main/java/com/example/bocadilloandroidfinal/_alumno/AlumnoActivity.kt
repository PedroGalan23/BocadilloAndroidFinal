package com.example.bocadilloandroidfinal._alumno

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bocadilloandroidfinal.LoginActivity
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class AlumnoActivity : AppCompatActivity() {
    private lateinit var usuarioViewModel: UsuarioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_alumno)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        usuarioViewModel = ViewModelProvider(this).get(UsuarioViewModel::class.java)

        usuarioViewModel.usuarioAutenticado.observe(this) { usuario ->
            Log.d("AlumnoActivity", "Observando cambios en usuarioAutenticado")

            if (usuario != null) {
                Log.d("AlumnoActivity", "Bienvenido ${usuario.nombre} ${usuario.apellidos}, correo: ${usuario.correo}")

                val welcome = findViewById<TextView>(R.id.tv_welcome)
                welcome.text = "Hola, ${usuario.nombre} 👋"
            } else {
                Log.e("AlumnoActivity", "No se pudo obtener el usuario")
            }
        }

        // 🔥 Configurar manualmente el clic en "Salir"
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_salir -> {
                    cerrarSesion()
                    true
                }
                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }

    // 🔥 Método para cerrar sesión correctamente
    private fun cerrarSesion() {
        usuarioViewModel.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
