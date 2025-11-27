package com.example.galletas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.galletas.api.RetrofitClient
import com.example.galletas.data.model.SignUpRequest
import com.example.galletas.data.model.XanoErrorResponse
import com.example.galletas.databinding.ActivitySignUpBinding
import com.example.galletas.util.TokenManager
import com.example.galletas.util.UserManager
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * Actividad para registrar un nuevo usuario.
 * Permite al usuario introducir su nombre, email y contraseña para crear una cuenta.
 * Realiza una validación de los campos y maneja los errores específicos de la API.
 */
class SignUpActivity : AppCompatActivity() {

    // Referencia al View Binding para acceder a las vistas del layout.
    private lateinit var binding: ActivitySignUpBinding
    // Gestor del token de autenticación.
    private lateinit var tokenManager: TokenManager
    // Gestor de los datos del usuario.
    private lateinit var userManager: UserManager
    // Instancia del servicio de autenticación de Retrofit.
    private val authService by lazy { RetrofitClient.authService }

    /**
     * Método llamado al crear la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla el layout y lo asigna a la actividad.
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el gestor de tokens y el gestor de usuario.
        tokenManager = TokenManager(this)
        userManager = UserManager(this)

        // Configura el listener para el botón de registro.
        binding.signUpButton.setOnClickListener {
            // Limpia los mensajes de error previos antes de un nuevo intento.
            binding.passwordInputLayout.error = null
            binding.emailInputLayout.error = null
            signUpUser()
        }
    }

    /**
     * Gestiona la lógica para registrar a un nuevo usuario.
     */
    private fun signUpUser() {
        // Obtiene los datos de los campos de texto.
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Valida que ningún campo esté vacío.
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Muestra el indicador de carga.
        showLoading(true)

        // Lanza una coroutina para la llamada a la API.
        lifecycleScope.launch {
            try {
                // Crea el objeto de la petición.
                val signUpRequest = SignUpRequest(name, email, password)
                // Llama al endpoint de registro.
                val authResponse = authService.signUp(signUpRequest)
                // Si el registro es exitoso, guarda el token.
                tokenManager.saveAuthToken(authResponse.token)

                // Si la respuesta incluye datos del usuario, los guardamos
                if (authResponse.user != null) {
                    // Log para debugging
                    android.util.Log.d("SignUpActivity", "Usuario recibido de Xano: ${authResponse.user}")
                    android.util.Log.d("SignUpActivity", "Rol del usuario: ${authResponse.user.role}")

                    // Si el usuario no tiene rol o es null, lo detectamos por email
                    if (authResponse.user.role.isNullOrEmpty()) {
                        val detectedRole = userManager.detectRoleFromEmail(email)
                        android.util.Log.d("SignUpActivity", "Rol detectado por email: $detectedRole")
                        // Creamos un nuevo objeto User con el rol detectado
                        val userWithRole = authResponse.user.copy(role = detectedRole)
                        userManager.saveUser(userWithRole)
                    } else {
                        userManager.saveUser(authResponse.user)
                    }
                } else {
                    // Si no vienen datos del usuario, intentamos detectar el rol por el email
                    android.util.Log.d("SignUpActivity", "No se recibió objeto user, detectando rol por email")
                    val detectedRole = userManager.detectRoleFromEmail(email)
                    android.util.Log.d("SignUpActivity", "Rol detectado: $detectedRole")
                    userManager.saveUserRole(detectedRole)
                }

                // Log final para verificar
                android.util.Log.d("SignUpActivity", "Rol guardado final: ${userManager.getUserRole()}")
                android.util.Log.d("SignUpActivity", "¿Es admin?: ${userManager.isAdmin()}")

                // Navega a la home.
                navigateToHome()
            } catch (e: Exception) {
                // Si hay un error, lo captura y lo gestiona.
                showLoading(false)
                handleSignUpError(e)
            }
        }
    }

    /**
     * Procesa los errores de la API para dar feedback específico al usuario.
     * @param e La excepción capturada durante la llamada a la red.
     */
    private fun handleSignUpError(e: Exception) {
        // Comprueba si el error es una excepción HTTP (ej: 400 Bad Request, 404 Not Found, etc).
        if (e is HttpException && e.code() == 400) {
            try {
                // Intenta leer el cuerpo del error de la respuesta JSON de Xano.
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, XanoErrorResponse::class.java)

                // Comprueba el tipo de error específico que devuelve la API.
                if (errorResponse.payload?.param == "password") {
                    // Si el error se refiere a la contraseña, muestra un mensaje en el campo correspondiente.
                    binding.passwordInputLayout.error = "La contraseña debe tener al menos 8 caracteres."
                } else if (errorResponse.message?.contains("already exists") == true) {
                    // Si el mensaje indica que el usuario ya existe, lo muestra en el campo de email.
                    binding.emailInputLayout.error = "Este email ya está registrado."
                } else {
                    // Para cualquier otro error conocido, muestra el mensaje de la API.
                    Toast.makeText(this, "Error: ${errorResponse.message}", Toast.LENGTH_LONG).show()
                }
            } catch (jsonError: Exception) {
                // Si no se puede procesar el JSON del error, muestra un mensaje genérico.
                Toast.makeText(this, "Error inesperado al procesar la respuesta.", Toast.LENGTH_LONG).show()
            }
        } else {
            // Para errores que no son HTTP (ej: no hay conexión a internet), muestra un mensaje de conexión.
            Toast.makeText(this, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Controla la visibilidad de la barra de progreso.
     * @param isLoading `true` para mostrar la carga, `false` para ocultarla.
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.signUpButton.isEnabled = !isLoading
    }

    /**
     * Navega a la HomeActivity y limpia el historial de actividades.
     */
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
