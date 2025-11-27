package com.example.galletas.api

import com.example.galletas.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    // Endpoint de Auth API - Solo para obtener usuario actual
    @GET("auth/me")
    suspend fun getCurrentUser(): User

    // Endpoints de E-commerce API - Para gestión de usuarios
    @GET("user")
    suspend fun getAllUsers(): List<User>

    @GET("user/{user_id}")
    suspend fun getUserById(@Path("user_id") userId: Int): User

    @PUT("user/{user_id}")
    suspend fun updateUser(@Path("user_id") userId: Int, @Body user: User): User

    @DELETE("user/{user_id}")
    suspend fun deleteUser(@Path("user_id") userId: Int): Response<Unit>
}

