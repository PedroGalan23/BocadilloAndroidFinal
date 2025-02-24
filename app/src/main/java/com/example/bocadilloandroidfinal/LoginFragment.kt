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

//Login fragment es el punto de partida de nuestro navigation_main siendo crucial en la autentificación del usuario
class LoginFragment : Fragment() {

    //Declaramos las variables necesarias para la autentificación
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflamos la vista
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Obtenemos una instancia de FireBaseAuth
        auth = FirebaseAuth.getInstance()

        //Creamos una instancia del constructor GoogleSignInOptions
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //Opciones predeterminadas
            .requestIdToken("877677668660-0i8lb5l08f115q1v701sbon40t1844fb.apps.googleusercontent.com")//Solicitamos el id al token de nuestro Firebase Google Auth
            .requestEmail() //Solicitamos el acceso a la dirección de correo electrónico del usuario para obtener su perfil básico
            .build()//Construye la configuración completa
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso) //Obtiene una instancia que permitirá manejar el inicio de sesion

        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val tvRecuperar = view.findViewById<TextView>(R.id.tvRestablecer)
        val btnGoogle = view.findViewById<Button>(R.id.btnGoogleLogin)

        //Se observa el LiveData para comprobar si alumno esta auntenticado
        usuarioViewModel.usuarioAutenticado.observe(viewLifecycleOwner) { usuario ->
            /*isAdded es una manera de comprobar que el fragmento que vamos a cargar esta dentro de la actividad con motivos de nada falle
            usuario!=null dice si el usuario esta autenticado o no
             */
            if (usuario != null && isAdded) {
                //Sino tiene rol el usuario por defecto será un alumno
                val rol = usuario.rol ?: "alumno"
                //Comprobamos mediante toast
                Toast.makeText(requireContext(), "Bienvenido, ${usuario.nombre} ($rol)", Toast.LENGTH_SHORT).show()
                //Hacemos una comparación por rol, parecido a un switch pero en kotlin
                when (rol.lowercase()) {
                    //En todos los casos comprobamos primero si existe el fragmento actual para comprobar el contexto
                    "alumno" -> if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_alumnoFragment)
                    }
                    "admin" -> if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_adminFragment)
                    }
                    "cocina" -> if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_cocinaFragment)
                    }
                    //Si el rol no coincide vamos directamente a alumno
                    else -> if (findNavController().currentDestination?.id == R.id.loginFragment) {
                        findNavController().navigate(R.id.action_loginFragment_to_alumnoFragment)
                    }
                }
            }
        }

        //Si ha ocurrido algun error : Credenciales incorrectas, no existe el usuario... se mostrará un toast
        usuarioViewModel.errorMensaje.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                showToast(error)
            }
        }

        //Eliminamos los espacios para comprobar que el email no está vacio
        btnLogin.setOnClickListener {
            //pasamos los valores a una bariable
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            //El return nos saca del método setOnClick listener si la comprobación es verdadera
            if (email.isEmpty() || password.isEmpty()) {
                showToast("Por favor, ingrese su correo y contraseña")
                return@setOnClickListener
            }
            //Iniciamos sesión desde el usuarioviewmodel
            usuarioViewModel.signInWithEmailAndPassword(email, password)
        }
        //Iniciamos sesión desde el andorid llamando al función arriba definida
        btnGoogle.setOnClickListener {
            signInWithGoogle()
        }

        //Navegamos hacia resetPassword para resetear la contraseña
        tvRecuperar.setOnClickListener {
            //isAdded se utiliza para verificar si el fragmento actual existe en el Manifest y así evitar errores
            if (isAdded) {
                findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
            }
        }
    }


    private fun signInWithGoogle() {
        //Creamos un intento de inicio de sesión con google
        val signInIntent = googleSignInClient.signInIntent
        //Inicia la actividad esperando un resultado
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    /*
    Si el resultado corresponde a el código Google_Sign_In se obtiene la cuenta de google desde el INtent
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!//Se intenta recuperar el id de la cuenta
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("LoginFragment", "Google Sign-In failed: ${e.message}")
                showToast("Error al iniciar sesión")
            }
        }
    }

    //Autentificación de firebase con google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null) //Se crea una credencial de autentificación con google
        auth.signInWithCredential(credential) //Iniciamos sesión con la credencial
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (isAdded) {
                        //Si tiene exito navegamos hacia alumnoFragment
                        findNavController().navigate(R.id.action_loginFragment_to_alumnoFragment)
                    }
                } else {
                    val exception = task.exception
                    //Si ya esta registrado con otro método
                    if (exception is FirebaseAuthUserCollisionException) {
                        showToast("Este correo ya está registrado con otro método. Use su contraseña.")
                    } else {
                        showToast("Autenticación fallida")
                    }
                }
            }
    }

    //Metodo utilizado para reutilizar toasts
    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
    /*
        Define una constante llamada GOOGLE_SIGN_IN con el valor 9001,
        que se utiliza como código para identificar el resultado del inicio de sesión con Google.
    */
    companion object {
        private const val GOOGLE_SIGN_IN = 9001
    }
}
