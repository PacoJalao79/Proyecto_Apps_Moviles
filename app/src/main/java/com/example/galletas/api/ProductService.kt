package com.example.galletas.api

import com.example.galletas.data.model.CreateProductRequest
import com.example.galletas.data.model.CreateProductResponse
import com.example.galletas.data.model.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz de Retrofit para los endpoints relacionados con los productos.
 * Define las operaciones de la API para obtener, crear y eliminar productos.
 */
interface ProductService {

    /**
     * Obtiene la lista completa de productos desde el servidor.
     * @return Una lista de objetos Product.
     */
    @GET("product")
    suspend fun getProducts(): List<Product>

    /**
     * Envía una petición para crear un nuevo producto en el servidor.
     * @param request El cuerpo de la petición con los datos del nuevo producto.
     * @return La respuesta del servidor tras la creación del producto.
     */
    @POST("product")
    suspend fun createProduct(@Body request: CreateProductRequest): CreateProductResponse

    /**
     * Envía una petición para eliminar un producto específico del servidor.
     * @param productId El ID del producto a eliminar, que se pasa en la URL.
     * @return Una respuesta vacía (Response<Unit>) si la operación es exitosa.
     */
    @DELETE("product/{product_id}")
    suspend fun deleteProduct(@Path("product_id") productId: Int): Response<Unit>
}
