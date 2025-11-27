package com.example.galletas.data.model

/**
 * Modelo de datos para el cuerpo de la petición de inicio de sesión.
 * Representa el JSON que se envía a la API para hacer login.
 * `{"email": "user@example.com", "password": "secret"}`
 *
 * @param email El email del usuario.
 * @param password La contraseña del usuario.
 */
data class LoginRequest(
    val email: String,
    val password: String
)
