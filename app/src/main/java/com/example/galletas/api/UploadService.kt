package com.example.galletas.api

import com.example.galletas.data.model.ProductImage
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Interfaz de Retrofit para el endpoint de subida de archivos (imágenes).
 */
interface UploadService {

    /**
     * Sube una imagen al servidor usando una petición de tipo multipart/form-data.
     * La anotación @Multipart indica que esta es una petición de este tipo.
     * La autenticación es gestionada por el AuthInterceptor.
     *
     * @param image La parte del cuerpo de la petición que contiene el archivo de imagen.
     *              La anotación @Part define esta parte del formulario.
     * @return Un objeto ProductImage con la información de la imagen que el servidor ha guardado (URL, path, etc.).
     */
    @Multipart
    @POST("upload/image")
    suspend fun uploadImage(@Part image: MultipartBody.Part): ProductImage
}
