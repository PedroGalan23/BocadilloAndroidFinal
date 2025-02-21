import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bocadilloandroidfinal.R
import com.example.bocadilloandroidfinal.databinding.FragmentAdminAgregarAlumno1Binding
import com.example.bocadilloandroidfinal.modelos.Usuario
import com.example.bocadilloandroidfinal.viewmodels.UsuarioViewModel

class AdminAgregarUsuarioFragment : Fragment() {
    private lateinit var binding: FragmentAdminAgregarAlumno1Binding
    private val usuarioViewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminAgregarAlumno1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lista de roles
        val roles = listOf("Alumno", "Cocina", "Admin")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles)
        binding.spinnerRol.adapter = adapter

        // Deshabilitar botón inicialmente
        binding.btnAgregar.isEnabled = false

        binding.btnVolver.setOnClickListener {
            findNavController().navigate(R.id.action_adminAgregarUsuarioFragment_to_fragment_admin_alumno)
        }

        binding.btnAgregar.setOnClickListener {
            guardarAlumno()
        }

        // Configurar validaciones en tiempo real
        setupValidations()
    }

    private fun setupValidations() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validarFormulario()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.edtCorreo.addTextChangedListener(textWatcher)
        binding.edtPassword.addTextChangedListener(textWatcher)
        binding.edtLogin.addTextChangedListener(textWatcher)
        binding.edtNombre.addTextChangedListener(textWatcher)
    }

    private fun validarFormulario() {
        val login = binding.edtLogin.text.toString().trim()
        val nombre = binding.edtNombre.text.toString().trim()
        val correo = binding.edtCorreo.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        val emailValido = Patterns.EMAIL_ADDRESS.matcher(correo).matches()
        val passwordValida = password.length >= 6
        val camposNoVacios = login.isNotEmpty() && nombre.isNotEmpty() && correo.isNotEmpty() && password.isNotEmpty()

        // Mostrar errores en tiempo real
        if (!emailValido) {
            binding.edtCorreo.error = "Correo inválido"
        } else {
            binding.edtCorreo.error = null
        }

        if (!passwordValida) {
            binding.edtPassword.error = "Debe tener más de 6 caracteres"
        } else {
            binding.edtPassword.error = null
        }

        // Habilitar o deshabilitar el botón
        binding.btnAgregar.isEnabled = emailValido && passwordValida && camposNoVacios
    }

    private fun guardarAlumno() {
        val login = binding.edtLogin.text.toString().trim()
        val nombre = binding.edtNombre.text.toString().trim()
        val apellidos = binding.edtApellidos.text.toString().trim()
        val correo = binding.edtCorreo.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val curso = binding.edtCurso.text.toString().trim()
        val rol = binding.spinnerRol.selectedItem.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(requireContext(), "Correo inválido", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length <= 6) {
            Toast.makeText(requireContext(), "La contraseña debe tener más de 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoUsuario = Usuario(
            id = null, // 🔥 Firebase generará el ID
            login = login,
            nombre = nombre,
            apellidos = apellidos,
            correo = correo,
            password = password,
            curso = curso,
            rol = rol
        )

        //Registra alumnos en el auth
        usuarioViewModel.insertarAlumno(nuevoUsuario) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Alumno guardado con éxito", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_adminAgregarUsuarioFragment_to_fragment_admin_alumno)
                nuevoUsuario.correo?.let {
                    nuevoUsuario.password?.let { it1 ->
                        usuarioViewModel.registrarUsuario(it, it1) { success, message ->
                            if (success) {
                                Log.d("FirebaseAuth", message ?: "Registro exitoso")
                            } else {
                                Log.e("FirebaseAuth", "Error al registrar usuario: $message")
                            }
                        }
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Error al guardar el alumno", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
