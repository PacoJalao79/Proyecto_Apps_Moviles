package com.example.galletas.util

import android.content.Context
import android.content.SharedPreferences
import com.example.galletas.data.model.User
import com.google.gson.Gson

/**
 * Gestiona el almacenamiento y recuperación de los datos del usuario autenticado.
 * Guarda información como ID, nombre, email y rol para uso en toda la aplicación.
 *
 * @param context Contexto de la aplicación, necesario para acceder a SharedPreferences.
 */
class UserManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "galletas_user_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ROLE = "user_role"
        private const val KEY_USER_JSON = "user_json"

        // Constantes para los roles
        const val ROLE_ADMIN = "admin"
        const val ROLE_USER = "user"
        const val ROLE_CLIENTE = "cliente"
    }

    /**
     * Guarda todos los datos del usuario en SharedPreferences.
     * @param user El objeto User con toda la información del usuario.
     */
    fun saveUser(user: User) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_ROLE, user.role ?: ROLE_USER) // Por defecto "user" si es null
            putString(KEY_USER_JSON, gson.toJson(user)) // Guardamos también el JSON completo
            apply()
        }
    }

    /**
     * Guarda manualmente el rol del usuario.
     * Útil si necesitas actualizar solo el rol sin tener el objeto User completo.
     * @param role El rol del usuario.
     */
    fun saveUserRole(role: String) {
        prefs.edit().putString(KEY_USER_ROLE, role).apply()
    }

    /**
     * Obtiene el rol del usuario guardado.
     * @return El rol del usuario, o "user" por defecto si no hay ninguno guardado.
     */
    fun getUserRole(): String {
        return prefs.getString(KEY_USER_ROLE, ROLE_USER) ?: ROLE_USER
    }

    /**
     * Obtiene el ID del usuario guardado.
     * @return El ID del usuario, o -1 si no hay ninguno guardado.
     */
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, -1)
    }

    /**
     * Obtiene el nombre del usuario guardado.
     * @return El nombre del usuario, o null si no hay ninguno guardado.
     */
    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    /**
     * Obtiene el email del usuario guardado.
     * @return El email del usuario, o null si no hay ninguno guardado.
     */
    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    /**
     * Recupera el objeto User completo desde SharedPreferences.
     * @return El objeto User, o null si no hay datos guardados.
     */
    fun getUser(): User? {
        val userJson = prefs.getString(KEY_USER_JSON, null)
        return if (userJson != null) {
            try {
                gson.fromJson(userJson, User::class.java)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    /**
     * Verifica si el usuario actual es administrador.
     * @return true si el usuario tiene rol de admin, false en caso contrario.
     */
    fun isAdmin(): Boolean {
        return getUserRole() == ROLE_ADMIN
    }

    /**
     * Verifica si el usuario actual es un usuario normal/cliente.
     * @return true si el usuario tiene rol de user o cliente, false en caso contrario.
     */
    fun isRegularUser(): Boolean {
        val role = getUserRole()
        return role == ROLE_USER || role == ROLE_CLIENTE
    }

    /**
     * Verifica si hay datos de usuario guardados.
     * @return true si existe información del usuario, false en caso contrario.
     */
    fun hasUserData(): Boolean {
        return getUserId() != -1
    }

    /**
     * Limpia todos los datos del usuario almacenados.
     * Debe llamarse cuando el usuario cierra sesión.
     */
    fun clearUser() {
        prefs.edit().clear().apply()
    }

    /**
     * Detecta el rol basándose en el email (método alternativo).
     * Útil como fallback si el backend no devuelve el rol.
     * Si el email termina en "@gmail.com.admin", se considera administrador.
     *
     * @param email El email del usuario.
     * @return El rol detectado basándose en el email.
     */
    fun detectRoleFromEmail(email: String): String {
        return if (email.endsWith("@gmail.com.admin")) {
            ROLE_ADMIN
        } else {
            ROLE_USER
        }
    }
}

