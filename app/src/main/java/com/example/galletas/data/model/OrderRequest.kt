package com.example.galletas.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para el cuerpo de la petición de creación de una orden.
 * Representa el JSON que se envía a la API para realizar una compra.
 *
 * @param products Una lista de enteros, donde cada entero es el `id` de un producto a comprar.
 * @param total El total de la compra calculado desde el carrito.
 * @param userId El ID del usuario que realiza el pedido.
 */
data class OrderRequest(
    val products: List<Int>,
    val total: Double,
    @SerializedName("user_id") val userId: Int
)
