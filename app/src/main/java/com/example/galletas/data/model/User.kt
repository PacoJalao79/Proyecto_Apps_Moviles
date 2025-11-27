package com.example.galletas.data.model

import com.google.gson.annotations.SerializedName

/**
 * Clase de datos que representa el perfil de un usuario.
 */
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String? = null,  // Solo para enviar, nunca se recibe del servidor
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    val role: String? = null,
    val status: String? = null,
    @SerializedName("shipping_address") val shippingAddress: String? = null,
    val phone: String? = null,
    @SerializedName("created_at") val createdAt: Long? = null
)
