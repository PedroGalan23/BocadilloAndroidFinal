package com.example.bocadilloandroidfinal._admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bocadilloandroidfinal.MainActivity
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class AdminFragment : Fragment() {
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dashboard_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationViewAdmin)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment_admin) as? NavHostFragment
        val navController = navHostFragment?.navController

        if (navController != null) {
            bottomNavigationView.setupWithNavController(navController)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.menu_salir_cocina_admin) {
                cerrarSesion()
                true
            } else {
                navController?.navigate(item.itemId)
                true
            }
        }
    }
/*
    private fun cerrarSesion() {
        usuarioViewModel.signOut()
        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, com.example.bocadilloandroidfinal.LoginFragment())
            .commit()
    }
    */
private fun cerrarSesion() {
    usuarioViewModel.signOut()
    FirebaseAuth.getInstance().signOut()
    Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()

    // Reiniciar la actividad con el nav_main
    val intent = Intent(requireContext(), MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    requireActivity().finish()
}

}
