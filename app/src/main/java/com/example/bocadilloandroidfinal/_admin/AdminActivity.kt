package com.example.bocadilloandroidfinal._admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bocadilloandroidfinal.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_admin)

        // Obtener el NavHostFragment correctamente
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_admin) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)
        bottomNavigationView.setupWithNavController(navController)
    }
}