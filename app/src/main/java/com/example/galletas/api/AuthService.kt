package com.example.galletas.api

import com.example.galletas.data.model.AuthResponse
import com.example.galletas.data.model.LoginRequest
import com.example.galletas.data.model.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz de Retrofit para los endpoints de autenticación (login y registro).
 * Define las llamadas a la API que se pueden realizar para gestionar la autenticación de usuarios.
 */
interface AuthService {

    /**
     * Realiza una petición POST para iniciar sesión.
     * @param request El cuerpo de la petición, que contiene el email y la contraseña del usuario.
     * @return Un objeto AuthResponse que contiene el token de autenticación si el login es exitoso.
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    /**
     * Realiza una petición POST para registrar un nuevo usuario.
     * @param request El cuerpo de la petición, con el nombre, email y contraseña del nuevo usuario.
     * @return Un objeto AuthResponse con el token de autenticación para la nueva sesión.
     */
    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): AuthResponse

}
