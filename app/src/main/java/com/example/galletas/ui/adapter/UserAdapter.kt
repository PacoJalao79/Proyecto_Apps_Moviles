package com.example.galletas.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.galletas.data.model.User
import com.example.galletas.databinding.ItemUserBinding

/**
 * Adapter para mostrar la lista de usuarios en el panel de administración.
 *
 * @param users Lista de usuarios a mostrar.
 * @param onUserClicked Callback cuando se hace click en un usuario para ver detalles.
 * @param onDeleteClicked Callback cuando se hace click en eliminar un usuario.
 */
class UserAdapter(
    private var users: List<User>,
    private val onUserClicked: (User) -> Unit,
    private val onDeleteClicked: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        with(holder.binding) {
            userNameTextView.text = user.name
            userEmailTextView.text = user.email
            userIdTextView.text = "ID: ${user.id}"
            userRoleTextView.text = "Rol: ${user.role ?: "Usuario"}"

            // Click en el item completo para ver detalles
            root.setOnClickListener {
                onUserClicked(user)
            }

            // Click en el botón de eliminar
            deleteUserButton.setOnClickListener {
                onDeleteClicked(user)
            }
        }
    }

    override fun getItemCount() = users.size

    /**
     * Actualiza la lista de usuarios.
     */
    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}

