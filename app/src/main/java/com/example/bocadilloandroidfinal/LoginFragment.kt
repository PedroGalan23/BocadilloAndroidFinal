package com.example.bocadilloandroidfinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("877677668660-0i8lb5l08f115q1v701sbon40t1844fb.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val tvRecuperar = view.findViewById<TextView>(R.id.tvRestablecer)
        val btnGoogle = view.findViewById<Button>(R.id.btnGoogleLogin)

        usuarioViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { usuario ->
            if (usuario != null && isAdded) {
                val rol = usuario.rol ?: "alumno"
                Toast.makeText(requireContext(), "Bienvenido, ${usuario.nombre} ($rol)", Toast.LENGTH_SHORT).show()

                when (rol.lowercase()) {
                    "alumno" -> if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_alumnoFragment)
                    }
                    "admin" -> if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_adminFragment)
                    }
                    "cocina" -> if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_cocinaFragment)
                    }
                    else -> if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_alumnoFragment)
                    }
                }
            }
        }


        usuarioViewModel.errorMensaje.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                showToast(error)
            }
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Por favor, ingrese su correo y contraseña")
                return@setOnClickListener
            }

            usuarioViewModel.signInWithEmailAndPassword(email, password)
        }

        btnGoogle.setOnClickListener {
            signInWithGoogle()
        }

        tvRecuperar.setOnClickListener {
            if (isAdded) {
                findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("LoginFragment", "Google Sign-In failed: ${e.message}")
                showToast("Error al iniciar sesión")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (isAdded) {
                        findNavController().navigate(R.id.action_loginFragment_to_alumnoFragment)
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        showToast("Este correo ya está registrado con otro método. Use su contraseña.")
                    } else {
                        showToast("Autenticación fallida")
                    }
                }
            }
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val GOOGLE_SIGN_IN = 9001
    }
}
