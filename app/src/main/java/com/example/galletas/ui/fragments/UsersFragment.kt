package com.example.galletas.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.galletas.api.RetrofitClient
import com.example.galletas.data.model.User
import com.example.galletas.databinding.FragmentUsersBinding
import com.example.galletas.ui.adapter.UserAdapter
import com.example.galletas.util.UserManager
import kotlinx.coroutines.launch

/**
 * Fragmento para gestionar usuarios (solo accesible para administradores).
 * Permite ver la lista de usuarios, editar y eliminar usuarios.
 */
class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var userManager: UserManager
    private lateinit var userAdapter: UserAdapter
    private val userService by lazy { RetrofitClient.storeUserService }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        userManager = UserManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Verifica que el usuario sea administrador
        if (!userManager.isAdmin()) {
            Toast.makeText(requireContext(), "Acceso denegado: Solo administradores", Toast.LENGTH_LONG).show()
            return
        }

        setupRecyclerView()
        loadUsers()
    }

    /**
     * Configura el RecyclerView con el adapter de usuarios.
     */
    private fun setupRecyclerView() {
        userAdapter = UserAdapter(
            users = emptyList(),
            onUserClicked = { user ->
                showUserDetailsDialog(user)
            },
            onDeleteClicked = { user ->
                showDeleteConfirmationDialog(user)
            }
        )

        binding.usersRecyclerView.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Carga la lista de usuarios desde la API.
     */
    private fun loadUsers() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val users = userService.getAllUsers()
                if (users.isEmpty()) {
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.usersRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                    binding.usersRecyclerView.visibility = View.VISIBLE
                    userAdapter.updateUsers(users)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al cargar usuarios: ${e.message}", Toast.LENGTH_LONG).show()
                binding.emptyTextView.visibility = View.VISIBLE
                binding.emptyTextView.text = "Error al cargar usuarios"
            } finally {
                showLoading(false)
            }
        }
    }

    /**
     * Muestra un diálogo con los detalles del usuario.
     */
    private fun showUserDetailsDialog(user: User) {
        AlertDialog.Builder(requireContext())
            .setTitle("Detalles del Usuario")
            .setMessage("""
                Nombre: ${user.name}
                Email: ${user.email}
                Rol: ${user.role ?: "Usuario"}
                ID: ${user.id}
            """.trimIndent())
            .setPositiveButton("Editar") { _, _ ->
                showEditUserDialog(user)
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }

    /**
     * Muestra un diálogo para editar los datos del usuario.
     */
    private fun showEditUserDialog(user: User) {
        val dialogView = layoutInflater.inflate(com.example.galletas.R.layout.dialog_edit_profile, null)

        // Obtener referencias a los campos
        val editName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_name)
        val editFirstName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_first_name)
        val editLastName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_last_name)
        val editEmail = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_email)
        val editPhone = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_phone)
        val editAddress = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_address)
        val editPassword = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.galletas.R.id.edit_password)

        // Pre-llenar los campos con los datos actuales del usuario
        editName.setText(user.name)
        editFirstName.setText(user.firstName ?: "")
        editLastName.setText(user.lastName ?: "")
        editEmail.setText(user.email)
        editPhone.setText(user.phone ?: "")
        editAddress.setText(user.shippingAddress ?: "")

        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Editar Usuario: ${user.name}")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val newName = editName.text.toString().trim()
                val newFirstName = editFirstName.text.toString().trim()
                val newLastName = editLastName.text.toString().trim()
                val newEmail = editEmail.text.toString().trim()
                val newPhone = editPhone.text.toString().trim()
                val newAddress = editAddress.text.toString().trim()
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
                val updatedUser = user.copy(
                    name = newName,
                    firstName = newFirstName.ifBlank { null },
                    lastName = newLastName.ifBlank { null },
                    email = newEmail,
                    phone = newPhone.ifBlank { null },
                    shippingAddress = newAddress.ifBlank { null },
                    password = newPassword.ifBlank { null }  // Solo si se cambió
                )

                // Actualizar el usuario
                updateUser(updatedUser)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Actualiza los datos de un usuario en el servidor.
     */
    private fun updateUser(user: User) {
        showLoading(true)

        lifecycleScope.launch {
            try {
                // Llamar al endpoint PUT /user/{user_id}
                userService.updateUser(user.id, user)

                Toast.makeText(requireContext(), "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()

                // Recargar la lista de usuarios
                loadUsers()

            } catch (e: Exception) {
                showLoading(false)
                android.util.Log.e("UsersFragment", "Error al actualizar usuario: ${e.message}", e)
                Toast.makeText(requireContext(), "Error al actualizar usuario: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar un usuario.
     */
    private fun showDeleteConfirmationDialog(user: User) {
        // Evita que el admin se elimine a sí mismo
        if (user.id == userManager.getUserId()) {
            Toast.makeText(requireContext(), "No puedes eliminarte a ti mismo", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar al usuario '${user.name}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteUser(user)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Elimina un usuario de la base de datos.
     */
    private fun deleteUser(user: User) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                userService.deleteUser(user.id)
                Toast.makeText(requireContext(), "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show()
                loadUsers() // Recarga la lista
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(requireContext(), "Error al eliminar usuario: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Muestra u oculta el indicador de carga.
     */
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

