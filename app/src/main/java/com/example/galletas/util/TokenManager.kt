package com.example.galletas.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Gestiona el almacenamiento y la recuperación del token de autenticación en el dispositivo.
 * Esta clase utiliza SharedPreferences, un sistema de Android para guardar pares de clave-valor
 * de forma persistente, ideal para guardar pequeños datos como un token de sesión.
 *
 * @param context Contexto de la aplicación, necesario para acceder a SharedPreferences.
 */
class TokenManager(context: Context) {

    // Instancia de SharedPreferences. El archivo se llamará "galletas_prefs".
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Objeto compañero para definir constantes que se usarán en esta clase.
    companion object {
        private const val PREFS_NAME = "galletas_prefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    /**
     * Guarda el token de autenticación en SharedPreferences.
     * @param token El token JWT recibido del servidor.
     */
    fun saveAuthToken(token: String) {
        prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    /**
     * Recupera el token de autenticación desde SharedPreferences.
     * @return El token almacenado, o `null` si no se encuentra ninguno.
     */
    fun getToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    /**
     * Limpia (borra) el token de autenticación almacenado.
     * Debe llamarse cuando el usuario cierra sesión.
     */
    fun clearToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    /**
     * Verifica si hay un token de autenticación guardado.
     * Esto es una forma sencilla de saber si el usuario ha iniciado sesión.
     * @return `true` si existe un token, `false` en caso contrario.
     */
    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}
