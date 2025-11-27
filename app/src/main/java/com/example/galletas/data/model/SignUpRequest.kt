package com.example.galletas.data.model

/**
 * Modelo de datos para el cuerpo de la petición de registro de un nuevo usuario.
 * Representa el JSON que se envía a la API para crear una cuenta.
 * `{"name": "John Doe", "email": "john@example.com", "password": "secret123"}`
 *
 * @param name El nombre del usuario.
 * @param email El email del usuario.
 * @param password La contraseña que el usuario ha elegido.
 */
data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String
)
