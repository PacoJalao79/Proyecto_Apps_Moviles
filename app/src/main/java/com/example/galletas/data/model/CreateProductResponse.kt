package com.example.galletas.data.model

import java.io.Serializable

/**
 * Modelo de datos para la respuesta de creación de un producto.
 * Representa el JSON que se recibe del servidor tras crear un producto con éxito.
 *
 * @param id El ID del nuevo producto que se ha creado en la base de datos.
 * @param createdAt La fecha y hora de creación en formato timestamp (milisegundos).
 */
data class CreateProductResponse(
    val id: Int,
    val createdAt: Long
) : Serializable
