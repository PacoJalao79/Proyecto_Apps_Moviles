package com.example.galletas.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para el cuerpo de la petición de creación de un producto.
 * Representa el JSON que se envía a la API para crear un nuevo producto.
 *
 * @param name El nombre del producto.
 * @param price El precio del producto.
 * @param description La descripción del producto.
 * @param stock La cantidad de producto en stock.
 * @param brand La marca del producto.
 * @param category La categoría del producto.
 * @param image Una lista de objetos `ProductImage` que han sido previamente subidos y asociados al producto.
 *              La anotación `@SerializedName("image")` asegura que en el JSON final, este campo se llame "image".
 */
data class CreateProductRequest(
    val name: String,
    val price: Double,
    val description: String,
    val stock: Int,
    val brand: String,
    val category: String,
    @SerializedName("image")
    val image: List<ProductImage>
)
