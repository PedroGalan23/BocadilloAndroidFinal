package com.example.bocadilloandroidfinal._cocina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bocadilloandroidfinal.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class CocinaActivity :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState )
        setContentView(R.layout.dashboard_cocina)

        //Obtener el NavHost
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.nav_host_fragment_cocina) as NavHostFragment
        val navController=navHostFragment.navController

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationViewcocina)
        bottomNavigationView.setupWithNavController(navController)
    }
}