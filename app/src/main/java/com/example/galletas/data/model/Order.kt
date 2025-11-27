package com.example.galletas.data.model

data class Order(
    val id: Int,
    val created_at: Long,  // Timestamp en milisegundos
    val total: Double,
    val status: String?,
    val user_id: Int,
    val _user: User? = null
)

