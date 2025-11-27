package com.example.galletas.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.galletas.MainActivity
import com.example.galletas.SignUpActivity
import com.example.galletas.api.RetrofitClient
import com.example.galletas.data.model.User
import com.example.galletas.databinding.FragmentProfileBinding
import com.example.galletas.util.TokenManager
import com.example.galletas.util.UserManager
import kotlinx.coroutines.launch

/**
 * Fragmento para mostrar el perfil del usuario y gestionar la sesión.
 * La vista de este fragmento cambia dependiendo de si el usuario ha iniciado sesión
 * o si está navegando como invitado.
 */
class ProfileFragment : Fragment() {

    // View Binding para acceder a las vistas del layout.
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    // Gestor para acceder al token de autenticación.
    private lateinit var tokenManager: TokenManager
    // Gestor para acceder a los datos del usuario.
    private lateinit var userManager: UserManager
    // Servicio para obtener datos del usuario desde la API.
    private val userService by lazy { RetrofitClient.authUserService }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        userManager = UserManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
    }

    /**
     * Se llama cuando el fragmento se vuelve visible. Llama a updateUi()
     * para asegurar que la vista siempre refleje el estado de sesión actual.
     */
    override fun onResume() {
        super.onResume()
        updateUi()
    }

    /**
     * Comprueba si el usuario ha iniciado sesión y llama a la función correspondiente
     * para mostrar la interfaz de usuario adecuada (logueado o invitado).
     */
    private fun updateUi() {
        android.util.Log.d("ProfileFragment", "=== UPDATE UI ===")
        android.util.Log.d("ProfileFragment", "¿Está logueado?: ${tokenManager.isLoggedIn()}")
        android.util.Log.d("ProfileFragment", "User ID guardado: ${userManager.getUserId()}")
        android.util.Log.d("ProfileFragment", "Rol guardado: ${userManager.getUserRole()}")
        android.util.Log.d("ProfileFragment", "¿Es admin?: ${userManager.isAdmin()}")

        if (tokenManager.isLoggedIn()) {
            showLoggedInUi()
            loadUserProfile()
        } else {
            showGuestUi()
        }
    }

    /**
     * Carga el perfil del usuario desde la caché local primero, luego actualiza desde la API.
     */
    private fun loadUserProfile() {
        android.util.Log.d("ProfileFragment", "=== LOAD USER PROFILE ===")

        // PASO 1: Mostrar inmediatamente los datos en caché (si existen)
        val cachedUser = userManager.getUser()
        android.util.Log.d("ProfileFragment", "Usuario en caché: $cachedUser")

        if (cachedUser != null) {
            android.util.Log.d("ProfileFragment", "✅ Mostrando usuario en caché")
            android.util.Log.d("ProfileFragment", "Datos: ID=${cachedUser.id}, name=${cachedUser.name}, email=${cachedUser.email}, role=${cachedUser.role}")
            displayUserProfile(cachedUser)
        } else {
            android.util.Log.d("ProfileFragment", "⚠️ No hay usuario en caché, mostrando loading")
            // Si no hay caché, mostrar loading
            showLoading(true)
        }

        // PASO 2: Actualizar desde la API en segundo plano
        android.util.Log.d("ProfileFragment", "=== ACTUALIZANDO PERFIL DESDE API ===")
        android.util.Log.d("ProfileFragment", "Rol guardado: ${userManager.getUserRole()}")
        android.util.Log.d("ProfileFragment", "¿Es admin?: ${userManager.isAdmin()}")

        val userId = userManager.getUserId()
        android.util.Log.d("ProfileFragment", "User ID para consulta: $userId")

        lifecycleScope.launch {
            try {
                if (userId != -1) {
                    android.util.Log.d("ProfileFragment", "📡 Llamando a API: GET /user/$userId")

                    // Llamamos al endpoint GET /user/{user_id} que devuelve TODOS los campos
                    val updatedUser = RetrofitClient.storeUserService.getUserById(userId)

                    android.util.Log.d("ProfileFragment", "✅ Usuario recibido de API: $updatedUser")
                    android.util.Log.d("ProfileFragment", "Rol desde API: ${updatedUser.role}")

                    // Si el usuario no tiene rol o es null, usamos el que ya teníamos guardado
                    val finalUser = if (updatedUser.role.isNullOrEmpty()) {
                        val savedRole = userManager.getUserRole()
                        android.util.Log.d("ProfileFragment", "⚠️ API no devolvió rol, usando el guardado: $savedRole")
                        updatedUser.copy(role = savedRole)
                    } else {
                        updatedUser
                    }

                    // Guardamos y mostramos los datos actualizados
                    android.util.Log.d("ProfileFragment", "💾 Guardando usuario actualizado")
                    userManager.saveUser(finalUser)
                    displayUserProfile(finalUser)

                    android.util.Log.d("ProfileFragment", "✅ Perfil actualizado exitosamente")
                } else {
                    android.util.Log.e("ProfileFragment", "❌ User ID es -1, no se puede cargar perfil")
                }
            } catch (e: Exception) {
                android.util.Log.e("ProfileFragment", "❌ Error al actualizar perfil desde API: ${e.message}", e)
                // Si falla la actualización pero ya teníamos datos en caché, no hacemos nada
                // Los datos en caché ya se están mostrando
                if (cachedUser == null) {
                    Toast.makeText(requireContext(), "Error al cargar perfil: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } finally {
                showLoading(false)
            }
        }
    }

    /**
     * Muestra los datos del usuario en la interfaz.
     */
    private fun displayUserProfile(user: User) {
        val isAdmin = userManager.isAdmin()

        with(binding) {
            // Muestra el nombre del usuario
            userNameTextView.text = user.name
            userNameTextView.visibility = View.VISIBLE

            // Muestra el primer nombre si existe
            if (!user.firstName.isNullOrBlank()) {
                userFirstNameTextView.text = "Primer Nombre: ${user.firstName}"
                userFirstNameTextView.visibility = View.VISIBLE
            } else {
                userFirstNameTextView.visibility = View.GONE
            }

            // Muestra el apellido si existe
            if (!user.lastName.isNullOrBlank()) {
                userLastNameTextView.text = "Apellido: ${user.lastName}"
                userLastNameTextView.visibility = View.VISIBLE
            } else {
                userLastNameTextView.visibility = View.GONE
            }

            // Muestra el email
            userEmailTextView.text = user.email
            userEmailTextView.visibility = View.VISIBLE

            // Muestra el teléfono si existe
            if (!user.phone.isNullOrBlank()) {
                userPhoneTextView.text = "Teléfono: ${user.phone}"
                userPhoneTextView.visibility = View.VISIBLE
            } else {
                userPhoneTextView.visibility = View.GONE
            }

            // Muestra la dirección solo para usuarios normales (no admin)
            if (isAdmin) {
                // Si es admin, siempre ocultar la dirección
                userAddressTextView.visibility = View.GONE
                android.util.Log.d("ProfileFragment", "Dirección oculta para administrador")
            } else {
                // Si es usuario normal, mostrar solo si tiene dirección
                if (!user.shippingAddress.isNullOrBlank()) {
                    userAddressTextView.text = "Dirección: ${user.shippingAddress}"
                    userAddressTextView.visibility = View.VISIBLE
                } else {
                    userAddressTextView.visibility = View.GONE
                }
            }

            // Muestra el rol
            userRoleTextView.text = "Rol: ${user.role ?: "Usuario"}"
            userRoleTextView.visibility = View.VISIBLE

            // Muestra el mensaje de bienvenida personalizado
            welcomeMessage.text = "¡Hola, ${user.name}!"
            welcomeMessage.visibility = View.VISIBLE
        }
    }

    /**
     * Muestra la interfaz para un usuario que ha iniciado sesión.
     * Oculta los elementos de invitado y muestra un mensaje de bienvenida y el botón de cerrar sesión.
     */
    private fun showLoggedInUi() {
        binding.logoutButton.visibility = View.VISIBLE
        binding.editProfileButton.visibility = View.VISIBLE
        binding.guestMessage.visibility = View.GONE
        binding.guestLoginButton.visibility = View.GONE
        binding.guestSignupButton.visibility = View.GONE

        // Configura la acción para el botón de editar perfil.
        binding.editProfileButton.setOnClickListener {
            showEditProfileDialog()
        }

        // Configura la acción para el botón de cerrar sesión.
        binding.logoutButton.setOnClickListener {
            tokenManager.clearToken() // Borra el token del dispositivo.
            userManager.clearUser()   // Borra los datos del usuario.
            navigateToLogin()         // Navega a la pantalla de inicio de sesión.
        }
    }

    /**
     * Muestra la interfaz para un usuario en modo invitado.
     * Oculta los elementos de usuario logueado y muestra botones para iniciar sesión o crear una cuenta.
     */
    private fun showGuestUi() {
        binding.welcomeMessage.visibility = View.GONE
        binding.userNameTextView.visibility = View.GONE
        binding.userFirstNameTextView.visibility = View.GONE
        binding.userLastNameTextView.visibility = View.GONE
        binding.userEmailTextView.visibility = View.GONE
        binding.userPhoneTextView.visibility = View.GONE
        binding.userAddressTextView.visibility = View.GONE
        binding.userRoleTextView.visibility = View.GONE
        binding.logoutButton.visibility = View.GONE
        binding.editProfileButton.visibility = View.GONE
        binding.guestMessage.visibility = View.VISIBLE
        binding.guestLoginButton.visibility = View.VISIBLE
        binding.guestSignupButton.visibility = View.VISIBLE

        // Configura la acción para el botón de iniciar sesión.
        binding.guestLoginButton.setOnClickListener {
            navigateToLogin()
        }

        // Configura la acción para el botón de crear cuenta.
        binding.guestSignupButton.setOnClickListener {
            val intent = Intent(requireActivity(), SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Muestra el diálogo para editar el perfil del usuario.
     */
    private fun showEditProfileDialog() {
        val currentUser = userManager.getUser() ?: return
        val isAdmin = userManager.isAdmin()

        val dialogView = layoutInflater.inflate(com.example.galletas.R.layout.dialog_edit_profile, null)
        val editName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_name)
        val editFirstName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_first_name)
        val editLastName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_last_name)
        val editEmail = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_email)
        val editPhone = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_phone)
        val editAddress = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_address)
        val editPassword = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_password)

        // Ocultar el campo de dirección para administradores
        // Obtenemos el TextInputLayout padre del campo de dirección
        if (isAdmin && editAddress.parent != null && editAddress.parent.parent != null) {
            val addressInputLayout = editAddress.parent.parent as? android.view.View
            addressInputLayout?.visibility = View.GONE
            android.util.Log.d("ProfileFragment", "Campo de dirección oculto para administrador")
        }

        // Pre-llenar los campos con los datos actuales
        editName.setText(currentUser.name)
        editFirstName.setText(currentUser.firstName ?: "")
        editLastName.setText(currentUser.lastName ?: "")
        editEmail.setText(currentUser.email)
        editPhone.setText(currentUser.phone ?: "")
        if (!isAdmin) {
            editAddress.setText(currentUser.shippingAddress ?: "")
        }

        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar Perfil")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = editName.text.toString().trim()
                val newFirstName = editFirstName.text.toString().trim()
                val newLastName = editLastName.text.toString().trim()
                val newEmail = editEmail.text.toString().trim()
                val newPhone = editPhone.text.toString().trim()
                val newAddress = if (isAdmin) null else editAddress.text.toString().trim().ifBlank { null }
                val newPassword = editPassword.text.toString().trim()

                // Validaciones básicas
                if (newName.isBlank() || newEmail.isBlank()) {
                    Toast.makeText(requireContext(), "Nombre y email son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    Toast.makeText(requireContext(), "Email inválido", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Crear usuario actualizado
                val updatedUser = currentUser.copy(
                    name = newName,
                    firstName = newFirstName.ifBlank { null },
                    lastName = newLastName.ifBlank { null },
                    email = newEmail,
                    phone = newPhone.ifBlank { null },
                    shippingAddress = newAddress,  // null para admin
                    password = newPassword.ifBlank { null }  // Solo si se cambió
                )

                // Actualizar el perfil
                updateProfile(updatedUser)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Actualiza el perfil del usuario en el servidor.
     * Para admins, si no existe en la API, guarda solo localmente.
     */
    private fun updateProfile(updatedUser: User) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                // Verificar si el usuario es admin
                val isAdmin = userManager.isAdmin()

                if (isAdmin) {
                    android.util.Log.d("ProfileFragment", "🔧 Admin detectado, intentando actualizar en API...")
                }

                // Intentar actualizar en el servidor
                val user = RetrofitClient.storeUserService.updateUser(updatedUser.id, updatedUser)

                // Si llega aquí, el update fue exitoso
                userManager.saveUser(user)
                displayUserProfile(user)

                Toast.makeText(requireContext(), "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()

            } catch (e: retrofit2.HttpException) {
                android.util.Log.e("ProfileFragment", "Error HTTP al actualizar perfil: ${e.code()}", e)

                // Si es admin y da 404, guardar solo localmente
                if (userManager.isAdmin() && e.code() == 404) {
                    android.util.Log.d("ProfileFragment", "⚠️ Admin no existe en API, guardando solo localmente")

                    // Guardar datos localmente
                    userManager.saveUser(updatedUser)
                    displayUserProfile(updatedUser)

                    Toast.makeText(
                        requireContext(),
                        "✅ Perfil actualizado localmente\n(Solo visible en este dispositivo)",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Para otros errores o usuarios normales, mostrar error
                    android.util.Log.e("ProfileFragment", "Error al actualizar perfil: HTTP ${e.code()}")
                    Toast.makeText(
                        requireContext(),
                        "Error al actualizar perfil: HTTP ${e.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                android.util.Log.e("ProfileFragment", "Error al actualizar perfil: ${e.message}", e)

                // Si es admin, intentar guardar localmente como fallback
                if (userManager.isAdmin()) {
                    android.util.Log.d("ProfileFragment", "⚠️ Error al actualizar admin, guardando solo localmente")

                    userManager.saveUser(updatedUser)
                    displayUserProfile(updatedUser)

                    Toast.makeText(
                        requireContext(),
                        "✅ Perfil actualizado localmente\n(Solo visible en este dispositivo)",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error al actualizar perfil: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } finally {
                showLoading(false)
            }
        }
    }

    /**
     * Muestra u oculta el indicador de carga.
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Navega a la MainActivity (pantalla de login) y limpia el historial de navegación.
     * Esto evita que el usuario pueda volver a la pantalla de perfil con el botón "atrás".
     */
    private fun navigateToLogin() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    /**
     * Limpia la referencia al binding para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
