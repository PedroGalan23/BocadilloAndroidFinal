package com.example.bocadilloandroidfinal._admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bocadilloandroidfinal.LoginActivity
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

import androidx.activity.viewModels

class AdminActivity : AppCompatActivity() {
    private val usuarioViewModel: UsuarioViewModel by viewModels() // Inicialización segura con ViewModelProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_admin)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_admin) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)
        bottomNavigationView.setupWithNavController(navController)

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

    private fun cerrarSesion() {
        usuarioViewModel.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
