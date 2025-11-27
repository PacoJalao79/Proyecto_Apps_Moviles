package com.example.galletas.data.model

import java.io.Serializable

/**
 * Modelo de datos que representa un producto de la tienda.
 * Esta clase implementa `Serializable` para que sus instancias puedan ser pasadas
 * entre componentes de Android (aunque en este proyecto no se usa directamente).
 *
 * @param id El identificador único del producto.
 * @param name El nombre del producto.
 * @param price El precio del producto.
 * @param image Una lista de objetos `ProductImage` asociados a este producto.
 */
data class Product(
    val id: Int,
    val name: String?,
    val price: Double?,
    val image: List<ProductImage>?
) : Serializable
