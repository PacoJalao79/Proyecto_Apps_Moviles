package com.example.galletas.data.model

import java.io.Serializable

/**
 * Modelo de datos que representa una imagen de producto, tanto la que se envía como la que se recibe de la API.
 * Contiene todos los detalles de una imagen subida al servidor de Xano.
 *
 * @param path La ruta relativa del archivo en el servidor.
 * @param name El nombre del archivo.
 * @param type El tipo de archivo (ej: "image").
 * @param size El tamaño del archivo en bytes.
 * @param mime El tipo MIME del archivo (ej: "image/jpeg").
 * @param url La URL completa y pública para acceder a la imagen.
 * @param meta Un objeto `ImageMeta` que contiene los metadatos de la imagen (ancho y alto).
 * @param access El nivel de acceso del archivo (ej: "public").
 */
data class ProductImage(
    val path: String?,
    val name: String?,
    val type: String?,
    val size: Int?,
    val mime: String?,
    val url: String?,
    val meta: ImageMeta?,
    val access: String?
) : Serializable
