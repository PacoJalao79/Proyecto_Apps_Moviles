package com.example.galletas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.galletas.api.RetrofitClient
import com.example.galletas.data.model.LoginRequest
import com.example.galletas.databinding.ActivityMainBinding
import com.example.galletas.util.TokenManager
import com.example.galletas.util.UserManager
import kotlinx.coroutines.launch

/**
 * Actividad principal que funciona como la pantalla de inicio de sesión (Login).
 * Es la primera pantalla que ve el usuario.
 * Desde aquí, el usuario puede iniciar sesión, crear una nueva cuenta o entrar como invitado.
 */
class MainActivity : AppCompatActivity() {

    // Referencia al View Binding para acceder a las vistas del layout de forma segura.
    private lateinit var binding: ActivityMainBinding
    // Gestor para guardar y recuperar el token de autenticación localmente.
    private lateinit var tokenManager: TokenManager
    // Gestor para guardar y recuperar los datos del usuario.
    private lateinit var userManager: UserManager
    // Instancia del servicio de autenticación de Retrofit para hacer llamadas a la API.
    private val authService by lazy { RetrofitClient.authService }

    /**
     * Método que se llama cuando la actividad es creada por primera vez.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla (crea) el layout y lo asigna a la actividad.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa el gestor de tokens y el gestor de usuario.
        tokenManager = TokenManager(this)
        userManager = UserManager(this)

        // Configura el listener para el botón de "Iniciar Sesión".
        binding.loginButton.setOnClickListener {
            loginUser()
        }

        // Configura el listener para el botón de "Crear Cuenta".
        binding.createAccountButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Configura el listener para el botón de "Modo Invitado".
        binding.guestModeButton.setOnClickListener {
            // Borra cualquier token y datos de usuario de una sesión anterior para asegurar que entramos como invitado.
            tokenManager.clearToken()
            userManager.clearUser()
            // Navega a la pantalla principal de la tienda.
            navigateToHome()
        }
    }

    /**
     * Gestiona la lógica para el inicio de sesión del usuario.
     */
    private fun loginUser() {
        // Obtiene el email y la contraseña de los campos de texto, eliminando espacios en blanco.
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Valida que los campos no estén vacíos.
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return // Detiene la ejecución si los campos están vacíos.
        }

        // Muestra un indicador de carga para dar feedback visual al usuario.
        showLoading(true)

        // Lanza una coroutina en el hilo principal para realizar la llamada a la red de forma asíncrona.
        lifecycleScope.launch {
            try {
                // Crea el objeto de la petición con los datos del usuario.
                val loginRequest = LoginRequest(email, password)
                // Llama al endpoint de login de la API.
                val authResponse = authService.login(loginRequest)
                // Si la llamada es exitosa, guarda el token recibido.
                tokenManager.saveAuthToken(authResponse.token)

                // Si la respuesta incluye datos del usuario, los guardamos
                if (authResponse.user != null) {
                    // Log para debugging
                    android.util.Log.d("MainActivity", "=== LOGIN EXITOSO ===")
                    android.util.Log.d("MainActivity", "Usuario recibido del login: ${authResponse.user}")
                    android.util.Log.d("MainActivity", "ID: ${authResponse.user.id}")
                    android.util.Log.d("MainActivity", "Name: ${authResponse.user.name}")
                    android.util.Log.d("MainActivity", "Email: ${authResponse.user.email}")
                    android.util.Log.d("MainActivity", "Rol del usuario: ${authResponse.user.role}")

                    // Si el usuario no tiene rol o es null, lo detectamos por email
                    val finalUser = if (authResponse.user.role.isNullOrEmpty()) {
                        val detectedRole = userManager.detectRoleFromEmail(email)
                        android.util.Log.d("MainActivity", "⚠️ Rol detectado por email: $detectedRole")
                        authResponse.user.copy(role = detectedRole)
                    } else {
                        android.util.Log.d("MainActivity", "✅ Rol recibido del servidor: ${authResponse.user.role}")
                        authResponse.user
                    }

                    // Guardamos el usuario con su ID
                    android.util.Log.d("MainActivity", "💾 Guardando usuario básico...")
                    userManager.saveUser(finalUser)
                    android.util.Log.d("MainActivity", "✅ Usuario básico guardado")

                    // NUEVO: Intentamos cargar el perfil completo desde la API E-commerce
                    // para obtener TODOS los campos (firstName, lastName, phone, etc.)
                    try {
                        android.util.Log.d("MainActivity", "📡 Cargando perfil completo del usuario ID: ${finalUser.id}")
                        val fullProfile = RetrofitClient.storeUserService.getUserById(finalUser.id)
                        android.util.Log.d("MainActivity", "✅ Perfil completo cargado: $fullProfile")
                        android.util.Log.d("MainActivity", "Rol en perfil completo: ${fullProfile.role}")

                        // Preservamos el rol que ya teníamos
                        val completeUser = if (fullProfile.role.isNullOrEmpty()) {
                            android.util.Log.d("MainActivity", "⚠️ Perfil completo sin rol, preservando: ${finalUser.role}")
                            fullProfile.copy(role = finalUser.role)
                        } else {
                            android.util.Log.d("MainActivity", "✅ Usando rol del perfil completo: ${fullProfile.role}")
                            fullProfile
                        }

                        // Guardamos el perfil completo
                        android.util.Log.d("MainActivity", "💾 Guardando perfil completo...")
                        userManager.saveUser(completeUser)
                        android.util.Log.d("MainActivity", "✅ Perfil completo guardado con todos los datos")
                        android.util.Log.d("MainActivity", "Datos finales: ID=${completeUser.id}, name=${completeUser.name}, role=${completeUser.role}")
                    } catch (e: Exception) {
                        android.util.Log.e("MainActivity", "❌ Error al cargar perfil completo: ${e.message}", e)
                        android.util.Log.d("MainActivity", "⚠️ Continuando con datos básicos del login")
                        // Si falla, seguimos con los datos básicos del login (ya guardados arriba)
                    }
                } else {
                    // Si no vienen datos del usuario, intentamos obtenerlos de /auth/me
                    android.util.Log.d("MainActivity", "⚠️ No se recibió objeto user en authResponse")
                    android.util.Log.d("MainActivity", "🔧 Intentando obtener usuario desde /auth/me")

                    try {
                        // Intentar obtener el usuario actual desde el endpoint /auth/me
                        val currentUser = RetrofitClient.authUserService.getCurrentUser()
                        android.util.Log.d("MainActivity", "✅ Usuario obtenido de /auth/me: $currentUser")

                        val detectedRole = userManager.detectRoleFromEmail(email)

                        // Si el usuario no tiene rol, lo detectamos por email
                        val finalUser = if (currentUser.role.isNullOrEmpty()) {
                            android.util.Log.d("MainActivity", "⚠️ Rol detectado por email: $detectedRole")
                            currentUser.copy(role = detectedRole)
                        } else {
                            android.util.Log.d("MainActivity", "✅ Rol recibido de /auth/me: ${currentUser.role}")
                            currentUser
                        }

                        // Guardar usuario
                        android.util.Log.d("MainActivity", "💾 Guardando usuario desde /auth/me...")
                        userManager.saveUser(finalUser)
                        android.util.Log.d("MainActivity", "✅ Usuario guardado: ID=${finalUser.id}, name=${finalUser.name}")

                        // Intentar cargar perfil completo desde E-commerce API
                        if (finalUser.id != -1) {
                            try {
                                android.util.Log.d("MainActivity", "📡 Intentando cargar perfil completo desde /user/${finalUser.id}")
                                val fullProfile = RetrofitClient.storeUserService.getUserById(finalUser.id)
                                val completeUser = fullProfile.copy(role = finalUser.role)
                                userManager.saveUser(completeUser)
                                android.util.Log.d("MainActivity", "✅ Perfil completo guardado")
                            } catch (e: Exception) {
                                android.util.Log.e("MainActivity", "⚠️ No se pudo cargar perfil completo: ${e.message}")
                            }
                        }

                    } catch (e: Exception) {
                        android.util.Log.e("MainActivity", "❌ Error al obtener usuario de /auth/me: ${e.message}")

                        // Como último recurso, crear usuario temporal
                        val detectedRole = userManager.detectRoleFromEmail(email)
                        android.util.Log.d("MainActivity", "🔧 Creando usuario temporal con rol: $detectedRole")

                        val tempUser = com.example.galletas.data.model.User(
                            id = if (detectedRole == "admin") 1 else -1,
                            name = email.substringBefore("@"),
                            email = email,
                            role = detectedRole
                        )

                        userManager.saveUser(tempUser)
                        android.util.Log.d("MainActivity", "✅ Usuario temporal guardado")
                    }
                }

                // Log final para verificar
                android.util.Log.d("MainActivity", "=== VERIFICACIÓN FINAL ===")
                android.util.Log.d("MainActivity", "User ID guardado: ${userManager.getUserId()}")
                android.util.Log.d("MainActivity", "User name guardado: ${userManager.getUserName()}")
                android.util.Log.d("MainActivity", "User email guardado: ${userManager.getUserEmail()}")
                android.util.Log.d("MainActivity", "Rol guardado final: ${userManager.getUserRole()}")
                android.util.Log.d("MainActivity", "¿Es admin?: ${userManager.isAdmin()}")
                android.util.Log.d("MainActivity", "Usuario completo en caché: ${userManager.getUser()}")

                // Navega a la pantalla principal de la aplicación.
                navigateToHome()
            } catch (e: retrofit2.HttpException) {
                // Manejo específico de errores HTTP
                showLoading(false)
                when (e.code()) {
                    403 -> {
                        // Error 403: Credenciales inválidas
                        Toast.makeText(
                            this@MainActivity,
                            "❌ Credenciales incorrectas\n\nVerifica tu email y contraseña.\nSi no tienes cuenta, créala primero.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    401 -> {
                        // Error 401: No autorizado
                        Toast.makeText(
                            this@MainActivity,
                            "❌ Email o contraseña incorrectos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    404 -> {
                        // Error 404: Usuario no encontrado
                        Toast.makeText(
                            this@MainActivity,
                            "❌ Usuario no encontrado\n\n¿No tienes cuenta? Crea una nueva.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        // Otros errores HTTP
                        Toast.makeText(
                            this@MainActivity,
                            "Error del servidor (${e.code()}): ${e.message()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                // Si ocurre un error (ej: sin internet, timeout), lo captura.
                showLoading(false) // Oculta el indicador de carga.
                // Muestra un mensaje de error al usuario.
                Toast.makeText(
                    this@MainActivity,
                    "Error de conexión: ${e.message}\n\nVerifica tu conexión a internet.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Controla la visibilidad de la barra de progreso y habilita/deshabilita los botones.
     * @param isLoading `true` para mostrar la carga, `false` para ocultarla.
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !isLoading
        binding.createAccountButton.isEnabled = !isLoading
        binding.guestModeButton.isEnabled = !isLoading
    }

    /**
     * Navega a la HomeActivity, que es la pantalla principal de la tienda.
     * Limpia el historial de actividades para que el usuario no pueda volver a la pantalla de login
     * con el botón de "atrás".
     */
    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Finaliza la MainActivity.
    }
}
