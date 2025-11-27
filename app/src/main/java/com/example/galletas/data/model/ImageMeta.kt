package com.example.galletas.data.model

import java.io.Serializable

/**
 * Modela el objeto `meta` anidado dentro de la respuesta de una imagen de la API de Xano.
 * Contiene los metadatos de la imagen, como sus dimensiones.
 * `"meta": {"width": 800, "height": 1200}`
 *
 * @param width El ancho de la imagen en píxeles.
 * @param height El alto de la imagen en píxeles.
 */
data class ImageMeta(
    val width: Int?,
    val height: Int?
) : Serializable
