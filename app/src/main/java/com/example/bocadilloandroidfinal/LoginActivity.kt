package com.example.bocadilloandroidfinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel

class LoginActivity : AppCompatActivity() {
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        // 🔥 Observar usuario autenticado
        usuarioViewModel.usuarioAutenticado.observe(this) { usuario ->
            if (usuario != null) {
                val rol = usuario.rol ?: "alumno" // Si el rol no está definido, usa "alumno"
                Toast.makeText(this, "Bienvenido, ${usuario.nombre} ($rol)", Toast.LENGTH_SHORT).show()

                // Redirige a MainActivity con el rol del usuario
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER_ROLE", rol)
                startActivity(intent)
                finish()
            }
        }

        // 🔥 Manejo de errores
        usuarioViewModel.errorMensaje.observe(this) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // Validar campos antes de iniciar sesión
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese su correo y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            usuarioViewModel.signInWithEmailAndPassword(email, password)
        }
    }
}
