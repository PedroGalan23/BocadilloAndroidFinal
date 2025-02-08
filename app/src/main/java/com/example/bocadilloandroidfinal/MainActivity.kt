package com.example.bocadilloandroidfinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.bocadilloandroidfinal._admin.AdminActivity
import com.example.bocadilloandroidfinal._alumno.AlumnoActivity
import com.example.bocadilloandroidfinal._cocina.CocinaActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userRole = intent.getStringExtra("USER_ROLE") ?: "alumno"

        val intent = when (userRole.lowercase()) {
            "alumno" -> Intent(this, AlumnoActivity::class.java)
            "cocina" -> Intent(this, CocinaActivity::class.java)
            "admin" -> Intent(this, AdminActivity::class.java)
            else -> Intent(this, AlumnoActivity::class.java) // Default
        }

        startActivity(intent)
        finish() // Cierra MainActivity para que no pueda volver atrás
    }
}
