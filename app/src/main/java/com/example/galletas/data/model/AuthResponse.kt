package com.example.galletas.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para la respuesta de autenticación de la API.
 * Representa el JSON que se recibe del servidor tras un login o registro exitoso.
 *
 * Xano puede devolver dos formatos:
 * 1. Solo token: `{"authToken": "a_very_long_jwt_token"}`
 * 2. Token + datos de usuario: `{"authToken": "...", "user": {...}}`
 *
 * @param token El token de autenticación (JWT) que se usará para las futuras llamadas a la API.
 * @param user Los datos del usuario autenticado (opcional). Si Xano lo devuelve, lo capturamos.
 */
data class AuthResponse(
    @SerializedName("authToken")
    val token: String,

    @SerializedName("user")
    val user: User? = null
)
